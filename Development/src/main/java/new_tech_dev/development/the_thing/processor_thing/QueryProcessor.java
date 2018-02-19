package new_tech_dev.development.the_thing.processor_thing;

import java.util.ArrayList;
import java.util.List;


public class QueryProcessor {
	
	private final List<String> qTokens = new ArrayList<>();
	
	private final String preparedQuery;
	
	public QueryProcessor(String query){
	
		List<String> queryArgs = new ArrayList<>();
		List<String> queryFragments = new ArrayList<>();
		if(query != null){
			if(query.contains("[")){
				String[] queryParts = query.split("\\[");
				String[] aux;
				for(String part: queryParts){
					if(part.contains("]")){
						aux = part.split("\\]");
						qTokens.add(aux[0]);
						queryArgs.add(aux[0]);
						queryFragments.add((aux.length > 1) ? aux[1] : "");
					} else {
						queryFragments.add(part);
					}
				}
				this.preparedQuery = buildQuery(queryFragments,queryArgs);
			}else{
				this.preparedQuery = query;
			}
			
		} else {
			this.preparedQuery = ";";
			// TODO - Crear una hoja XML con las consultas de los metodos de baseDao
		}
	}
	
	private String buildQuery(List<String> part, List<String> args){
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
	
	public List<String> getTokens(){
		return this.qTokens;
	}
	
	public String getPreparedQuery(){
		return this.preparedQuery;
	}

}
