package new_tech_dev.development.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import new_tech_dev.development.executor.Executor;

public class Dao<T> {
	
	private T dao;
	
	@SuppressWarnings("unchecked")
	public Dao(Class<?> clazz){
		dao = (T) java.lang.reflect.Proxy.newProxyInstance(
	            clazz.getClassLoader(),
	            new java.lang.Class[] { clazz },
	            new java.lang.reflect.InvocationHandler() {

					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						String method_name = method.getName();
						Type[] genericInterfaces = clazz.getGenericInterfaces();
						Executor ex = null;
						for (Type genericInterface : genericInterfaces) {
						    if (genericInterface instanceof ParameterizedType) {
						        Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
//						        for (Type genericType : genericTypes) {
//						            System.out.println("Generic type: " + genericType);
//						        }
						        Class<?> c = Class.forName(genericTypes[0].getTypeName());
						        ex = new Executor(c);
						    }
						}
		                if(method.getGenericParameterTypes().length>0){
		                	return (T) ex.execute(method_name, args[0]);
		                } else {
		                	return (T) ex.execute(method_name, null);
		                }
						
					}
	            	
	            });
	}

	public T getDao() {
		return dao;
	}

}
