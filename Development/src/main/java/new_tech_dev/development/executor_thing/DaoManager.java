package new_tech_dev.development.executor_thing;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.dom_acces.DomReader;
import new_tech_dev.development.method_ting.MethodInfo;
import new_tech_dev.development.query_processors_thing.QueryProcessor;

public class DaoManager {
	
	private Map<String, MethodInfo> methods = new HashMap<>();
	private Class<?> classV;
	private Class<?> classK;
	
	
	public DaoManager(Class<?> clazz) {
		
		try {
			checkDaoXmlIntegrity(clazz);
			this.classV = (Class<?>) ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
			this.classK = (Class<?>) ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[1];
			
		} catch ( Exception e ) {
			// TODO Crear un tipo de exception especifica
			e.printStackTrace();
		}
	}
	
	/*
	 * Execute recupera el MethodInfo y lo pasa a un procesador que crea la Query final
	 */
	public Object execute(Method method, Object[] args, Connection conect) {
		MethodInfo methodInfo = methods.get(method.getName());
		String query = QueryProcessor.process(methodInfo,methodInfo.ArgKeyValueGenerator(args));
		ResultSet rs = 
		return null;
	}
	
	/*
	 * Recupera las consultas y los argumentos que se les pasan
	 * Se crean los objetos MethodInfo
	 */
	private void checkDaoXmlIntegrity(Class<?> clazz) throws Exception {
		Map<String, String[]> queryArgsNames;
		Map<String, String> querys;
		DomReader dReader;

		try {

			dReader = new DomReader(clazz.getSimpleName());
			queryArgsNames = dReader.getParams();
			querys = dReader.getQuerys();
			Method[] methodsList = clazz.getDeclaredMethods();
			for (int i = 0; i < methodsList.length; i++) {
				this.methods.put(methodsList[i].getName(), new MethodInfo(methodsList[i],
						querys.get(methodsList[i].getName()), queryArgsNames.get(methodsList[i].getName())));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
}
