package carbon.equipment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;
import carbon.view.XMLLoad;
import local.maps.LocalMap;

public class QC extends Equipment implements Runnable{
	
	
	private int trollySpeed=50;
	
	public int getTrollySpeed() {
		return trollySpeed;
	}
	public void setTrollySpeed(int trollySpeed) {
		this.trollySpeed = trollySpeed;
	}
	
	QCMoveingModule movingModule;
	
	QCTrolly trolly; // 트롤리
	
	public QC(String id) {
		super(id);

		
		this.equipmentType =Equipment.TYPE_QC;
		
		movingModule = new QCMoveingModule();	
		
		
		trolly = new QCTrolly(this.getID()+"-1");
		
		load =XMLLoad.getInstace();
		
		viewInfo = load.getEquipmentInfo("qc");
		
		width =  Integer.parseInt(viewInfo.getAttribute("width").getValue());
		
		height =  Integer.parseInt(viewInfo.getAttribute("height").getValue());
		
				
	}
	public void setLocation(int x, int y)
	{
		this.x=x;
		this.y=y;
		movingModule.setX(x);
		movingModule.setY(y);
		trolly.setLocation(x, y);
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
			
			System.out.println("qc order:"+info);
			
			switch (info.getMessageType()) {
			case OrderInfo.MESSATE_TYPE_AGV_QC_AVIVAL:
				this.trolly.executeOrder(info);
				
				break;
			case OrderInfo.ORDER_QC_INBOUND_WORK:
				
				OrderInfo qcOrder = new OrderInfo();				
				qcOrder.x = movingModule.x;
				qcOrder.y = movingModule.y+height-15;
				qcOrder.setMessageType(OrderInfo.AGV_INBOUND_WORK_CALL);
				info.setQC(this);
				sendMessage(qcOrder);
				//this.trolly.executeOrder(info);
				break;	

			default:
				break;
			}		
		
		}
	}

	@Override
	public void executeOrder(OrderInfo info) {
		
		info.setQcID(this.getID());
		this.chennel.append(info);
		
	}
	class QCMoveingModule extends MovingModuleAdapter implements Runnable
	{
		
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
		MovingModule trollyMovingModule;		

		public QCTrolly(String id) {
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
							Thread.sleep(getTrollySpeed());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}	
						
					}while(toY!=y);
					
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
				QC.this.updateWorkCount();
				QC.this.setState(STATE_BUSY);
				
				info.x = movingModule.x;
				
				info.y = movingModule.y+height-15;
				
				trollyMovingModule.moveTo(trollyMovingModule.x-2, 100);
				
				trollyMovingModule.moveTo(trollyMovingModule.x-2, 45);
				
			
				QC.this.setState(STATE_IDLE);				
		
				
				info.setMessageType(OrderInfo.QC_INBOUND_WORK_END);
				
				this.sendMessage(info);
				
				System.out.println("qc trolly process end: "+this.getID()+", "+info.x+", "+info.y);
				
			}
			
		}

		@Override
		public Point getLocation() {
			// TODO Auto-generated method stub
			return new Point(movingModule.x, movingModule.y);
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
			g.fillRect(trollyMovingModule.x-2,trollyMovingModule.y,  QC.this.width+5, QC.this.height/4);
			
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
	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return new Point(45, 45);
	}
	@Override
	public void update(LocalMap map) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(movingModule.x, movingModule.y, width, height);
		g.drawString(this.getWorkCount()+"번", movingModule.x, movingModule.y);
		g.setColor(Color.gray);
		g.drawRect(movingModule.x, movingModule.y+height-15, width, 15);
		
		
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

