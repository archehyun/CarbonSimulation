package carbon.view;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class XMLLoad {
	
	private static XMLLoad instance;
	public static XMLLoad getInstace()
	{
		if(instance ==null)
			instance = new XMLLoad();
		return instance;
	}
	
	
	private XMLLoad()
	{
		this.fileName ="layout/layout.xml";
		load(fileName);
	}
	private String fileName;
	private Element root;
	
	public Element getEquipmentInfo(String equipment)
	{
		return root.getChild(equipment);
	}
	public void load(String fileName)
	{
		//this.removeViewNodeAll();
		
		this.fileName=fileName;
		
		SAXBuilder builder =new SAXBuilder();

		File file = new File(this.fileName);

		try {
			Document doc = builder.build(file);
			root = doc.getRootElement();
			/*Element agv=root.getChild("agv");
			Element qc=root.getChild("qc");
			Element atc=root.getChild("qc");
			
			System.out.println(agv);*/
			
			//List li=doc.getContent();
			
			
			//create(doc);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//updateView();
		
	}
	public int getIntAttribute(Element element, String name)
	{
		
		try{
			return Integer.parseInt(element.getAttribute(name).getValue());
		}catch(Exception e)
		{
			return 0;
		}
		
	}
/*	private void create(Document doc)
	{	
		List children = doc.getContent();
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof Element) {
				create((Element) o);
			}		
		}
	}*/
/*	private void create(Element element,ViewNode parentNode)
	{
		List attributes = element.getAttributes();

		List children = element.getContent();

		Iterator iterator = children.iterator();

		String name = element.getName();

		//ViewNode childNode = this.createViewNode(element, name);

	//	parentNode.add(childNode);

		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof Element) {
				create((Element) o,childNode);
			}

			else if (o instanceof String) {
				System.out.println("String: " + o);
			}   
		}

		Iterator attributeIterator = attributes.iterator();
		while (attributeIterator.hasNext()) {
			Attribute o = (Attribute) attributeIterator.next();	

		}
	}*/
	public static void main(String[] args) {
		XMLLoad load = new XMLLoad();
		load.load("layout/layout.xml");
	}


	public void reload() {
		load(fileName);
		
	}

}
