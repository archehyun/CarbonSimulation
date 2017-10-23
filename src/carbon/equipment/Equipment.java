package carbon.equipment;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;
import carbon.equipment.queue.QueueChennel;

public abstract class Equipment implements IFEquipment, Runnable{
	
	protected int workCount;
	
	public static final int TYPE_QC = 1;
	
	public static final int TYPE_AGV = 2;
	
	public static final int TYPE_ATC = 3;
	
	public static final int STATE_IDLE = 0;
	public static final int STATE_BUSY = 1;
	
	protected boolean isReady;
	
	protected Thread thread;
	
	protected QueueChennel chennel;
	
	protected ProcessManager manager =ProcessManager.getInstace(); 
	
	private String ID;
	
	private int state;
	
	protected int equipmentType;

	public int getEquipmentType() {
		return equipmentType;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getID() {
		return ID;
	}
	public Equipment(String id) {
		this.ID = id;
		chennel = new QueueChennel();
		manager.addEquipment(this);
	}
	
	public void sendMessage(OrderInfo message)
	{
		manager.appendMessage(message);
	}
	@Override
	public void equipmentStart() {
		if(thread ==null)
		{
			isReady=true;
			thread = new Thread(this);
			thread.start();
		}

	}

	@Override
	public void equipmentStop() {
		isReady=false;
		thread = null;		
	}
	public int getWorkCount() {
		// TODO Auto-generated method stub
		return workCount;
	}
	public void updateWorkCount() {
		workCount++;
		
	}

}
