package cn.hsh.util; 

import org.apache.ibatis.executor.parameter.ParameterHandler;  
import org.apache.ibatis.executor.resultset.ResultSetHandler;  
import org.apache.ibatis.executor.statement.StatementHandler;  
import org.apache.ibatis.mapping.BoundSql;  
import org.apache.ibatis.mapping.MappedStatement;  
import org.apache.ibatis.plugin.*;  
import org.apache.ibatis.reflection.MetaObject;  
import org.apache.ibatis.reflection.SystemMetaObject;  
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;  
import org.apache.log4j.Logger;  
import java.sql.*;  
import java.util.List;  
import java.util.Properties;  
  
/** 
 * Mybatis - ͨ�÷�ҳ������ 
 * @author liuzh/abel533/isea 
 * Created by liuzh on 14-4-15. 
 */  
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class}),  
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})  
public class PageHelper implements Interceptor {  
    private static final Logger logger = Logger.getLogger(PageHelper.class);  
  
    public static final ThreadLocal<Page> localPage = new ThreadLocal<Page>();  
  
    /** 
     * ��ʼ��ҳ 
     * @param pageNum 
     * @param pageSize 
     */  
    public static void startPage(Integer pageNum, Integer pageSize) {  
        localPage.set(new Page(pageNum, pageSize));  
    }  
  
    /** 
     * ������ҳ�����ؽ�����÷������뱻���ã�����localPage��һֱ������ȥ��ֱ����һ��startPage 
     * @return 
     */  
    public static Page endPage() {  
        Page page = localPage.get();  
        localPage.remove();  
        return page;  
    }  
  
    public Object intercept(Invocation invocation) throws Throwable {  
        if (localPage.get() == null) {  
            return invocation.proceed();  
        }  
        if (invocation.getTarget() instanceof StatementHandler) {  
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();  
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);  
            // ������������(����Ŀ������ܱ�������������أ��Ӷ��γɶ�δ���ͨ�����������ѭ��  
            // ���Է������ԭʼ�ĵ�Ŀ����)  
            while (metaStatementHandler.hasGetter("h")) {  
                Object object = metaStatementHandler.getValue("h");  
                metaStatementHandler = SystemMetaObject.forObject(object);  
            }  
            // �������һ����������Ŀ����  
            while (metaStatementHandler.hasGetter("target")) {  
                Object object = metaStatementHandler.getValue("target");  
                metaStatementHandler = SystemMetaObject.forObject(object);  
            }  
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");  
            //��ҳ��Ϣif (localPage.get() != null) {  
            Page page = localPage.get();  
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");  
            // ��ҳ������Ϊ��������parameterObject��һ������  
            String sql = boundSql.getSql();  
            // ��дsql  
            String pageSql = buildPageSql(sql, page);  
            //��д��ҳsql  
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);  
            Connection connection = (Connection) invocation.getArgs()[0];  
            // �����ҳ���������ҳ����  
            setPageParameter(sql, connection, mappedStatement, boundSql, page);  
            // ��ִ��Ȩ������һ��������  
            return invocation.proceed();  
        } else if (invocation.getTarget() instanceof ResultSetHandler) {  
            Object result = invocation.proceed();  
            Page page = localPage.get();  
            page.setResult((List) result);  
            return result;  
        }  
        return null;  
    }  
  
    /** 
     * ֻ�������������͵� 
     * <br>StatementHandler 
     * <br>ResultSetHandler 
     * @param target 
     * @return 
     */  
    public Object plugin(Object target) {  
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {  
            return Plugin.wrap(target, this);  
        } else {  
            return target;  
        }  
    }  
  
    public void setProperties(Properties properties) {  
  
    }  
  
    /** 
     * �޸�ԭSQLΪ��ҳSQL 
     * @param sql 
     * @param page 
     * @return 
     */  
    private String buildPageSql(String sql, Page page) {  
        StringBuilder pageSql = new StringBuilder(200);  
        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");  
        pageSql.append(sql);  
        pageSql.append(" ) temp where rownum <= ").append(page.getEndRow());  
        pageSql.append(") where row_id > ").append(page.getStartRow());  
        return pageSql.toString();  
    }  
  
    /** 
     * ��ȡ�ܼ�¼�� 
     * @param sql 
     * @param connection 
     * @param mappedStatement 
     * @param boundSql 
     * @param page 
     */  
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,  
                                  BoundSql boundSql, Page page) {  
        // ��¼�ܼ�¼��  
        String countSql = "select count(0) from (" + sql + ")";  
        PreparedStatement countStmt = null;  
        ResultSet rs = null;  
        try {  
            countStmt = connection.prepareStatement(countSql);  
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,  
                    boundSql.getParameterMappings(), boundSql.getParameterObject());  
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());  
            rs = countStmt.executeQuery();  
            int totalCount = 0;  
            if (rs.next()) {  
                totalCount = rs.getInt(1);  
            }  
            page.setTotal(totalCount);  
            int totalPage = totalCount / page.getPageSize() + ((totalCount % page.getPageSize() == 0) ? 0 : 1);  
            page.setPages(totalPage);  
        } catch (SQLException e) {  
            logger.error("Ignore this exception", e);  
        } finally {  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                logger.error("Ignore this exception", e);  
            }  
            try {  
                countStmt.close();  
            } catch (SQLException e) {  
                logger.error("Ignore this exception", e);  
            }  
        }  
    }  
  
    /** 
     * �������ֵ 
     * @param ps 
     * @param mappedStatement 
     * @param boundSql 
     * @param parameterObject 
     * @throws SQLException 
     */  
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,  
                               Object parameterObject) throws SQLException {  
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);  
        parameterHandler.setParameters(ps);  
    }  
  
    /** 
     * Description: ��ҳ 
     * Author: liuzh 
     * Update: liuzh(2014-04-16 10:56) 
     */  
    public static class Page<E> {  
        private int pageNum;  
        private int pageSize;  
        private int startRow;  
        private int endRow;  
        private int total;  
        private int pages;  
        private List<E> result;  
  
        public Page(int pageNum, int pageSize) {  
            this.pageNum = pageNum;  
            this.pageSize = pageSize;  
            this.startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;  
            this.endRow = pageNum * pageSize;  
        }  
  
        public List<E> getResult() {  
            return result;  
        }  
  
        public void setResult(List<E> result) {  
            this.result = result;  
        }  
  
        public int getPages() {  
            return pages;  
        }  
  
        public void setPages(int pages) {  
            this.pages = pages;  
        }  
  
        public int getEndRow() {  
            return endRow;  
        }  
  
        public void setEndRow(int endRow) {  
            this.endRow = endRow;  
        }  
  
        public int getPageNum() {  
            return pageNum;  
        }  
  
        public void setPageNum(int pageNum) {  
            this.pageNum = pageNum;  
        }  
  
        public int getPageSize() {  
            return pageSize;  
        }  
  
        public void setPageSize(int pageSize) {  
            this.pageSize = pageSize;  
        }  
  
        public int getStartRow() {  
            return startRow;  
        }  
  
        public void setStartRow(int startRow) {  
            this.startRow = startRow;  
        }  
  
        public int getTotal() {  
            return total;  
        }  
  
        public void setTotal(int total) {  
            this.total = total;  
        }  
  
        @Override  
        public String toString() {  
            return "Page{" +  
                    "pageNum=" + pageNum +  
                    ", pageSize=" + pageSize +  
                    ", startRow=" + startRow +  
                    ", endRow=" + endRow +  
                    ", total=" + total +  
                    ", pages=" + pages +  
                    '}';  
        }  
    }  
}  