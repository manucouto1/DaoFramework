package new_tech_dev.development.the_thing.processor_thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.the_thing.method_thing.MethodInfo;

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
				queryFragments.add((aux.length > 1) ? aux[1] : "");
			} else {
				queryFragments.add(part);
			}
		
		return buildQuery(queryFragments,queryArgs,method,nameValue);
	}

	private static String getValueFromEntityObject(String argument, MethodInfo method, Map<String,Object> valueObject) {
		String entity = argument.split("\\.")[0].trim();
		String attribute = argument.split("\\.")[1].trim();
		Class<?> clase;
		try {
			clase = (Class<?>) method.getType(entity);
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
	
	private static <T> String getterResolve(Method getMethod,T valueObject){
		
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
	
	private static String buildQuery(List<String> part, List<String> args, MethodInfo method, Map<String, Object> nameValue){
		int i;
		StringBuilder sb = new StringBuilder();
		for (i = 0; i < args.size(); i++){
			sb.append(part.get(i));
			if (args.get(i).contains(".")) {
				sb.append(getValueFromEntityObject(args.get(i), method, nameValue));
			} else {
				sb.append((String) nameValue.get(args.get(i)));
			}
		}
		if(args.size()<part.size())
			sb.append(part.get(i));
		
		return sb.toString();
	}

}
