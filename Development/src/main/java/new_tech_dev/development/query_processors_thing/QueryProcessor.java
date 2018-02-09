package new_tech_dev.development.query_processors_thing;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class QueryProcessor {

	public static String proccess(String query, Type[] types, Object[] parameters, String[] parameterNames) {
		
		return (query.contains("["))?parseQuery(query):query;
	}
	
	private static String parseQuery(String query){
		String[] aux;
		String[] queryParts = query.split("\\[");
		List<String> queryArgs = new ArrayList<>();
		List<String> queryFragments = new ArrayList<>();
		
		for(String part : queryParts)
			if(part.contains("]")){
				aux = part.split("\\]");
				queryArgs.add(aux[0]);
				queryFragments.add((aux.length<1)?aux[1]:"");
			} else {
				queryFragments.add(part);
			}
		for(String fragment: queryFragments)
		System.out.println("@@## Fragments >"+fragment);
		for(String arg: queryArgs)
			System.out.println("@@## Ags >"+arg);
		return null;
	}
	
	
}
