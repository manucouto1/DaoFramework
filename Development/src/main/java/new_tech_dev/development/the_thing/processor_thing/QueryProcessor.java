package new_tech_dev.development.the_thing.processor_thing;

import java.util.ArrayList;
import java.util.List;

import new_tech_dev.development.the_thing.method_thing.DaoMethod;

public abstract class QueryProcessor {

	public static String process(DaoMethod method) {
		return (method.getQuery().contains("[")) ? parseQuery(method) : method.getQuery();
	}
	
	public static List<String> getTockens(String query){
		List<String> tokens = new ArrayList<>();
		if(query.contains("[")){
			String[] queryParts = query.split("\\[");
			String[] aux;
			for(String part: queryParts){
				if(part.contains("]")){
					aux = part.split("\\]");
					tokens.add(aux[0]);
				}
			}
		}
		
		return tokens;
	}

	private static String parseQuery(DaoMethod method) {
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
		
		return buildQuery(queryFragments,queryArgs);
	}
	
	private static String buildQuery(List<String> part, List<String> args){
		int i;
		StringBuilder sb = new StringBuilder();
		for (i = 0; i < args.size(); i++){
			sb.append(part.get(i));
			sb.append("?");
		}
		if(args.size()<part.size())
			sb.append(part.get(i));
		
		return sb.toString();
	}

}
