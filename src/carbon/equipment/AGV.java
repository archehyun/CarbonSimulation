package carbon.equipment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.BlockManager;
import carbon.equipment.process.ProcessManager;
import carbon.equipment.queue.QueueNode;
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
	OrderInfo agvOrder;
	@Override
	public void executeOrder(ProcessManager manager) {
		this.chennel.append(manager.getOrder());		
	}
	InteralMessage message;
	public void run() {
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			System.out.println("AGV process : "+this.getID()+", "+info.getMessageType() );
			
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
			case OrderInfo.AGV_INBOUND_WORK_END:
				
				
				System.out.println("agv send");
				this.sendMessage(info);
				
				
				break;	
			case OrderInfo.MESSATE_TYPE_AGV_QC_AVIVAL:
				
				agvOrder = new OrderInfo();
				agvOrder.x = info.blockX+info.bayIndex*BlockManager.conW;
				agvOrder.y = info.blockY;
				agvOrder.setEquipmentType(OrderInfo.EQUIPMENT_TYPE_AGV);				
//				info.setMessageType(OrderInfo.AGV_INBOUND_WORK_END);
				
				System.out.println("arrrvla==="+info);
				//trolly.executeOrder(info);
				AGV.this.sendMessage(agvOrder);
				
				break;
			default:
				System.out.println("order error"+info.getMessageType());
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
						if(toX>x)
							moveRight();
						else							
						{
							moveLeft();
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
				
				trollyMovingModule.moveTo(info.x, info.y);
				
				if(info.getEquipmentType()==OrderInfo.EQUIPMENT_TYPE_QC)
				{
					info.setMessageType(OrderInfo.MESSATE_TYPE_AGV_QC_AVIVAL);
				}
				
				AGV.this.executeOrder(info);
				
/*				info.info.setEquipmentType(OrderInfo.EQUIPMENT_TYPE_AGV);
				
				info.info.setMessageType(OrderInfo.MESSATE_TYPE_AGV_AVIVAL);
				
				AGV.this.sendMessage(info);
				
				System.out.println("send===");
				
				trollyMovingModule.moveTo(info.info.blockX+info.info.bayIndex*BlockManager.conW, info.info.blockY);
						
				AGV.this.setState(STATE_IDLE);				
				
				
				AGV.this.executeOrder(info);
				
				System.out.println("agv process end: "+this.getID());*/
				
			}
			
		}

		@Override
		public Point getLocation() {
			// TODO Auto-generated method stub
			return new Point(module.x, module.y);
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
		return new Point(module.getX(), module.getY());
	}


	@Override
	public void setLocation(int x, int y) {
		
		
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
		
		if(this.getState()==Equipment.STATE_BUSY)
		{
		g.setColor(Color.blue);
		}
		else
		{
			g.setColor(Color.green);
		}
		g.fillRect(trolly.trollyMovingModule.x, trolly.trollyMovingModule.y, 25, 10);
		g.setColor(Color.white);
		g.drawRect(trolly.trollyMovingModule.x, trolly.trollyMovingModule.y, 25, 10);
		
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
	class InteralMessage extends QueueNode
	{
		public int x;
		public int y;
		public OrderInfo info;
	}

}
