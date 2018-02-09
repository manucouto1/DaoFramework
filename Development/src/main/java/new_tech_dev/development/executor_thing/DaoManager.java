package new_tech_dev.development.executor_thing;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.dom_acces.DomReader;
import new_tech_dev.development.query_processors_thing.QueryProcessor;

public class DaoManager {
	
	private Map<String, String> querys = new HashMap<>();
	private Map<String, String[]> argNames = new HashMap<>();
	private Map<String, Method> methods = new HashMap<>();
	private Class<?> classV;
	private Class<?> classK;
	
	
	public DaoManager(Class<?> clazz) {
		
		try {
			testDaoXmlIntegrity(clazz);
			this.classV = (Class<?>) ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
			this.classK = (Class<?>) ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[1];
			
		} catch ( Exception e ) {
			// TODO Crear un tipo de exception especifica
			e.printStackTrace();
		}
	}
	
	/*
	 * TODO debe saber que tipo de executor
	 */
	public Object execute(Method method, Object[] args) {
		
//		String finalQuery = QueryProcessor.proccess();
		
		return null;
	}
	/*
	 * Compara los metodos de la interfazDao con los del XMLDao
	 * Comprar los argumentos de cada metodo para que tengan el mismo nombre
	 * Si todo esta bien inicializa los atributos que usaran los executors
	 * 
	 */
	private void testDaoXmlIntegrity(Class<?> clazz) throws Exception{
		try{
			DomReader dReader = new DomReader(clazz.getSimpleName());
			this.argNames = dReader.getArgs();
			this.querys = dReader.getQuerys();
			Method[] methodsList = clazz.getDeclaredMethods();
			for(int i=0; i<methodsList.length; i++){
				if(querys.get(methodsList[i].getName())!=null){
					if(argNames.get(methodsList[i].getName()).equals(getStringParameterNames(methodsList[i].getParameterTypes()))){
						methods.put(methodsList[i].getName(), methodsList[i]);
					} else {
						throw new Exception(" TODO Exception Message");
					}
				} else {
					throw new Exception(" TODO Exception Message");
				}
			} 
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Pasa el array Type[] al array String[] con los nombres de los tipos
	 */
	private String[] getStringParameterNames(Type[] parameterTypes) {
		String[] result = new String[parameterTypes.length];
		for(int i=0; i<parameterTypes.length; i++){
			result[i] = parameterTypes[i].getTypeName();
		}
		return result;
	}
	
}
