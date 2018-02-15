package new_tech_dev.development.the_thing.executor_thing;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.sistems_acces.db_acces.ConnectionFactory;
import new_tech_dev.development.sistems_acces.dom_acces.DomReader;
import new_tech_dev.development.the_thing.method_thing.MethodInfo;
import new_tech_dev.development.the_thing.processor_thing.QueryProcessor;
import new_tech_dev.development.the_thing.return_thing.ReturnCaster;

public class DaoManager {
	
	private Map<String, MethodInfo> methods = new HashMap<>();
	
	public DaoManager(Class<?> clazz) {
		try {
			checkDaoXmlIntegrity(clazz);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Execute recupera el MethodInfo y lo pasa a un procesador que crea la Query final
	 */
	public Object execute(String name, Object[] args, Connection conect) {
		MethodInfo methodInfo = methods.get(name);
		String query = QueryProcessor.process(methodInfo,methodInfo.ArgKeyValueGenerator(args));
		ResultSet rs;
		try {
			rs = ConnectionFactory.execute(query);
			Object result = ReturnCaster.execute(rs, methodInfo);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			Class<?> classV =  (clazz.getGenericInterfaces()!=null)?(Class<?>)((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0]:null;
			Class<?> classK =  (clazz.getGenericInterfaces()!=null)?(Class<?>)((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[1]:null;
			dReader = new DomReader(clazz.getSimpleName());
			queryArgsNames = dReader.getParams();
			querys = dReader.getQuerys();
			Method[] methodsList = clazz.getMethods();
			for (int i = 0; i < methodsList.length; i++) {
				this.methods.put(methodsList[i].getName(), new MethodInfo(methodsList[i],
						querys.get(methodsList[i].getName()), queryArgsNames.get(methodsList[i].getName()),classV,classK));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
