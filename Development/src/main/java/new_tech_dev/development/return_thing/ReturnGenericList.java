package new_tech_dev.development.return_thing;

import java.sql.ResultSet;
import java.util.List;

public class ReturnGenericList<T> extends Return<T,List<T>>{
	
	@Override
	public List<T> execute(ResultSet rs, Class<T> clazz) {
		return null;
	}
	
}
