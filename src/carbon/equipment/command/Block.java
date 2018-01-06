package carbon.equipment.command;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import carbon.equipment.process.BlockManager;
import local.maps.LocalMap;
import local.maps.model.IFLocation;

public class Block implements IFLocation{
	
	int bay;
	int row;
	int tier;
	int blockNumber;
	int x,y;
	int blockInfo[][][];
	Point location;
	int gap=1;
	int conW=2;//컨테이너 width;
	int conH=5;//컨테이너 height;
	public Block(int blockNumber, int bay, int row, int tier) {
		this.blockNumber = blockNumber;
		this.bay =bay;
		this.row=row;
		this.tier = tier;
		location = new Point();
		blockInfo = new int[bay][row][tier];
	}
	public Point getLocation()
	{
		return location;
	}
	public void setLocation(int x, int y)
	{
		location.x=x;
		location.y=y;
		
		//atc.setLocation(x, y);
	}
	@Override
	public void update(LocalMap map) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		
		for(int bayCount=0;bayCount<bay;bayCount++)
		{
			for(int rowCount=0;rowCount<row;rowCount++)
			{
				g.fillRect(location.x+(conW+gap)*bayCount, location.y+(conH+1)*rowCount, BlockManager.conW, BlockManager.conH);
			}			
		}
		//g.fillRect(location.x, location.y, 50, 25);
		g.setColor(Color.white);
		g.drawString(blockNumber+"번", location.x+5, location.y+10);
		
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
