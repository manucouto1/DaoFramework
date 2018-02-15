package new_tech_dev.development.the_thing.proxy_thing;

import new_tech_dev.development.the_thing.handler_thing.CachedHandler;

public class CachedDao<T> {
	
	private T returned;
	
	@SuppressWarnings("unchecked")
	public CachedDao(Class<?> clazz) {
		returned = (T) java.lang.reflect.Proxy.newProxyInstance(
	            clazz.getClassLoader(),
	            new java.lang.Class[] { clazz },
	            new CachedHandler(clazz));
	}
	
	public T getDao() {
		return returned;
	}
	
}
