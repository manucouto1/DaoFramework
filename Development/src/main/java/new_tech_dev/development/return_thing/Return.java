package new_tech_dev.development.return_thing;

import java.sql.Connection;
import java.sql.ResultSet;

public abstract class Return <K,R>{
	
	public abstract R execute(String query, Class<K> clazz) throws Exception;
	
	protected ResultSet process(String query, Connection connection){
		return null;
	}
}
