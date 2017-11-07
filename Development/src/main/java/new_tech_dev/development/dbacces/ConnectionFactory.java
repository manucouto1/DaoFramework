package new_tech_dev.development.dbacces;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.Driver;

public class ConnectionFactory {
	
	private static Properties dbProperties;
	private static String url;
	private static Driver dbDriver;
	
	static{
		System.out.println(" Estableciendo conexion ... ");
		try{
			dbProperties = new Properties();
			dbProperties.load(new FileInputStream("src/main/resources/jdbc.properties"));
			dbDriver = (Driver)Class.forName(dbProperties.getProperty("DriverClassName")).newInstance();
			url = dbProperties.getProperty("url");
			System.out.println(" Conexion establecida.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static Connection getConnection() throws Exception{
        return dbDriver.connect(url,dbProperties);
	}
	
	public ResultSet execute(String query) throws Exception{
		System.out.println(" Ejecutando Query: "+query);
		Connection con = getConnection();
		Statement stmt = (Statement) con.createStatement();
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(query);
		}catch(Exception e){
			stmt.executeUpdate(query);
		}
		return rs;
		
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
