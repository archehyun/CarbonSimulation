package carbon.equipment.process;

import java.util.Iterator;
import java.util.LinkedList;

import carbon.equipment.Equipment;
import carbon.equipment.IFEquipment;
import carbon.equipment.command.OrderInfo;
import carbon.equipment.queue.QueueChennel;

public class SelectProcess implements Runnable{
	
	private LinkedList<IFEquipment> equipmentList;
	
	OrderInfo order=null;
	private QueueChennel chennel;
	public SelectProcess(LinkedList<IFEquipment> equipmentList) {
		chennel = new QueueChennel();
		this.equipmentList =equipmentList;
		this.start();		
	}
	public synchronized void setOrder(int orderType,OrderInfo info)
	{
		
		SelectOrder selectOrder = new SelectOrder(0);
		selectOrder.orderType =orderType;
		selectOrder.info = info;
		
		chennel.append(selectOrder);
	}
	
	public void setOrderByWorkCount(int equipmentType, OrderInfo order, int orderType)
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
					selectedEquipment = item;
				}
			}
		}
		while(selectedEquipment==null);
		
		System.out.println(selectedEquipment+","+selectedEquipment.getLocation());

		order.setMessageType(orderType);
		
		selectedEquipment.executeOrder(order);
	}
	@Override
	public void run() {
		
		
		while(true)
		{
			SelectOrder info =(SelectOrder) chennel.poll();
			
			this.setOrderByIDLE(info.orderType,info.info,OrderInfo.ORDER_AGV_INBOUND_WORK);			
			
		}		
	}
	class SelectOrder extends OrderInfo
	{
		
		public SelectOrder(int orderID) {
			super(orderID);
			// TODO Auto-generated constructor stub
		}
		public int orderType;
		public OrderInfo info;
		
	}
	public void start()
	{
		Thread thread = new Thread(this);
		thread.start();
	}
	

}
