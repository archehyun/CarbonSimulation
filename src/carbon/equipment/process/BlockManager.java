package carbon.equipment.process;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import carbon.equipment.ATC;
import carbon.equipment.command.Block;

public class BlockManager {
	
	public static int BAY=20;
	public static int ROW=4;
	public static int TIER=4;
	public static int gap=1;
	public static int conW=2;//컨테이너 width;
	public static int conH=5;//컨테이너 height;
	
	private static BlockManager instance;
	List blockList;
	
	public static BlockManager getInstace() {
		
		if(instance == null)
			instance = new BlockManager();
		
		return instance;
	}
	
	
	private BlockManager() {
		blockList = new LinkedList<Block>();
	}
	public List getBlockList() {
		return blockList;
	}

	int blockStartPointX;
	int blockStartPointY;
	public void init()
	{
		int blockCount=0;
		for(int i=0;i<1;i++)
		{
			for(int j=0;j<1;j++)
			{
				Block block = new Block(blockCount++, 20, 4, 4);
				block.setLocation(30+j*100, 250+i*100);
				blockList.add(block);
				
				ATC atc = new ATC("atc"+blockCount);
				atc.setLocation(block.getLocation().x, block.getLocation().y);
				System.out.println("add block");
			}
		}
	}	
	
	Random rn = new Random();
	public Block selectBlockNumber()
	{
		int size = blockList.size();
		return (Block) blockList.get(rn.nextInt(size));
	}

}
