package new_tech_dev.development.return_thing;

import new_tech_dev.development.db_acces.ConnectionFactory;

public class ReturnGeneric <T> extends Return<T,T> {
	
	private ConnectionFactory connection;
	
	public T execute(String query, Class<T> clazz) throws Exception{
		process(query, connection.getConnection());
		return null;
	}
}
