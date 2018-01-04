package new_tech_dev.development.base_cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import new_tech_dev.development.base_entity.BaseEntity;

public class CacheManager <V extends BaseEntity>{
	
	private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);
	
	private Map<Integer,V> cacheHashMap = new HashMap<>();
	
	public CacheManager(){
		try {
			Thread threadCleanerUpper = new Thread(
				new Runnable(){
					int milliSecondSleepTime = 500000;
					@Override
					public void run(){
						try{
							while(true){
								 LOG.info(" SCANNING : ThreadCleanerUpper Scanning For"
										 +"Expired Objects...");
								 Set<?> KeySet = cacheHashMap.keySet();
								 Iterator<?> keys = KeySet.iterator();
								 while(keys.hasNext()){
									 Object key = keys.next();
									 V value =  cacheHashMap.get(key);
									 if (value.isExpired()){
										 cacheHashMap.remove(key);
										 LOG.info(" REMOVING : ThreadCleanerUpper Running."
										 		+ "Found an Expired Object in the Cache");
									 }
								 }
								 Thread.sleep(this.milliSecondSleepTime);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return;
					}
				});
				threadCleanerUpper.setPriority(Thread.MIN_PRIORITY);
				threadCleanerUpper.start();
		} catch (Exception e) {
			LOG.error(" CACHING : CacheManager.Static Block: " + e);
		}
	}
	
	public V putCache(V object){
		LOG.info(" CACHING : Putting cache id > "+object);
		cacheHashMap.put(object.getId(), object);
		if(cacheHashMap.get(object.getId())!= null){
			LOG.info(" CACHING : confirmed ");
			return object;
		}
		LOG.info(" CACHING : error ");
		return null;
	}
	
	public List<V> putCache(List<V> objects){
		LOG.info(" CACHING : Putting  cache > "+objects);
		for( V object : objects) {
			cacheHashMap.put(object.getId(), object);
		}
		return getAll();
	}
	
	public void KillCache(){
		cacheHashMap = new HashMap<>();
	}
	
	public boolean deleteFromCache(Integer id) {
		return (null!=cacheHashMap.remove(id));
	}
	
	public List<V> getAll(){
		return new ArrayList<V>(cacheHashMap.values());
	}
	
	public boolean update(V object){
		deleteFromCache(object.getId());
		return (null != putCache(object));
	}
	
	public V getCache( Object identifier) {
		V object = null;
		
		if(identifier instanceof Integer){
			LOG.info(" FOUND : Usando Cache para id >"+identifier);
			object = cacheHashMap.get(identifier);
		}
		if(identifier instanceof BaseEntity){
			LOG.info(" FOUND : Usando Cache para id > "+((BaseEntity) identifier).getId());
			object = cacheHashMap.get(((BaseEntity) identifier).getId());
		}
		
		LOG.info(" GET : Cache found > "+ object);
		if (object == null){
			return null;
		}
		if (object.isExpired()){
			cacheHashMap.remove(identifier);
			return null;
		} else {
			return object;
		}
		
	}
//	public V getCache( V identifier) {
//		System.out.println(" Usando Cache para id > "+identifier.getId());
//		V object = cacheHashMap.get(identifier.getId());
//		System.out.println(" Cache found > "+ object);
//		if (object == null){
//			return null;
//		}
//		if (object.isExpired()){
//			cacheHashMap.remove(identifier);
//			return null;
//		} else {
//			return object;
//		}
//		
//	}
}
