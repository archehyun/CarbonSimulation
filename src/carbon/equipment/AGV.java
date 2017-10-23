package carbon.equipment;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;

public class AGV extends Equipment {
	
	public AGV(String id) {
		super(id);
		
		this.equipmentType =Equipment.TYPE_AGV;
		
		this.setState(STATE_IDLE);
	}

	class AGVMovingModule extends MovingModule implements Runnable
	{
		
		protected boolean isReady; // 스레드 시작
		
		protected Thread thread;

		@Override
		public void moveUp() {
			
		}

		@Override
		public void moveDown() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void moveLeft() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void moveRight() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void moveTo(int toX, int toY) {
			
			
		}

		@Override
		public void run() {
			do
			{
				if(destinationX>x)
				{
					moveRight();
				}
				else if(destinationX<x)
				{
					moveLeft();
				}
			}
			while(destinationX==x);
			
		}
		
	}

	@Override
	public void executeOrder(ProcessManager manager) {
		this.chennel.append(manager.getOrder());
		
	}



	public void run() {
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			System.out.println("AGV process : "+this.getID());
			
			
			this.setState(STATE_BUSY);
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			this.setState(STATE_IDLE);
			
			info.setMessageType(OrderInfo.MESSATE_TYPE_AGV);
			
			this.sendMessage(info);
		}
		
	}



	@Override
	public void executeOrder(OrderInfo info) {
		
		info.setAgvID(this.getID());
		this.chennel.append(info);
		
	}

}
