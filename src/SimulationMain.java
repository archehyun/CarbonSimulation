import carbon.equipment.AGV;
import carbon.equipment.ATC;
import carbon.equipment.QC;
import carbon.view.MainFrame;

public class SimulationMain {
	
	public static void main(String[] args) {		
		
		QC qc1 = new QC("qc1");
		qc1.setLocation(30, 45);
		QC qc2 = new QC("qc2");
		qc2.setLocation(75, 45);
		QC qc3 = new QC("qc3");
		qc3.setLocation(115, 45);
		QC qc4 = new QC("qc4");
		qc4.setLocation(150, 45);
		
		AGV agv1 = new AGV("agv1");
		AGV agv2 = new AGV("agv2");		
		AGV agv3 = new AGV("agv3");		
		
		ATC atc1 = new ATC("atc1");
		atc1.setLocation(30, 250);
		ATC atc2 = new ATC("atc2");
		atc2.setLocation(150, 250);
		ATC atc3 = new ATC("atc3");
		atc3.setLocation(30, 350);
		ATC atc4 = new ATC("atc4");
		atc4.setLocation(150, 350);
		

		
		MainFrame frame = new MainFrame();
		frame.createAndUpdateUI();
		
	}

}
