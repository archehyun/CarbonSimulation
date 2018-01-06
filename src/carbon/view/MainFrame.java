package carbon.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import carbon.equipment.process.BlockManager;
import carbon.equipment.process.OrderGeneration;
import carbon.equipment.process.ProcessManager;
import local.maps.MapConfig;
import local.maps.model.IFLocation;

public class MainFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	SimulationMap map = new SimulationMap(400, 300);

	private BlockManager blockManager;

	public void createAndUpdateUI()
	{		
		MapConfig config = new MapConfig(map);
		
		blockManager = BlockManager.getInstace();
		blockManager.init();
		List li2 = blockManager.getBlockList();
		Iterator<IFLocation> iter2 = li2.iterator();
		while(iter2.hasNext())
		{
			map.addLocation(iter2.next());
		}
		
		List li=ProcessManager.getInstace().getEquipmentList();
		
		Iterator<IFLocation> iter = li.iterator();
		
		while(iter.hasNext())
		{
			map.addLocation(iter.next());
		}	
		
		JPanel pnControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton butStart = new JButton("Start");
		butStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ProcessManager manager = ProcessManager.getInstace();
				manager.start();
				manager.equipmentStart();				
				OrderGeneration generation = new OrderGeneration();				
				generation.start();				
			}
		});
		
		JButton butLoad = new JButton("Load");
		butLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				XMLLoad load =XMLLoad.getInstace();
				load.reload();
				/*ProcessManager manager = ProcessManager.getInstace();
				manager.start();
				manager.equipmentStart();				
				OrderGeneration generation = new OrderGeneration();				
				generation.start();	*/			
			}
		});
		pnControl.add(butStart);
		pnControl.add(butLoad);
		
		
		this.getContentPane().add(map);
		this.getContentPane().add(pnControl, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(250,500);
		this.setVisible(true);
	}

}
