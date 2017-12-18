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
	
	private LinkedList<IFEquipment> equipmentList;
	
	private QueueChennel chennel;
	
	
	BlockManager blockManager;
	 
	private ProcessManager()
	{
		equipmentList = new LinkedList<IFEquipment>();
		
		blockManager = new BlockManager();
		
		chennel = new QueueChennel();
		
		selectEquipment = new SelectEquipment();
	}
	
	public LinkedList<IFEquipment> getEquipmentList() {
		return equipmentList;
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
	SelectEquipment selectEquipment;
	class SelectEquipment implements Runnable
	{
		
		OrderInfo order=null;
		private QueueChennel chennel;
		public SelectEquipment() {
			chennel = new QueueChennel();
			this.start();
		}
		public synchronized void setOrder(OrderInfo info)
		{
			chennel.append(order);
		}
		
		private synchronized void setOrderByIDLE(int equipmentType, OrderInfo order, int orderType)
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
						
						System.out.println("State:"+item.getState());
						selectedEquipment = item;
					}
				}
			}
			while(selectedEquipment==null);
			
			order.setMessageType(orderType);
			
			selectedEquipment.executeOrder(order);
		}
		@Override
		public void run() {
			
			
			while(true)
			{
				OrderInfo info =(OrderInfo) chennel.poll();
				
				this.setOrderByIDLE(Equipment.TYPE_AGV,info,OrderInfo.ORDER_AGV_INBOUND_WORK);
				
				
			}
			
		}
		public void start()
		{
			Thread thread = new Thread(this);
			thread.start();
		}
		
	}
	private void setOrderByIDLE(int equipmentType, OrderInfo order, int orderType)
	{
		new Thread()
		{
			
			public void run()
			{
				this.setName("set Order");
				Equipment selectedEquipment=null;
				
				do
				{
					Iterator<IFEquipment> iter = equipmentList.iterator();
					
					while(iter.hasNext())
					{
						Equipment item = (Equipment) iter.next();
						
						if(item.getEquipmentType()==equipmentType&&item.getState()==Equipment.STATE_IDLE)
						{				
							
							System.out.println("State:"+item.getState());
							selectedEquipment = item;
						}
					}
				}
				while(selectedEquipment==null);
				
				order.setMessageType(orderType);
				
				selectedEquipment.executeOrder(order);
			}
		}.start();
	}
	private void setOrderByWorkCount(int equipmentType, OrderInfo order, int orderType)
	{
		Iterator<IFEquipment> iter = equipmentList.iterator();
		
		Equipment selectedEquipment=(Equipment) iter.next();
		
		while(iter.hasNext())
		{
			Equipment item = (Equipment) iter.next();
			
			if(item.getEquipmentType()==equipmentType&&
					item.getWorkCount()<selectedEquipment.getWorkCount()
					&&item.getState()==Equipment.STATE_IDLE)
			{
				
				selectedEquipment = item;
			}
		}
		order.setMessageType(orderType);
		selectedEquipment.executeOrder(order);
	}	
	

	@Override
	public void run() {
		
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			
			switch (info.getMessageType()) {
			
			case OrderInfo.MESSATE_TYPE_CREATE:				
				
				
				this.setOrderByWorkCount(Equipment.TYPE_QC,info,OrderInfo.ORDER_QC_INBOUND_WORK);
				
				break;
				
			case OrderInfo.QC_INBOUND_WORK_END:
				
				System.out.println("qc work end");
				
				blockManager.selectBlockNumber();
				this.selectEquipment.setOrder(info);
				/*this.setOrderByIDLE(Equipment.TYPE_AGV,info,OrderInfo.ORDER_AGV_INBOUND_WORK);
				this.setOrderByIDLE(Equipment.TYPE_ATC,info,OrderInfo.ORDER_AGV_INBOUND_WORK);*/
				
				break;
			case OrderInfo.AGV_INBOUND_WORK_END:
				this.selectEquipment.setOrder(info);
				//this.setOrderByIDLE(Equipment.TYPE_ATC,info,OrderInfo.ORDER_ATC_INBOUND_WORK);
				
				break;
			case OrderInfo.ATC_INBOUND_WORK_END:
				
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
