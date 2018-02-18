package new_tech_dev.development.the_thing.method_thing;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.api.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.PreparedStatement;

import new_tech_dev.development.base_shit.base_entity.BaseEntity;
import new_tech_dev.development.the_thing.processor_thing.QueryProcessor;

public class DaoMethod {

	private final String name;
	private final String rawQuery;
	private final String[] qArgsNames;
	private final List<Class<?>> constructorClasses;
	private final Map<String, Type> argNameType = new HashMap<>();
	private final Class<?> returnClass;
	private final List<String> qTokens;

	/*
	 * Se mira que el numero de parámetros de la consulta y del metodo de la
	 * interfaz coincidan. Se ve que tengan el mismo nombre y esten en el mismo
	 * orden si todo va bien se crean atributos que asocian nombre de parametro
	 * con tipo de parametro, tambien se guarda la query cruda y los nombres de
	 * los parametros
	 */
	public DaoMethod(Method metodo, String rawQuery, String[] qArgsNames, Class<?> V, Class<?> K) throws Exception {

		this.rawQuery = rawQuery;
		this.name = metodo.getName();
		this.qArgsNames = qArgsNames;
		this.returnClass = metodo.getReturnType();
		this.constructorClasses = generateConstructorTypes();
		this.qTokens = QueryProcessor.getTockens(rawQuery);

		Type[] types = typeFromGenericParams(V, K, metodo.getParameters());

		if (qArgsNames != null) {
			if (types.length == qArgsNames.length) {
				for (int i = 0; i < types.length; i++) {
					this.argNameType.put(qArgsNames[i].trim(), types[i]);
				}
			} else {
				// TODO EXCEPTION numero de nombres y de tipos diferentes
			}
		} else if (types.length != 0) {
			// TODO EXCEPTION numero de nombres y de tipos diferentes
		}
	}

	/*
	 * Este metodod pasa los argumentos y la querypreparada al QueryProcesor
	 * Devuelve el resultSet de la consulta
	 * 
	 * TODO Implementar el prepared statement 
	 */
	public ResultSet execute(JdbcConnection con, Object[] args) throws SQLException {
		
		String[] finalArgs = new String[args.length];
		String finalQuery;
		PreparedStatement pStmt ;
		
		for(int i = 0; i < args.length; i++){
			finalArgs[i] = getValueFromEntityObject(qTokens.get(i),argKeyValueGenerator(args));
		}
		
		finalQuery = QueryProcessor.process(this);
		pStmt = new PreparedStatement(con, finalQuery);
		
		return null;
	}

	/*
	 * Este metodo asocia en un map el nombre de un variable con su valor
	 */
	public Map<String, Object> argKeyValueGenerator(Object[] args) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (qArgsNames != null) {
			for (int i = 0; i < args.length; i++) {
				result.put(qArgsNames[i].trim(), args[i]);
			}
		}
		return result;
	}

	/*
	 * Recupera una lista con los tipos del constructor de la Clase que devuelve
	 * el metodo
	 */
	private List<Class<?>> generateConstructorTypes() {
		List<Class<?>> ccClass = new ArrayList<>();
		if (BaseEntity.class.isAssignableFrom(this.returnClass)) {
			for (Field field : this.returnClass.getFields()) {
				ccClass.add(field.getType());
			}
		} else {
			ccClass.add(returnClass);
		}
		return ccClass;
	}

	/*
	 * Devuelve los tipos de los parametros del metodo, si es generico devuelve
	 * el tipo parametrizado del generico
	 */
	private Type[] typeFromGenericParams(Class<?> V, Class<?> K, Parameter[] parameters) {
		Type[] types = new Type[parameters.length];
		for (int x = 0; x < parameters.length; x++) {
			types[x] = (parameters[x].getParameterizedType().getTypeName().equalsIgnoreCase("V")) ? V
					: (parameters[x].getParameterizedType().getTypeName().equalsIgnoreCase("K")) ? K
							: parameters[x].getType();
		}
		return types;

	}
	/*
	 * Convierte el token a valor final para insertar en la consulta
	 * TODO creo que el map es inecesario
	 */
	private String getValueFromEntityObject(String argument, Map<String,Object> valueObject) {
		String entity = argument.split("\\.")[0].trim();
		String attribute = argument.split("\\.")[1].trim();
		Class<?> clase;
		try {
			clase = (Class<?>) getType(entity);
			Method getMethod = clase.getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1),
					(Class<?>[]) null);
			
			String returnValue = getterResolve(getMethod,valueObject.get(entity));
			return (returnValue!=null)?returnValue:null;
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/*
	 * Ejecutan el getter del atributo a resolver
	 */
	private <T> String getterResolve(Method getMethod,T valueObject){
		
		Object value = null;
		String stringValue;
		try {
			if(valueObject!=null){
				value = getMethod.invoke(valueObject, (Object[]) null);
				stringValue = (value!=null)?String.valueOf(value):null;
			}else{
				stringValue = "null";
			}
			return (getMethod.getReturnType().equals(String.class))
			? "'" + stringValue + "'" : stringValue;
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	// Getters
	public String getQuery() {
		return this.rawQuery;
	}

	public String getName() {
		return this.name;
	}

	public Class<?> getReturnType() {
		return returnClass;
	}

	public List<Class<?>> getConstructorTypes() {
		return this.constructorClasses;
	}
	
	public Type getType(String name) {
		return argNameType.get(name);
	}
}
