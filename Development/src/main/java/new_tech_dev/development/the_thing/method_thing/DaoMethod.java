package new_tech_dev.development.the_thing.method_thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.the_thing.query_thing.executor_thing.QueryExecutor;
import new_tech_dev.development.the_thing.query_thing.processor_thing.QueryProcessor;
import new_tech_dev.development.the_thing.return_thing.Return;

public class DaoMethod {

	private final String name;
	private final String[] qArgsNames;
	private final Map<String, Type> argNameType = new HashMap<>();
	private final QueryProcessor processor;
	private boolean returnGeneratedKeys;
	private final Return<?, ?> returnCaster;

	/*
	 * Se mira que el numero de parámetros de la consulta y del metodo de la
	 * interfaz coincidan. Se ve que tengan el mismo nombre y esten en el mismo
	 * orden si todo va bien se crean atributos que asocian nombre de parametro
	 * con tipo de parametro, tambien se guarda la query cruda y los nombres de
	 * los parametros
	 */
	public DaoMethod(Method metodo, String rawQuery, String[] qArgsNames, Type[] types, Return<?, ?> returnCaster,
			Boolean generatedKeys) throws Exception {

		this.name = metodo.getName();
		this.qArgsNames = qArgsNames;
		this.returnCaster = returnCaster;
		this.processor = new QueryProcessor(rawQuery);
		this.returnGeneratedKeys = (generatedKeys != null) ? generatedKeys : false;

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
	 */
	public Object execute(Connection con, Object[] args) throws Exception {

		PreparedStatement pStmt;
		QueryExecutor qExec;

		if (returnGeneratedKeys) {
			pStmt = con.prepareStatement(processor.getPreparedQuery(), Statement.RETURN_GENERATED_KEYS);
		} else {
			pStmt = con.prepareStatement(processor.getPreparedQuery());
		}

		if (args != null) {
			List<String> tokens = processor.getTokens();
			for (int i = 0; i < tokens.size(); i++) {
				if (tokens.get(i).contains(".")) {
					pStmt.setString(i + 1,
							getValueFromEntityObject(processor.getTokens().get(i), argKeyValueGenerator(args)));
				} else {
					pStmt.setString(i + 1,
							String.valueOf(argKeyValueGenerator(args).get(processor.getTokens().get(i))));
				}

			}
		}
		qExec = new QueryExecutor(pStmt, processor.getPreparedQuery());
		return this.returnCaster.execute(qExec.execute());
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
	 * Convierte el token a valor final para insertar en la consulta TODO creo
	 * que el map es inecesario
	 */
	private String getValueFromEntityObject(String argument, Map<String, Object> valueObject) {
		String entity = argument.split("\\.")[0].trim();
		String attribute = argument.split("\\.")[1].trim();
		Class<?> clase;
		try {
			clase = (Class<?>) getType(entity);
			Method getMethod = clase.getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1),
					(Class<?>[]) null);

			String returnValue = getterResolve(getMethod, valueObject.get(entity));
			return (returnValue != null) ? returnValue : null;

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
	private <T> String getterResolve(Method getMethod, T valueObject) {

		Object value = null;
		String stringValue;
		try {
			if (valueObject != null) {
				value = getMethod.invoke(valueObject, (Object[]) null);
				stringValue = (value != null) ? String.valueOf(value) : null;
			} else {
				stringValue = "null";
			}
			return stringValue;

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
	public String getName() {
		return this.name;
	}

	public Type getType(String name) {
		return argNameType.get(name);
	}

	public String getQuery() {
		return processor.getPreparedQuery();
	}
}
