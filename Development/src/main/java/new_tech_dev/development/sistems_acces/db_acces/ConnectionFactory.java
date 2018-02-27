package new_tech_dev.development.sistems_acces.db_acces;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.Driver;

public class ConnectionFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private static Properties dbProperties;
	
	private static String url;
	
	private static Driver dbDriver;
	
	static{
		LOG.info(" CONNECTING: Estableciendo conexion ... ");
		try{
			dbProperties = new Properties();
			dbProperties.load(new FileInputStream("src/main/resources/jdbc.properties"));
			dbDriver = (Driver)Class.forName(dbProperties.getProperty("DriverClassName")).newInstance();
			url = dbProperties.getProperty("url");
			LOG.info(" CONNECTING: Conexion establecida.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection() throws Exception{
        return  dbDriver.connect(url,dbProperties);
	}
	
	public static void closeConnection(Connection con, Statement st, ResultSet rs) throws Exception{
        if(con!=null)
                con.close();
        		System.out.println(" Conexion finalizada.\n");
        if(st!=null)
                st.close();
        if(rs!=null)
                rs.close();
	}

}
