package carbon.equipment;

import java.awt.Point;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;

public interface IFEquipment{
	
	public void executeOrder(ProcessManager manager);
	public void executeOrder(OrderInfo info);
	
	public void equipmentStart();
	
	public void equipmentStop();
	
	public Point getLocation();
	public void setLocation(int x, int y);
	
	
	

}
