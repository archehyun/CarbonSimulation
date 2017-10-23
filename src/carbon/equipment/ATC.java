package carbon.equipment;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;

public class ATC extends Equipment{

	public ATC(String id) {
		super(id);
		this.equipmentType =Equipment.TYPE_ATC;
		System.out.println("create atc");
		
	}

	@Override
	public void executeOrder(ProcessManager manager) {
		
		
	}

	@Override
	public void executeOrder(OrderInfo info) {
		chennel.append(info);
		
	}

	@Override
	public void run() {
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			System.out.println("ATC process : "+this.getID());
			
			
			this.setState(STATE_BUSY);
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			this.setState(STATE_IDLE);
			
			info.setMessageType(OrderInfo.MESSATE_TYPE_ATC);
			
			this.sendMessage(info);
		}
		
	}

}
