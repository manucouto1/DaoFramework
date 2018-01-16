package new_tech_dev.development.proxy;

public class CachedDao<T> {
	
	private T returned;
	
	@SuppressWarnings("unchecked")
	public CachedDao(Class<?> clazz) {
		returned = (T) java.lang.reflect.Proxy.newProxyInstance(
	            clazz.getClassLoader(),
	            new java.lang.Class[] { clazz },
	            new GenericCachedEntityMethod<T>(clazz));
	}
	
	public T getDao() {
		return returned;
	}
	
}
