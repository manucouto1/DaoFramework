package new_tech_dev.development.the_thing.return_thing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import new_tech_dev.development.base_shit.base_entity.BaseEntity;

public abstract class Return <K,R>{
	
	public abstract R execute(ResultSet rs) throws Exception;
	
	/*
	 * Recupera una lista con los tipos del constructor de la Clase que devuelve
	 * el metodo
	 */
	protected List<Class<?>> generateConstructorTypes(Class<?> returnClass) {
		List<Class<?>> ccClass = new ArrayList<>();
		if (BaseEntity.class.isAssignableFrom(returnClass)) {
			for (Class<?> cClazz : returnClass.getConstructors()[1].getParameterTypes()) {
				ccClass.add(cClazz);
			}
		} else {
			ccClass.add(returnClass);
		}
		return ccClass;
	}
	
	/*
	 * Crea y castea un objeto de una clase a partir de los datos recogidos de la base de datos
	 */
	@SuppressWarnings("unchecked")
	protected K cast(Object[] params, Class<K> returnClass){
		Constructor<K> constructor = null;
		Constructor<K>[] constructors;
		K objeto = null;
		if(BaseEntity.class.isAssignableFrom(returnClass)){
			try {
				constructors = (Constructor<K>[]) returnClass.getConstructors();
				constructor = (constructors.length>1)?constructors[1]:constructors[0];
				try {
					objeto = constructor.newInstance(params);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
				}
				return (K) returnClass.cast(objeto);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{
			return (returnClass.equals(Void.TYPE))?null:(params.length==0)?null:(K) params[0];
		}
	}
	
}
