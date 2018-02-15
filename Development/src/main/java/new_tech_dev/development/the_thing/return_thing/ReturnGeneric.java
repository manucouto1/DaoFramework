package new_tech_dev.development.the_thing.return_thing;

import java.sql.ResultSet;

public class ReturnGeneric <T> extends Return<T,T> {
	
	
	public T execute(ResultSet rs, Class<T> clazz) throws Exception{
		
		return null;
	}
}
