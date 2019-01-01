package TestClient;


	public class Threadable extends Thread {
	    String name;
	    SharedData theDemo;
	    
	    public Threadable(String name,SharedData theDemo) {
	        this.theDemo = theDemo;
	        this.name = name;
	        start();
	    }
	    
	    @Override
	    public void run() {
	        theDemo.test(name);
	    }
}
