package carbon.equipment;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;

public class QC extends Equipment implements Runnable{
	
	QCTrolly trolly;
	
	public QC(String id) {
		super(id);
		this.equipmentType =Equipment.TYPE_QC;
		
		System.out.println("QC »ý¼º:"+this.getID());
		
		trolly = new QCTrolly(this.getID()+"-1");
				
	}

	@Override
	public void executeOrder(ProcessManager manager) {

		OrderInfo qcOrder=manager.getOrder();

		this.chennel.append(qcOrder);

	}
	

	@Override
	public void run() {
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			System.out.println("qc process : "+this.getID());
			
			trolly.executeOrder(info);
		
		}
	}

	@Override
	public void executeOrder(OrderInfo info) {
		
		info.setQcID(this.getID());
		this.chennel.append(info);
		
	}
	class QCMoveingModule extends MovingModuleAdapter implements Runnable
	{
		Thread thread;
		
		@Override
		public void moveRight() {
			x++;
			
		}
		
		@Override
		public void moveLeft() {
			y--;			
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
		public void execute()
		{
			
		}
	}
	class QCTrolly extends Equipment
	{
		MovingModule movingModule;
		

		public QCTrolly(String id) {
			super(id);
			movingModule = new MovingModule() {
				
				@Override
				public void moveUp() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void moveTo(int toX, int toY) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void moveRight() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void moveLeft() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void moveDown() {
					// TODO Auto-generated method stub
					
				}
			};
		}

		@Override
		public void executeOrder(ProcessManager manager) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void executeOrder(OrderInfo info) {
			this.chennel.append(info);
			
		}

		@Override
		public void run() {
			while(isReady)
			{	
				OrderInfo info=(OrderInfo)chennel.poll();
				
				System.out.println("trolly process : "+this.getID());
				
				this.setState(STATE_BUSY);
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
				this.setState(STATE_IDLE);
				
				info.setMessageType(OrderInfo.MESSATE_TYPE_FROM_QC);
				
				QC.this.updateWorkCount();
				
				this.sendMessage(info);
			}
			
		}
		
	}

	


}
