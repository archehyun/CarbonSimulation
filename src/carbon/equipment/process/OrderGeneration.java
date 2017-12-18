package carbon.equipment.process;

import carbon.equipment.command.OrderInfo;

public class OrderGeneration implements Runnable{
	
	protected boolean isReady;
	
	protected Thread thread;
	
	private int orderID;
	OrderInfo info;
	ProcessManager manager = ProcessManager.getInstace();

	@Override
	public void run() {
		while(isReady)
		{
			try {
				
				info = new OrderInfo(orderID++);
				
				manager.appendMessage(info);
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void start() {
		if(thread ==null)
		{
			isReady=true;
			thread = new Thread(this);
			thread.start();
		}

	}

	public void stop() {
		isReady=false;
		thread = null;		
	}

}
