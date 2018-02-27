package new_tech_dev.development.the_thing.query_thing.executor_thing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExecutor {

	private final PreparedStatement pStmt;
	
	private final String query;
	
	private static final Logger LOG = LoggerFactory.getLogger(QueryExecutor.class);
	
	public QueryExecutor(PreparedStatement pStmt, String query){
		this.pStmt = pStmt;
		this.query = query;
	}
	
	public ResultSet execute() {
		
		try {
			if(query.toUpperCase().contains("CALL")||query.toUpperCase().contains("DELETE")){
				pStmt.execute();
				LOG.info(" EXECUTE > "+pStmt);
				return null;
			} 
			if(query.toUpperCase().contains("UPDATE")||query.toUpperCase().contains("INSERT")){
				pStmt.executeUpdate();
				LOG.info(" EXECUTE UPDATE > "+pStmt);
				return pStmt.getGeneratedKeys();
			}else{
				LOG.info(" EXECUTE QUERY > "+pStmt);
				return pStmt.executeQuery();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
