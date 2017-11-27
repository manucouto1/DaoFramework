package new_tech_dev.development.container;

import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.base_entity.BaseEntity;
import new_tech_dev.development.proxy.CachedDao;

public class Container {
	
	private Map<Class<?>, Object> contenedor = new HashMap<Class<?>, Object>();
	
	private Map<Class<?>, CachedDao<?>> daoContainer = new HashMap<Class<?>, CachedDao<?>>();
	
	public <T extends BaseEntity> void put (Class <?> param1, Object param2) throws Exception {
		
		if(param1.isInstance(param2)){
			contenedor.put(param1, param2);
			if(null == daoContainer.get(param1)){
				daoContainer.put(param1, new CachedDao<T>(param1));
			}
		}else{
			throw new Exception("Tipos erroneos");
		}
	}
	
	public <T extends BaseEntity> void put (Class <?> param1) throws Exception {
		if(null == daoContainer.get(param1)){
			daoContainer.put(param1, new CachedDao<T>(param1));
		}else{
			throw new Exception("Executor ya creado");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance (Class <?> param) {
		return (T) contenedor.get(param);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDao (Class <?> param) {
		return (T) daoContainer.get(param).getDao();
	}
	
}
