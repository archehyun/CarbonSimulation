package carbon.equipment;

import carbon.equipment.command.OrderInfo;
import carbon.equipment.process.ProcessManager;

public interface IFEquipment {
	
	public void executeOrder(ProcessManager manager);
	public void executeOrder(OrderInfo info);
	
	public void equipmentStart();
	
	public void equipmentStop();
	

}
