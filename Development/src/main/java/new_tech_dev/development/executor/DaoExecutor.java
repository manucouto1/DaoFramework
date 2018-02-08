package new_tech_dev.development.executor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import new_tech_dev.development.base_entity.BaseEntity;
import new_tech_dev.development.db_acces.ConnectionFactory;
import new_tech_dev.development.dom_acces.DomReader;

public class DaoExecutor <T>{
	// TODO Separar responsabilidades 
	/*
	 *  1- Parse Query
	 *  2- Procesar ResultSet
	 *  
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DaoExecutor.class);
	
	private DomReader dReader;
	
	private ConnectionFactory conexion;
	
	private Map<String,String> querys = new HashMap<String,String>();
	
	private Map<String,String[]> argsName = new HashMap<String,String[]>();
	
	private Class<?> generic;
	
	private Constructor<?>  constructor;
	
	private Class<?>[] types;
	
	public DaoExecutor(Class<?> clazz) {
		
			this.conexion = new ConnectionFactory();
			this.dReader = new DomReader(clazz.getSimpleName());
			this.querys = dReader.getQuerys();
			this.argsName = dReader.getArgs();
			this.generic = clazz;
			
			try {
				this.constructor = clazz.getConstructors()[1];
			} catch(Exception e) {
				this.constructor = clazz.getConstructors()[0];
			}
			this.types = constructor.getParameterTypes();
	}
	
	// Execute Action
	public Object executeOne(String action, Type[] types, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException{
			String logString = "";
			ResultSet rs = executeQuery(getProcessedQuery(args,types,action));
			logString+=" PROCESSING: action > "+action+", args > ";
			logString+="[";
			for(Object a : args ){
				logString+= a+", ";
			}
			logString +="]";
			logString +=", cast > "+this.generic;
			LOG.info(logString);
			if(action.equalsIgnoreCase("add")){
				if(null != rs){
					if(rs.next()){
						LOG.info(" PROCESSION: "+action+" : "+rs.getString(1));
						return rs.getString(1);
					} 
				}
			}else{
				if(null != rs){
					if(rs.next()){
						
						return  processSingleResult(rs);
					} 
				}
			}
			
			return null;
	}
	
	public List<?> execute(String action, Type[] types, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException{

			return processMultyResult(executeQuery(getProcessedQuery(args,types,action)));
	}
	
	public List<?> executeNoParams(String action) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		
			return processMultyResult(executeQuery(querys.get(action)));
	}
	
	public Object executeOneNoParams(String action, Type tipo) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		
			ResultSet rs = executeQuery(querys.get(action));
			
			if(null != rs){
				if(rs.next()){
					if(tipo.getTypeName().equalsIgnoreCase("int")||tipo.getTypeName().equalsIgnoreCase("Integer"))
					return  rs.getInt(1);
				} 
			}
			return rs;
		
	}
	
	// Execute Query
	public ResultSet executeQuery(String query) {
			
			ResultSet rs = null;
			try {
				rs = conexion.execute(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
	}
	
	// Process Query Results
	public Object processSingleResult(ResultSet rs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		
			List<Object> resultSet= new ArrayList<>();
			Object objeto = null;
			int i = 1;
			if (null!=rs) {
				for (Class<?> clazz: types) {
					if (clazz.equals(String.class)) {
						resultSet.add(rs.getString(i));
					}
					if (clazz.equals(Integer.class)) {
						resultSet.add(rs.getInt(i));
					}
					if (clazz.equals(Boolean.class)) {
						resultSet.add(rs.getBoolean(i));
					}
					if (clazz.equals(Double.class)) {
						resultSet.add(rs.getDouble(i));
					}
					i++;
				}
				objeto = constructor.newInstance(resultSet.toArray());
				generic.cast(objeto);
			}
			LOG.info(" OUTPUT Processor : Object > "+objeto);
			return  objeto;
	}
	
	public List<?> processMultyResult(ResultSet rs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		
			List<Object> result = new ArrayList<>();
			while (rs.next()) {
				result.add(processSingleResult(rs));
			}
			return result;
	}
	
	private String processQuery(String query, Type[] types, String[] names, Object[] obj) {
		LOG.info(" PROCESSING Query > "+query);
		String result;
		String[] trozos;
		Class<?> clase = null;
		String aux;
		if (query.contains("[")) {
			trozos = query.split("\\[");
			result = trozos[0];
			for (int i = 1; i < trozos.length; i++) {
				aux = trozos[i].split("\\]")[1];
				trozos[i] = trozos[i].split("\\]")[0];
				if (trozos[i].contains(".")) {
					String[] elements = trozos[i].split("\\.");
					trozos[i] = elements[1];
				}
//				System.out.println(" @@## i-1 >> "+(i-1));
				try {
					clase = Class.forName(types[i - 1].getTypeName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (clase.cast(obj[i - 1]) instanceof BaseEntity) {
					Method method = null;
					try {
						method = clase.getMethod(
								"get" + trozos[i].substring(0, 1).toUpperCase() + trozos[i].substring(1),
								(Class<?>[]) null);
						if (method.getReturnType().equals(String.class)) {
							result += "'" + method.invoke(obj[i - 1], (Object[]) null) + "'";
						} else {
							result += method.invoke(obj[i - 1], (Object[]) null);
						}
						result += aux;

					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} else {
					for (int j = 0; j < names.length; j++) {
						if (trozos[i].equalsIgnoreCase(names[j])) {
							if (types[j].equals(String.class)) {
								result += "'" + obj[j] + "'";
							} else {
								result += obj[j];
							}
							result += aux;
						}
					}
				}
			}
		} else {
			result = query;
		}

		return result;
}
	
	private String processAllBEQuery(String query, Type[] types, String[] names, Object[] obj ){
		
			String result;
			String [] trozos;
			String aux;
			Method method;
			if (query.contains("[")) {
				trozos = query.split("\\[");
				result = trozos[0];
				for (int i = 1; i<trozos.length; i++) {
					aux = trozos[i].split("\\]")[1];
					trozos[i] = trozos[i].split("\\]")[0];
					if (trozos[i].contains(".")) {
						String[] elements = trozos[i].split("\\.");
						trozos[i] = elements[1];
						for (int j=0; j<names.length; j++) {
							if (names[j].contains(elements[0])) {
								try {
									Class<?> clase = Class.forName(types[j].getTypeName());
									method = clase.getMethod("get" + trozos[i].substring(0,1).toUpperCase() 
											+ trozos[i].substring(1), (Class<?>[]) null);
									
									if (method.getReturnType().equals(String.class)) {
										result += "'" + method.invoke(obj[j], (Object[]) null) + "'";
									} else {
										result += method.invoke(obj[j], (Object[]) null);
									}
									result += aux;
									
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						}
					}
					
				}
			}else{
				result = query;
			}
			return result;
}
	
	// Utilities 
	private String getProcessedQuery(Object [] args, Type[] types, String action){
		String query;
		if(allBaseEntity(args)){
			query = processAllBEQuery(querys.get(action), types, argsName.get(action), args).toUpperCase();
		} else {
			query = processQuery(querys.get(action), types, argsName.get(action), args).toUpperCase();
		}
		
		return query;
	}
	
	private boolean allBaseEntity(Object[] args){
			
			boolean result = true;
			for(Object obj : args) {
				if(!(obj instanceof BaseEntity)) {
					result = false;
					break;
				}
			}
			return result;
	}
	
}
