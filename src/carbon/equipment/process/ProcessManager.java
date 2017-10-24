package carbon.equipment.process;

import java.util.Iterator;
import java.util.LinkedList;

import carbon.equipment.Equipment;
import carbon.equipment.IFEquipment;
import carbon.equipment.command.OrderInfo;
import carbon.equipment.queue.QueueChennel;

public class ProcessManager implements Runnable{
	
	protected boolean isReady;
	
	protected Thread thread;
	
	private static ProcessManager instance;
	
	LinkedList<IFEquipment> equipmentList;
	
	QueueChennel chennel;
	 
	private ProcessManager()
	{
		equipmentList = new LinkedList<IFEquipment>();
		
		chennel = new QueueChennel();
	}
	
	public void addEquipment(IFEquipment equipment)
	{
		equipmentList.add(equipment);
	}

	public static ProcessManager getInstace() {
		
		if(instance == null)
			instance = new ProcessManager();
		
		return instance;
	}
	
	public OrderInfo getOrder() {
		
		return new OrderInfo(OrderInfo.MESSATE_TYPE_CREATE);
	}

	public synchronized void appendMessage(OrderInfo message) {
	
		chennel.append(message);
	}
	private void setOrderByIDLE(int equipmentType, OrderInfo order)
	{
		new Thread()
		{
			public void run()
			{
				Equipment selectedEquipment=null;
				
				do
				{
					Iterator<IFEquipment> iter = equipmentList.iterator();
					
					while(iter.hasNext())
					{
						Equipment item = (Equipment) iter.next();
						
						if(item.getEquipmentType()==equipmentType&&item.getState()==Equipment.STATE_IDLE)
						{				
							selectedEquipment = item;
						}
					}
				}
				while(selectedEquipment==null);
				
				selectedEquipment.executeOrder(order);
			}
		}.start();
	}
	private void setOrderByWorkCount(int equipmentType, OrderInfo order)
	{
		Iterator<IFEquipment> iter = equipmentList.iterator();
		
		Equipment selectedEquipment=(Equipment) iter.next();
		
		while(iter.hasNext())
		{
			Equipment item = (Equipment) iter.next();
			
			if(item.getEquipmentType()==equipmentType&&item.getWorkCount()<selectedEquipment.getWorkCount())
			{				
				selectedEquipment = item;
			}
		}
		selectedEquipment.executeOrder(order);
	}	
	

	@Override
	public void run() {
		
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			info.setMessageType(OrderInfo.MESSATE_TYPE_FROM_PROCESS);
			
			switch (info.getMessageType()) {
			
			case OrderInfo.MESSATE_TYPE_CREATE:				
				
				this.setOrderByWorkCount(Equipment.TYPE_QC,info);
				
				break;
				
			case OrderInfo.MESSATE_TYPE_FROM_QC:
				
				this.setOrderByIDLE(Equipment.TYPE_AGV,info);
				
				break;
			case OrderInfo.MESSATE_TYPE_FROM_AGV:
				
				this.setOrderByIDLE(Equipment.TYPE_ATC,info);
				
				break;
			case OrderInfo.MESSATE_TYPE_FROM_ATC:
				
				System.out.println("end: "+info);
				
				break;		
			default:
				break;
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
	public void equipmentStart()
	{
		Iterator<IFEquipment> iter = equipmentList.iterator();
		while(iter.hasNext())
		{
			IFEquipment item =  iter.next();
			item.equipmentStart();
		}
	}
	public void equipmentStop()
	{
		Iterator<IFEquipment> iter = equipmentList.iterator();
		while(iter.hasNext())
		{
			IFEquipment item = iter.next();
			item.equipmentStop();
		}
	}

	public void stop() {
		isReady=false;
		thread = null;		
	}

}
