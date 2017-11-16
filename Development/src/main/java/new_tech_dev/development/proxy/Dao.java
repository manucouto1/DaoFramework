package new_tech_dev.development.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import new_tech_dev.development.executor.Executor;

public class Dao<T> {
	
	private T returned;
	
	@SuppressWarnings("unchecked")
	public Dao(Class<?> clazz){
		returned = (T) java.lang.reflect.Proxy.newProxyInstance(
	            clazz.getClassLoader(),
	            new java.lang.Class[] { clazz },
	            new java.lang.reflect.InvocationHandler() {

					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						String method_name = method.getName();
						Type[] genericInterfaces = clazz.getGenericInterfaces();
						Type[] types = method.getParameterTypes();
						Type[] typos = null ;
						Class<T> paraZed = null;
						Executor<T> ex = null;
						
						for (Type genericInterface : genericInterfaces) {
						    if (genericInterface instanceof ParameterizedType) {
						        Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
						        typos = genericTypes;
						        paraZed = (Class<T>) Class.forName(genericTypes[0].getTypeName());
						        
						        // TODO Usar una cache en vez del ejecutor y guardar el ejecutor en la cache
						        ex = new Executor<>(paraZed);
						    }
						}
						
						if(method.getGenericParameterTypes().length > 0){
							if((method_name.equalsIgnoreCase("findOne")||
								method_name.equalsIgnoreCase("delete")||
								method_name.equalsIgnoreCase("update")||
								method_name.equalsIgnoreCase("add")) && (method.getGenericParameterTypes().length==1)
								){
									if(method.getReturnType().getSimpleName().contains("List")){
										return ex.execute(method_name, typos, args);
									}
									return ex.executeOne(method_name, typos, args);
							}else{
									if(method.getReturnType().getSimpleName().contains("List")){
										return ex.execute(method_name, types, args);
									}
									return ex.executeOne(method_name, types, args);
							}
						} else {
							if(method.getReturnType().getSimpleName().contains("List")){
								return ex.executeNoParams(method_name);
							}
							return ex.executeOneNoParams(method_name);
						}
					}
	            });
	}
	
	public T getDao() {
		return returned;
	}
	
}
