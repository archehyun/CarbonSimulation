package carbon.equipment;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;

public class AGV extends Equipment {
	
	public AGV(String id) {
		super(id);
		
		this.equipmentType =Equipment.TYPE_AGV;
		
		this.setState(STATE_IDLE);
	}
	
	AGVMovingModule module = new AGVMovingModule();
	
	class AGVMovingModule extends MovingModule implements Runnable
	{		
		protected boolean isReady=false; // 스레드 시작
		
		protected Thread thread;

		@Override
		public void moveUp() {
			--y;
		}

		@Override
		public void moveDown() {
			++y;
			
		}

		@Override
		public void moveLeft() {
			--x;
			
		}

		@Override
		public void moveRight() {
			++x;
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
			while(destinationX==x&&isReady);
			
			OrderInfo info = new OrderInfo(OrderInfo.MESSATE_TYPE_AGV_AVIVAL);
			
			AGV.this.executeOrder(info);
			
		}
		public void execute()
		{
			if(thread==null)
			{
				isReady=true;				
				thread = new Thread(this);
				thread.start();
			}
		}
		
		public void stop()
		{
			isReady = false;
			
			thread = null;
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
			
			switch (info.getMessageType()) {
			
			case OrderInfo.MESSATE_TYPE_FROM_PROCESS:
				
				
				this.setState(STATE_BUSY);
				
				this.setDestination(info.getBlockID());
				
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
				this.setState(STATE_IDLE);
				
				
				break;
			case OrderInfo.MESSATE_TYPE_AGV_AVIVAL:
				
				info.setMessageType(OrderInfo.MESSATE_TYPE_FROM_AGV);
				
				this.sendMessage(info);
				break;	

			default:
				break;
			}
			
			
			
		}		
	}



	private void setDestination(Object blockID) {
		
		module.setDestination(0,0);
		module.execute();
		
	}


	@Override
	public void executeOrder(OrderInfo info) {
		
		info.setAgvID(this.getID());
		
		this.chennel.append(info);
		
	}

}
