package carbon.equipment.process;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import carbon.equipment.Equipment;
import carbon.equipment.IFEquipment;
import carbon.equipment.QC;
import carbon.equipment.command.Block;
import carbon.equipment.command.OrderInfo;
import carbon.equipment.queue.QueueChennel;

public class ProcessManager implements Runnable{
	
	protected boolean isReady;
	
	protected Thread thread;
	
	private static ProcessManager instance;
	
	private LinkedList<IFEquipment> equipmentList;
	
	private QueueChennel chennel;	
	
	BlockManager blockManager=BlockManager.getInstace();
	 
	private ProcessManager()
	{
		equipmentList = new LinkedList<IFEquipment>();
		
		chennel = new QueueChennel();
		
		selectProcess = new SelectProcess(equipmentList);
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
	
	SelectProcess selectProcess;

	
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
	
	Random rn = new Random();
	
	public void processQC(OrderInfo info)
	{
		switch (info.getMessageType()) {
		
		case OrderInfo.MESSATE_TYPE_CREATE:
			System.out.println("create order");
			
			this.setOrderByWorkCount(Equipment.TYPE_QC,info,OrderInfo.ORDER_QC_INBOUND_WORK);
			
			break;
		case OrderInfo.AGV_INBOUND_WORK_CALL:				
			Block block=blockManager.selectBlockNumber();
			
			info.bayIndex = rn.nextInt(20);
			info.rowIndex = rn.nextInt(4);
			info.setBlockLocation(block.getLocation().x,block.getLocation().y);
			info.setMessageType(OrderInfo.ORDER_AGV_INBOUND_WORK);
			this.selectProcess.setOrder(Equipment.TYPE_AGV, info);	
			
			break;	
		}
		
	}
	public void processAGV(OrderInfo info)
	{
		switch (info.getMessageType()) {
		
		case OrderInfo.MESSATE_TYPE_AGV_QC_AVIVAL:
			
			System.out.println("qc send");
			
			QC qc=info.getQC();
			
			qc.executeOrder(info);
				
			
			
			break;
		case OrderInfo.AGV_INBOUND_WORK_END:
			
			
			this.setOrderByWorkCount(Equipment.TYPE_ATC,info,OrderInfo.ORDER_QC_INBOUND_WORK);
			System.out.println("atc");
			
			break;	
		}
	}
	
	public void processATC(OrderInfo info)
	{
		
	}

	@Override
	public void run() {
		
		while(isReady)
		{
			try{
			OrderInfo info=(OrderInfo)chennel.poll();
			System.out.println(info);
			
			
			switch (info.getEquipmentType()) {
			case OrderInfo.EQUIPMENT_TYPE_QC:
				processQC(info);
				
				break;

			case OrderInfo.EQUIPMENT_TYPE_AGV:
				
				processAGV(info);
				
				break;
				
			case OrderInfo.EQUIPMENT_TYPE_ATC:
				processATC(info);				
				break;
				
			default:
				processQC(info);
				break;
			}
			
			
			/*switch (info.getMessageType()) {
			
			case OrderInfo.MESSATE_TYPE_CREATE:				
				
				this.setOrderByWorkCount(Equipment.TYPE_QC,info,OrderInfo.ORDER_QC_INBOUND_WORK);
				
				break;
			case OrderInfo.AGV_INBOUND_WORK_CALL:
				
				this.selectProcess.setOrder(Equipment.TYPE_AGV, info);	
				
				break;
			case OrderInfo.MESSATE_TYPE_AGV_AVIVAL:
				Block block=blockManager.selectBlockNumber();
				
				info.bayIndex = rn.nextInt(20);
				info.rowIndex = rn.nextInt(4);
				
				info.setBlockLocation(block.getLocation().x,block.getLocation().y);
				
				this.selectProcess.setOrder(Equipment.TYPE_AGV, info);	
				
				break;	
				
			case OrderInfo.QC_INBOUND_WORK_END:
				
				//System.out.println("qc work end");
				
				
				this.selectProcess.setOrder(Equipment.TYPE_AGV, info);	
				
				
				break;
			case OrderInfo.AGV_INBOUND_WORK_END:
				System.out.println("set atc Work: "+info);
				this.setOrderByWorkCount(Equipment.TYPE_ATC,info,OrderInfo.ORDER_QC_INBOUND_WORK);
				//this.selectProcess.setOrder(Equipment.TYPE_ATC, info);				
				
				break;
			case OrderInfo.ATC_INBOUND_WORK_END:
				
				//System.out.println("end: "+info);
				
				break;		
			default:
				break;
			}*/
			}
			catch(Exception e)
			{
				System.err.println(e.getMessage());
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
