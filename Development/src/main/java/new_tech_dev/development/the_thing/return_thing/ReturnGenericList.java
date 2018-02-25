package new_tech_dev.development.the_thing.return_thing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import new_tech_dev.development.base_shit.base_entity.BaseEntity;

public class ReturnGenericList<T> extends Return<T,List<T>>{
	
	private final List<Class<?>> constructorClasses;
	
	private final Class<T> returnClass;
	
	public ReturnGenericList(Method metodo, Class<T> returnClass) {
		this.returnClass = returnClass;
		this.constructorClasses = generateConstructorTypes(returnClass);
	}

	@Override
	public List<T> execute(ResultSet rs) throws SQLException {
		List<T> result = new ArrayList<>();
		System.out.println("@@## ReturnGenericList > "+returnClass);
		while(rs.next()){
			result.add(buildEntity(rs));
		}
		return result;
	}
	
	private T buildEntity(ResultSet rs){
		List<Object> att= new ArrayList<>();
		int i = 1;
		if(rs!=null){
			try{
				for(Class<?> clazz : constructorClasses){
					System.out.println("@@## CLASS >> "+clazz);
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
					if (clazz.equals(Object.class)) {
						att.add(rs.getObject(i));
					}
					i++;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return cast(att.toArray(), this.returnClass);
	}
	
	/*
	 * Recupera una lista con los tipos del constructor de la Clase que devuelve
	 * el metodo
	 */
	private List<Class<?>> generateConstructorTypes(Class<?> returnClass) {
		List<Class<?>> ccClass = new ArrayList<>();
		if (BaseEntity.class.isAssignableFrom(returnClass)) {
			for (Field field : returnClass.getFields()) {
				ccClass.add(field.getType());
			}
		} else {
			ccClass.add(returnClass);
		}
		return ccClass;
	}
}
