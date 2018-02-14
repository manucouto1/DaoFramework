package new_tech_dev.development.return_thing;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import new_tech_dev.development.method_ting.MethodInfo;

public class ReturnCaster{
	
	@SuppressWarnings("unchecked")
	public static <T> T execute(ResultSet rs, MethodInfo metodo) {
		System.out.println(" @@## clazz >> "+metodo.getReturnType());
		
		return (metodo.getReturnType().equals(Void.class)) ? null : (metodo.getReturnType().equals(List.class)) ? (T) multyResult() : singleResult();
	}
	
	private static <T> List<T> multyResult(){
		
		
		return null;
	}
	private static <T> T singleResult(){
//		List<Object> resultSet= new ArrayList<>();
//		Object objeto = null;
//		int i = 1;
//		if (null!=rs) {
//			for (Class<?> clazz: types) {
//				if (clazz.equals(String.class)) {
//					resultSet.add(rs.getString(i));
//				}
//				if (clazz.equals(Integer.class)) {
//					resultSet.add(rs.getInt(i));
//				}
//				if (clazz.equals(Boolean.class)) {
//					resultSet.add(rs.getBoolean(i));
//				}
//				if (clazz.equals(Double.class)) {
//					resultSet.add(rs.getDouble(i));
//				}
//				i++;
//			}
//			objeto = constructor.newInstance(resultSet.toArray());
//			generic.cast(objeto);
//		}
//		LOG.info(" OUTPUT Processor : Object > "+objeto);
		return null;
	}
}
