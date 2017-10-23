package carbon.equipment.queue;

public class ProcessQueue extends QueueChennel{
	
	private static ProcessQueue instance;
	private ProcessQueue() {
		// TODO Auto-generated constructor stub
	}
	public static ProcessQueue getInstance()
	{
		
		if(instance == null)
		{
			instance = new ProcessQueue();
		}
		return instance;
	}

}
