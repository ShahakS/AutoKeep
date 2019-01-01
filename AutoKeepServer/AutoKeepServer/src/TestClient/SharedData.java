package TestClient;

public class SharedData {
	private int i = 0;
	
	public synchronized void test(String name) {
        for(;i<10;i++) {
        	//test.print(name + " :: "+i);
        	System.out.println(name + " :: "+i);
            try{
                Thread.sleep(500);
            } catch (Exception e) {
                test.print(e.getMessage());
            }
        }
    }
	
	public synchronized void test2(String name) {
        for(;i<10;i++) {
        	//test.print(name + " :: "+i);
        	System.out.println(name + " :: "+i);
            try{
                Thread.sleep(500);
            } catch (Exception e) {
                test.print(e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        SharedData theDemo = new SharedData();
        new Threadable("THREAD 1",theDemo);
        new Threadable("THREAD 2",theDemo);
        new Threadable("THREAD 3",theDemo);
    }
}
