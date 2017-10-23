package carbon.equipment.command;

import carbon.equipment.queue.QueueNode;

public class OrderInfo extends QueueNode{
	
	
	private int orderID;
	
	public int getOrderID() {
		return orderID;
	}
	public static final int MESSATE_TYPE_CREATE=0;

	public static final int MESSATE_TYPE_QC = 1;

	public static final int MESSATE_TYPE_AGV = 3;
	
	
	
	private String qcID;
	
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
	
	private int messageType;
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

}
