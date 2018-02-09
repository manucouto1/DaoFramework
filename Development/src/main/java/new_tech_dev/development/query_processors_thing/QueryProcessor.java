package new_tech_dev.development.query_processors_thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.method_ting.MethodInfo;

public abstract class QueryProcessor {

	public static String process(MethodInfo method, Map<String, Object> argNameValue) {

		return (method.getQuery().contains("[")) ? parseQuery(method, argNameValue) : method.getQuery();
	}

	private static String parseQuery(MethodInfo method, Map<String, Object> nameValue) {
		String[] aux;
		String[] queryParts = method.getQuery().split("\\[");
		List<String> queryArgs = new ArrayList<>();
		List<String> queryFragments = new ArrayList<>();

		for (String part : queryParts)
			if (part.contains("]")) {
				aux = part.split("\\]");
				queryArgs.add(aux[0]);
				queryFragments.add((aux.length < 1) ? aux[1] : "");
			} else {
				queryFragments.add(part);
			}

		for (String arg : queryArgs)
			if (arg.contains(".")) {
				arg = getValueFromEntityObject(arg, method, nameValue.get(arg));
			} else {
				arg = (String) nameValue.get(arg);
			}

		return null;
	}

	private static String getValueFromEntityObject(String argument, MethodInfo method, Object valueObject) {
		String entity = argument.split("\\.")[0];
		String attribute = argument.split("\\.")[1];
		Class<?> clase;
		try {
			clase = Class.forName(method.getType(entity).getTypeName());
			Method getMethod = clase.getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1),
					(Class<?>[]) null);
			clase.cast(valueObject);
			return (getMethod.getReturnType().equals(String.class))
					? "'" + (String) getMethod.invoke(valueObject, (Object[]) null) + "'"
					: (String) getMethod.invoke(valueObject, (Object[]) null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
