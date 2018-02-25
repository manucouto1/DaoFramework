package new_tech_dev.development.sistems_acces.dom_acces;

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
	
	private Map<String,String[]> args = new HashMap<>();
	
	private Map<String,Boolean> generatedKeys = new HashMap<>();
	
	private Document document;
	
	
	public DomReader(String url) {
		
		int i = 0;
		this.xmlFile = new File(url);
		SAXBuilder builder = new SAXBuilder();
		
		try {
			document = (Document) builder.build("src/main/resources/"+xmlFile+".xml");
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			// Accedemos al documento y nos colocamos en el nodo padre
			Element rootNode = document.getRootElement();
			// Listamos los hijos
			List<?> list = rootNode.getChildren();
			// Buscamos la accion
			while( i<list.size()){
				Element e = (Element)list.get(i);
				if(null !=e.getAttribute("name")){
					querys.put(e.getAttribute("name").getValue(),e.getChildText("script"));
					if(null!=e.getAttribute("var")){
						args.put(e.getAttribute("name").getValue(),e.getAttribute("var").getValue().split(","));
					}
					if(null!=e.getAttribute("generatedKeys")){
						generatedKeys.put(e.getAttribute("name").getValue(),Boolean.valueOf(e.getAttribute("generatedKeys").getValue()));
					} else {
						generatedKeys.put(e.getAttribute("name").getValue(),false);
					}
				} 
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getQuerys() {
		return querys;
	}
	
	public Map<String,String[]> getParams(){
		return args;
	}
	
	public Boolean hasGeneratedKeys(String name){
		
		return (generatedKeys.get(name)!=null)?generatedKeys.get(name):false;
	}
	public Map<String,Boolean> generatedKeys(){
		return generatedKeys;
	}
	

}
