package new_tech_dev.development.the_thing.return_thing;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReturnGeneric <T> extends Return<T,T> {
	
	private final List<Class<?>> constructorClasses;
	
	private final Class<T> returnClass;
	
	
	public ReturnGeneric(Method metodo, Class<T> returnType){
		this.returnClass = returnType;
		this.constructorClasses = generateConstructorTypes(this.returnClass);
	}
	
	public T execute(ResultSet rs) throws Exception{
		List<Object> att= new ArrayList<>();
		int i = 1;
		if(rs!=null){
			rs.next();
			try{
				for(Class<?> clazz : constructorClasses){
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
	
}
