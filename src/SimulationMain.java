import carbon.equipment.AGV;
import carbon.equipment.QC;
import carbon.equipment.process.OrderGeneration;
import carbon.equipment.process.ProcessManager;

public class SimulationMain {
	
	public static void main(String[] args) {
		
		
		QC qc1 = new QC("qc1");
		QC qc2 = new QC("qc2");
		QC qc3 = new QC("qc3");
		QC qc4 = new QC("qc4");
		
		AGV agv1 = new AGV("agv1");
		AGV agv2 = new AGV("agv2");		
		AGV agv3 = new AGV("agv3");
		
		ProcessManager manager = ProcessManager.getInstace();
		manager.start();
		manager.equipmentStart();
		
		OrderGeneration generation = new OrderGeneration();
		
		generation.start();
		
	}

}
