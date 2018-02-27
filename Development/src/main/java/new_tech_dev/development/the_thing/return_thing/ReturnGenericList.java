package new_tech_dev.development.the_thing.return_thing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReturnGenericList<T> extends Return<T,List<T>>{
	
	private final List<Class<?>> constructorClasses;
	
	private final Class<T> returnClass;
	
	public ReturnGenericList(Class<T> returnClass) {
		this.returnClass = returnClass;
		this.constructorClasses = generateConstructorTypes(returnClass);
	}

	@Override
	public List<T> execute(ResultSet rs) throws SQLException {
		List<T> result = new ArrayList<>();
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
