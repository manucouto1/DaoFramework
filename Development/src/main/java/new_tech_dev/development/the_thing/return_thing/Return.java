package new_tech_dev.development.the_thing.return_thing;

import java.sql.ResultSet;

public abstract class Return <K,R>{
	
	public abstract R execute(ResultSet rs, Class<K> clazz) throws Exception;
	
}
