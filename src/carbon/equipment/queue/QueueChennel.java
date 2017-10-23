package carbon.equipment.queue;




public class QueueChennel {
	private QueueNode message=null;
	protected QueueNode first; //pointer to the first node of Linked List
	protected QueueNode last; //pointer to the last node of Linked List

	public synchronized QueueNode poll()
	{
		while((message=poll1())==null)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return message;
	}
	private synchronized QueueNode poll1()
	{
		QueueNode retMsg = null;
		
		if (first == null)
		{
			retMsg = null;
		}
		else if (first == last)
		{
			retMsg = first;
			first = null;
			last = null;
		}
		else
		{
			retMsg = first; 
			first = first.getNext();;
			first.setPrev(null);
		}
		
		return retMsg;
	}
	
	public synchronized boolean append(QueueNode newMsg)
	{
		if (first == null)
		{
			first = newMsg;
			last = newMsg;
		}
		else
		{
			newMsg.setPrev(last);
			last.setNext(newMsg);
			last = newMsg;
		}
		notifyAll();
		
		return true;
	}
}
