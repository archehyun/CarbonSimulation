package carbon.equipment.queue;

public class QueueNode {
	private QueueNode prev; //doubly linked list
	private QueueNode next; //doubly linked list
	
	public QueueNode() {
		prev = null;
		next = null;	
	}
	
	public  QueueNode getPrev()
	{
		return prev;
	}

	public void setPrev( QueueNode prev)
	{
		this.prev = prev;
	}

	public  QueueNode getNext()
	{
		return next;
	}

	public void setNext( QueueNode next)
	{
		this.next = next;
	}

}
