package new_tech_dev.development.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import new_tech_dev.development.base_cache.CacheManager;
import new_tech_dev.development.base_entity.BaseEntity;
import new_tech_dev.development.executor.DaoExecutor;

public class CachedDao<T> {
	
	private T returned;
	
	@SuppressWarnings("unchecked")
	public CachedDao(Class<?> clazz) {
		returned = (T) java.lang.reflect.Proxy.newProxyInstance(
	            clazz.getClassLoader(),
	            new java.lang.Class[] { clazz },
	            new java.lang.reflect.InvocationHandler() {
	            	private CacheManager cache = new CacheManager<>();
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						String method_name = method.getName();
						Type[] genericInterfaces = clazz.getGenericInterfaces();
						Type[] types = method.getParameterTypes();
						Type[] typos = null ;
						Class<T> paraZed = null;
						DaoExecutor<T> ex = null;
						
						
						for (Type genericInterface : genericInterfaces) {
						    if (genericInterface instanceof ParameterizedType) {
						        Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
						        typos = genericTypes;
						        paraZed = (Class<T>) Class.forName(genericTypes[0].getTypeName());
						        
						        // TODO Usar una cache en vez del ejecutor y guardar el ejecutor en la cache
						        ex = new DaoExecutor<>(paraZed);
						    }
						}
						// Decide the method to execute the action
						System.out.println(" Method name >> "+method_name+" , parameterS > "+method.getGenericParameterTypes().length);
						if(method.getGenericParameterTypes().length > 0){
							
							if((method.getGenericParameterTypes().length==1)){
								
								if(method_name.equalsIgnoreCase("findOne")){
									return (null != cache.getCache(args[0])) ? cache.getCache(args[0]) : cache.putCache((BaseEntity) ex.executeOne(method_name, typos, args));
								} 
								if(method_name.equalsIgnoreCase("delete")){
									return (null != ex.executeOne(method_name, typos, args) && 0 < (Integer) ex.executeOne(method_name, typos, args))? cache.deleteFromCache((Integer) args[0]) : false;
								}
								if(method_name.equalsIgnoreCase("update")){
									return (null != ex.executeOne(method_name, typos, args) && 0 < (Integer) ex.executeOne(method_name, typos, args))? cache.update((BaseEntity) args[0]) : false;
								}
								if(method_name.equalsIgnoreCase("add")){
									return (null != ex.executeOne(method_name, typos, args) && 0 < (Integer) ex.executeOne(method_name, typos, args))? cache.putCache((BaseEntity) args[0]): false;
								}
								
							} 
							if(method.getReturnType().getSimpleName().contains("List")){
									return ex.execute(method_name, types, args);
									}
									return ex.executeOne(method_name, types, args);
							
							
						} else if(method_name.equalsIgnoreCase("findAll"))
							{
								return (0==cache.getAll().size())?cache.putCache(ex.executeNoParams(method_name)): cache.getAll();
							} else if(method.getReturnType().getSimpleName().contains("List"))
							{
								return ex.executeNoParams(method_name);
							}
							return ex.executeOneNoParams(method_name);
						}
	            });
	}
	
	public T getDao() {
		return returned;
	}
	
}
