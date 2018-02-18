package new_tech_dev.development.the_thing.processor_thing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.the_thing.method_thing.DaoMethod;

public abstract class QueryProcessorB {
	
	public static String process(DaoMethod method, Map<String, Object> argNameValue) {
		return (method.getQuery().contains("[")) ? parseQuery(method, argNameValue) : method.getQuery();
	}
	
	private static String parseQuery(DaoMethod method, Map<String, Object> nameValue) {
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
	
	private static String buildQuery(List<String> part, List<String> args, DaoMethod method, Map<String, Object> nameValue){
		int i;
		StringBuilder sb = new StringBuilder();
		for (i = 0; i < args.size(); i++){
			sb.append(part.get(i));
			if (args.get(i).contains(".")) {
//				sb.append(getValueFromEntityObject(args.get(i), method, nameValue));
			} else {
				sb.append((String.valueOf(nameValue.get(args.get(i)))));
			}
		}
		if(args.size()<part.size())
			sb.append(part.get(i));
		
		return sb.toString();
	}
}
