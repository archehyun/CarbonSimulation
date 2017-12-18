package carbon.equipment.command;

import carbon.equipment.queue.QueueNode;

public class OrderInfo extends QueueNode{
	
	
	public int x;
	public int y;
	
	private int orderID;// 오더 아이디
	
	public int getOrderID() {
		return orderID;
	}
	public static final int MESSATE_TYPE_FROM_PROCESS=0;
	
	public static final int MESSATE_TYPE_CREATE=0;

	

	public static final int MESSATE_TYPE_AGV_AVIVAL = 3;

	public static final int MESSATE_TYPE_ATC = 4;
	
	public static final int MESSATE_TYPE_FROM_AGV = 2;
	
	
	public static final int QC_INBOUND_WORK_END = 1;
	
	public static final int ATC_INBOUND_WORK_END=3;
	
	public static final int AGV_INBOUND_WORK_END = 5;
	
	
	
	public static final int ORDER_QC_INBOUND_WORK = 1;

	public static final int ORDER_AGV_INBOUND_WORK = 0;

	public static final int ORDER_ATC_INBOUND_WORK = 0;
	
	
	public static final int QC_OUTBOUND_WORK_END = 1;
	
	public static final int ATC_OUTBOUND_WORK_END=3;
	
	public static final int AGV_OUTBOUND_WORK_END = 5;
	
	
	
	public static final int ORDER_QC_OUTBOUND_WORK = 1;

	public static final int ORDER_AGV_OUTBOUND_WORK = 0;

	public static final int ORDER_ATC_OUTBOUND_WORK = 0;
	
	
	
	public static final int COMMAND_AGV_GOTO=3;

	
	
	private String qcID;
	
	
	private int command;
	
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public String getQcID() {
		return qcID;
	}
	public void setQcID(String qcID) {
		this.qcID = qcID;
	}
	public String getAgvID() {
		return agvID;
	}
	public void setAgvID(String agvID) {
		this.agvID = agvID;
	}

	private String agvID;
	
	private int messageType; // 메시지 타입
	
	public int getMessageType() {
		return messageType;
	}
	public OrderInfo(int orderID) {
		this.orderID = orderID;
		
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String toString()
	{
		return "["+this.getOrderID()+","+this.qcID+","+this.getAgvID()+"]";
	}
	public Object getBlockID() {
		// TODO Auto-generated method stub
		return null;
	}

}
