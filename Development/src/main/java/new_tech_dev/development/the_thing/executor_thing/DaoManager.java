package new_tech_dev.development.the_thing.executor_thing;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.sistems_acces.dom_acces.DomReader;
import new_tech_dev.development.the_thing.method_thing.DaoMethod;
import new_tech_dev.development.the_thing.return_thing.ReturnFactory;

public class DaoManager {

	private Map<String, DaoMethod> daoMethods = new HashMap<>();

	/*
	 * Recupera informacion de los metodos del dao y del daoXml
	 * Crea un mapa de DaoMetodos con el nombre del metodo como clave
	 */
	public DaoManager(Class<?> clazz) {
		DomReader dReader = new DomReader(clazz.getSimpleName());
		Map<String, String[]> queryArgsNames = dReader.getParams();
		Map<String, String> querys = dReader.getQuerys();
		Map<String, Boolean> generatedKeys = dReader.generatedKeys();
		try {
			Class<?> classV = (clazz.getGenericInterfaces() != null)
					? (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0]
					: null;
			Class<?> classK = (clazz.getGenericInterfaces() != null)
					? (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[1]
					: null;

			Method[] methodsList = clazz.getMethods();
			
			for (int i = 0; i < methodsList.length; i++) {
				this.daoMethods.put(methodsList[i].getName(),
						new DaoMethod(methodsList[i], querys.get(methodsList[i].getName()),
								queryArgsNames.get(methodsList[i].getName()),
								typesFromGenericParams(classV, classK, methodsList[i].getParameters()),
								ReturnFactory.getReturnCaster(methodsList[i], getClassFromList(methodsList[i].getGenericReturnType(),classV,classK)),
								generatedKeys.get(methodsList[i].getName())));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/*
	 * Se le pasa el tipo devuelto por un metodo, si es una lista se devuelve el tipo parametrizado
	 */
	private Class<?> getClassFromList(Type returnType, Class<?> V, Class<?> K){
		if(returnType instanceof ParameterizedType){
			ParameterizedType aType = (ParameterizedType) returnType;
			
			return  (Class<?>) typeFromGeneric(V,K,aType.getActualTypeArguments()[0]);
		} else {
			return (Class<?>) typeFromGeneric(V,K,returnType);
		}
		
	}
	
	/*
	 * Devuelve los tipos de los parametros del metodo, si es generico devuelve
	 * el tipo parametrizado del generico
	 */
	private Type[] typesFromGenericParams(Class<?> V, Class<?> K, Parameter[] parameters) {
		Type[] types = new Type[parameters.length];
		for (int x = 0; x < parameters.length; x++) {
			types[x] = typeFromGenericParam(V,K,parameters[x]);
		}
		return types;

	}
	
	private Type typeFromGenericParam(Class<?> V, Class<?> K, Parameter parameter){
		return typeFromGeneric(V,K,parameter.getParameterizedType());
		
	}
	
	private Type typeFromGeneric(Class<?> V, Class<?> K, Type type){
		return (type.getTypeName().equalsIgnoreCase("V")) ? V
				 : (type.getTypeName().equalsIgnoreCase("K")) ? K
							: type;
	}

	/*
	 * recupera el DaoMethod del mapa, crea un PreparedStatement 
	 * pasa los argumentos, ejecuta el prepared stmt y procesa el resultado
	 */
	public Object execute(String name, Object[] args, Connection conect) {
		try {
			return daoMethods.get(name).execute(conect, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
