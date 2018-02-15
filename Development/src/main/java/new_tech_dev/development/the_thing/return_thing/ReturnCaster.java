package new_tech_dev.development.the_thing.return_thing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import new_tech_dev.development.base_shit.base_entity.BaseEntity;
import new_tech_dev.development.the_thing.method_thing.MethodInfo;

public class ReturnCaster {
	
	@SuppressWarnings("unchecked")
	public static <T> T execute(ResultSet rs, MethodInfo metodo) {
		return (metodo.getReturnType().equals(Void.class)||metodo.getReturnType().equals(void.class)) ? null : (metodo.getReturnType().equals(List.class)) ? (T) multyResult(rs, metodo) : singleResult(rs, metodo);
	}
	
	private static <T> List<T> multyResult(ResultSet rs, MethodInfo metodo){
		List<T> result = new ArrayList<>();
		try {
			while (rs.next()) {
				result.add(singleResult(rs,metodo));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static <T> T singleResult(ResultSet rs, MethodInfo metodo){
		List<Object> att= new ArrayList<>();
		List<Class<?>> argTypes = metodo.getConstructorTypes();
		int i = 1;
		if(rs!=null){
			try{
				for(Class<?> clazz : argTypes){
					if (clazz.equals(String.class)) {
						att.add(rs.getString(i));
					}
					if (clazz.equals(Integer.class)||clazz.equals(Integer.TYPE)) {
						att.add(rs.getInt(i));
					}
					if (clazz.equals(Boolean.class)||clazz.equals(Boolean.TYPE)) {
						att.add(rs.getBoolean(i));
					}
					if (clazz.equals(Double.class)||clazz.equals(Double.TYPE)) {
						att.add(rs.getDouble(i));
					}
					i++;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return cast(metodo,att.toArray());
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T cast(MethodInfo metodo, Object[] params){
		Constructor<T> constructor = null;
		Constructor<T>[] constructors;
		T objeto = null;
		if(BaseEntity.class.isAssignableFrom(metodo.getReturnType())){
			try {
				constructors = (Constructor<T>[]) metodo.getReturnType().getConstructors();
				constructor = (constructors.length>1)?constructors[1]:constructors[0];
				try {
					objeto = constructor.newInstance(params);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
				}
				return (T) metodo.getReturnType().cast(objeto);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{
			return (metodo.getReturnType().equals(Void.TYPE))?null:(params.length==0)?null:(T) params[0];
		}
		
	}
}
