package new_tech_dev.development.the_thing.handler_thing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import new_tech_dev.development.base_shit.base_cache.BaseEntityCache;
import new_tech_dev.development.base_shit.base_entity.BaseEntity;
import new_tech_dev.development.the_thing.executor_thing.DaoManager;

public class CachedHandler implements InvocationHandler{
	
	private DaoManager manager;
	// TODO Implement Caching functionality
	// private BaseEntityCache<BaseEntity> cache = new BaseEntityCache<>();
	
	public CachedHandler(Class<?> clazz){
		this.manager = new DaoManager(clazz);
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return manager.execute(method.getName(), args, null);
	}

}