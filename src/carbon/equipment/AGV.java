package carbon.equipment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;
import local.maps.LocalMap;

public class AGV extends Equipment {
	
	public AGV(String id) {
		super(id);		
		this.equipmentType =Equipment.TYPE_AGV;
		trolly = new AGVTrolly(this.getID()+"-1");
		this.setState(STATE_IDLE);
	}
	
	AGVTrolly trolly;
	
	AGVMovingModule module = new AGVMovingModule();
	
	class AGVMovingModule extends MovingModule implements Runnable
	{		
		protected boolean isReady=false; // 스레드 시작
		

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
		OrderInfo info;
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
			
			info = new OrderInfo(OrderInfo.MESSATE_TYPE_AGV_AVIVAL);
			
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
			
			case OrderInfo.ORDER_AGV_INBOUND_WORK:
				
				trolly.executeOrder(info);
			/*	this.setState(STATE_BUSY);
				
				this.setDestination(info.getBlockID());
				
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
				this.setState(STATE_IDLE);
				
				*/
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
	class AGVTrolly extends Equipment
	{
		public MovingModule trollyMovingModule;		

		public AGVTrolly(String id) {
			super(id);
			trollyMovingModule = new MovingModule() {
				
				@Override
				public void moveUp() {
					y--;
					
				}
				
				@Override
				public void moveTo(int toX, int toY) {
					do
					{
						if(toY>y)
							moveDown();
						else							
						{
							moveUp();
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}	
						
					}while(toY!=y);
					
				}
				
				@Override
				public void moveRight() {
					x++;
					
				}
				
				@Override
				public void moveLeft() {
					x--;
					
				}
				
				@Override
				public void moveDown() {
					y++;
					
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
				AGV.this.updateWorkCount();
				AGV.this.setState(STATE_BUSY);
				
				trollyMovingModule.moveTo(trollyMovingModule.x-2, 100);
				
				trollyMovingModule.moveTo(trollyMovingModule.x-2, 45);
						
				AGV.this.setState(STATE_IDLE);
				
				info.setMessageType(OrderInfo.QC_INBOUND_WORK_END);
				
				
				
				this.sendMessage(info);
				
				System.out.println("trolly process end: "+this.getID());
				
			}
			
		}

		@Override
		public Point getLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLocation(int x, int y) {
			trollyMovingModule.setX(x);
			trollyMovingModule.setY(y);
			
		}

		@Override
		public void update(LocalMap map) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void draw(Graphics g) {
			g.setColor(Color.red);
			//g.fillRect(trollyMovingModule.x-2,trollyMovingModule.y,  width+5, height/4);
			
		}

		@Override
		public void setLabelView(boolean isLabelView) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isEnter(Point mousePoint) {
			// TODO Auto-generated method stub
			return false;
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


	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(LocalMap map) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see local.maps.model.IFLocation#draw(java.awt.Graphics)
	 * @설명 agv 형상 정의
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(trolly.trollyMovingModule.x, trolly.trollyMovingModule.y, 50, 10);
		
	}


	@Override
	public void setLabelView(boolean isLabelView) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isEnter(Point mousePoint) {
		// TODO Auto-generated method stub
		return false;
	}

}
