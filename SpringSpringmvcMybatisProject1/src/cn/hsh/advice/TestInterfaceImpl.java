package cn.hsh.advice;

public class TestInterfaceImpl implements TestInterface,TestInterface2 {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void sayHello() {
		// TODO Auto-generated method stub
		System.out.println("Hello "+name);
	}

	@Override
	public void sayBye() {
		// TODO Auto-generated method stub
		System.out.println("Bye bye "+name);
		//int i=10/0;
	}

}
