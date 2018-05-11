package cn.hsh.util;

import java.util.List;

public class DataGrid {
	private int total;//总页数
	private List rows;//数据内容
	private List footer;//存储统计数据
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	public List getFooter() {
		return footer;
	}
	public void setFooter(List footer) {
		this.footer = footer;
	}
}
