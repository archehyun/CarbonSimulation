package carbon.equipment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.jdom.Element;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;
import carbon.view.XMLLoad;
import local.maps.LocalMap;

public class ATC extends Equipment{
	
	private Element viewInfo;
	int w;
	int h;
	ATCTrolly trolly;
	
	ATCMoveingModule movingModule;
	public ATC(String id) {
		super(id);
		this.equipmentType =Equipment.TYPE_ATC;
		
		load =XMLLoad.getInstace();
		
		movingModule = new ATCMoveingModule();
		
		viewInfo = load.getEquipmentInfo("atc");
		
		w =  Integer.parseInt(viewInfo.getAttribute("width").getValue());
		
		h =  Integer.parseInt(viewInfo.getAttribute("height").getValue());
		
		trolly = new ATCTrolly(getID()+"-1");
		
	}
	
	class ATCMoveingModule extends MovingModuleAdapter implements Runnable
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

	@Override
	public void executeOrder(ProcessManager manager) {
		
		
	}

	@Override
	public void executeOrder(OrderInfo info) {
		chennel.append(info);
		
	}
	
	class ATCTrolly extends Equipment
	{
		MovingModule trollyMovingModule;		

		public ATCTrolly(String id) {
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
						
					}while(toX!=x);
					
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
				
				ATC.this.updateWorkCount();
				
				ATC.this.setState(STATE_BUSY);
				
				trollyMovingModule.moveTo(ATC.this.x+100, 100);
				
				trollyMovingModule.moveTo(ATC.this.x, 45);
						
				ATC.this.setState(STATE_IDLE);
				
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
			g.fillRect(trollyMovingModule.x-2,trollyMovingModule.y-2,  15, h+4);
			
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
	public void run() {
		while(isReady)
		{	
			OrderInfo info=(OrderInfo)chennel.poll();
			
			trolly.executeOrder(info);
		}
		
	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(int x, int y) {
		this.x=x;
		this.y=y;
		movingModule.setX(x);
		movingModule.setY(y);
		trolly.setLocation(x, y);
		
	}

	@Override
	public void update(LocalMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		
		
		g.setColor(Color.YELLOW);		

		g.fillRect(movingModule.x, movingModule.y, w, h);	
		
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
