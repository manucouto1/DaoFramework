package new_tech_dev.development.container;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import new_tech_dev.development.base_entity.BaseEntity;
import new_tech_dev.development.proxy.CachedDao;

public class Container {
	
	private static final Logger LOG = LoggerFactory.getLogger(Container.class);
	
	private Map<Class<?>, Object> contenedor = new HashMap<Class<?>, Object>();
	
	private Map<Class<?>, CachedDao<?>> daoContainer = new HashMap<Class<?>, CachedDao<?>>();
	
	public <T extends BaseEntity> void put (Class <?> param1, Object param2) throws Exception {
		
		if(param1.isInstance(param2)){
			contenedor.put(param1, param2);
			if(null == daoContainer.get(param1)){
				LOG.info(" PUTTING DAO : container: key > "+param1.getSimpleName()+" | value : Dao<"+param2+">");
				daoContainer.put(param1, new CachedDao<T>(param1));
			}else{
				throw new Exception(" - ERROR: La clave ya existe");
			}
		}else{
			throw new Exception(" - ERROR: Tipos erroneos");
		}
	}
	
	public <T extends BaseEntity> void put (Class <?> param1) throws Exception {
		if(null == daoContainer.get(param1)){
			LOG.info(" PUTTING DAO : container: key > "+param1.getSimpleName());
			daoContainer.put(param1, new CachedDao<T>(param1));
		}else{
			throw new Exception(" - ERROR: La clave ya existe");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance (Class <?> param) {
		return (T) contenedor.get(param);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDao (Class <?> param) {
		LOG.info(" GETTING DAO : container key > "+param.getSimpleName());
		return (T) daoContainer.get(param).getDao();
	}
	
}
