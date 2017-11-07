package new_tech_dev.development.executor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.dbacces.ConnectionFactory;
import new_tech_dev.development.domacces.DomReader;

public class Executor {
	
	private String pathName;
	private DomReader dReader;
	private ConnectionFactory conexion;
	private Map<String,String> querys = new HashMap<String,String>();
	private Class<?> generic;
	
	public Executor(Class<?> clazz) {
		this.pathName = clazz.getName();
		this.conexion = new ConnectionFactory();
		this.dReader = new DomReader(clazz.getSimpleName());
		this.querys = dReader.getQuerys();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> execute(String action, Object obj) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException{
			
			int i = 1;
			List<Object> resultSet= new ArrayList<>();
			List<T> result = new ArrayList<>();
			Object objeto = null;
			generic =  Class.forName(pathName); 
			Constructor<?> [] constructor = generic.getConstructors();
			Class<?>[] types = constructor[0].getParameterTypes();
			if(types.length==0)types = constructor[1].getParameterTypes();
			//Leemos la query y la ejecutamos
			ResultSet rs;
			try {
				String query = processQuery(querys.get(action),obj);
				rs = conexion.execute(query);
				if(null != rs){
					while(rs.next()){
						for(Class<?> clazz: types){
							if(clazz.equals(String.class)){
								resultSet.add(rs.getString(i));
							}
							if(clazz.equals(Integer.class)){
								resultSet.add(rs.getInt(i));
							}
							if(clazz.equals(Boolean.class)){
								resultSet.add(rs.getBoolean(i));
							}
							i++;
						}
						i=1;
						generic.cast(objeto);
//						try{
							objeto = constructor[1].newInstance(resultSet.toArray());
//						} catch(Exception e){
//							objeto = constructor[0].newInstance();
//						}
						
						generic.cast(objeto);
						resultSet = new ArrayList<Object>();
						result.add((T)objeto);
					}
				}
				ConnectionFactory.closeConnection(ConnectionFactory.getConnection(), null, rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
	
	public String processQuery(String query, Object obj) {
		String result;
		String[] trozos;
		String aux;
		Method method;
			if(query.contains("[")){
				trozos = query.split("\\[");
				result = trozos[0];
				for(int i = 1; i<trozos.length; i++){
					aux = trozos[i].split("\\]")[1];
					trozos[i] = trozos[i].split("\\]")[0];
					if(trozos[i].contains(".")){
						trozos[i] = trozos[i].split("\\.")[1];
					}
					try {
						method = generic.getMethod("get"+trozos[i].substring(0,1).toUpperCase() 
								+ trozos[i].substring(1), (Class<?>[]) null);
						if(method.getReturnType().equals(String.class)){
							result+="'"+method.invoke(obj, (Object[]) null)+"'";
						}else{
							result+=method.invoke(obj, (Object[]) null);
						}
						result+=aux;

					} catch (NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
							e.printStackTrace();
					}
				}
		}else{
			result = query;
		}
			
		return result;
	}

//	private boolean isBasicType(Object obj) {
//		Class objClass = obj.getClass();
//		
//		if(objClass.equals(String.class)){
//			return true;
//		}
//		if(objClass.equals(Integer.class)){
//			return true;
//		}
//		if(objClass.equals(Boolean.class)){
//			return true;
//		}
//		return false;
//	}
}
