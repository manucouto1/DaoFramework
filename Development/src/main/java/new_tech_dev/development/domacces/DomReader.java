package new_tech_dev.development.domacces;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class DomReader {
	
	private File xmlFile;
	private Map<String,String> querys = new HashMap<>();
	
	public DomReader(String url) {
		this.xmlFile = new File(url+"Dao");
//		this.parts = url.split("Dao");
	}
	
	public Map<String, String> getQuerys() {
		int i = 0;
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			// Accedemos al documento y nos colocamos en el nodo padre
			document = (Document) builder.build("src/main/resources/"+xmlFile+".xml");
			Element rootNode = document.getRootElement();
			// Listamos los hijos
			List<?> list = rootNode.getChildren();
			// Buscamos la accion
			while( i<list.size()){
				Element e = (Element)list.get(i);
				if(null !=e.getAttribute("name")){
					querys.put(e.getAttribute("name").getValue(),e.getChildText("script"));
				}
				i++;
			}
			return querys;
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return querys;
	}
	
	
//	private HashMap<String,String> listAttributes(Element e) {
//		
//		Map<String,String> attributes = new HashMap<>();
//		List<Attribute> list = e.getAttributes();
//		
//		for(int i=0; i<list.size(); i++){
//			attributes.put(list.get(i).getName(),list.get(i).getValue());
//		}
//		return (HashMap<String, String>) attributes;
//	}
	
	


}
