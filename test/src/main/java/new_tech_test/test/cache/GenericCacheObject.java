package new_tech_test.test.cache;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import new_tech_dev.development.dbacces.ConnectionFactory;
import new_tech_dev.development.executor.Executor;


public abstract class GenericCacheObject<K,V>{

		private static LoadingCache<Integer, Object> caches;
		private static final Logger LOGGER = LoggerFactory.getLogger(GenericCacheObject.class);
		private ConnectionFactory conexion;
		private Map<Integer,Boolean> isPersist;
		private Executor exec;
		
		
		public GenericCacheObject(Class<?> clazz) {
			exec = new Executor(clazz);
		}
		
		private LoadingCache<K, V> makeCache() {
			  return customCacheBuild()
			    .removalListener(new PersistingRemovalListener())
			    .build(new PersistedStateCacheLoader());
		}
			 
		protected CacheBuilder<K, V> customCacheBuild() {
			return (CacheBuilder<K, V>) CacheBuilder.newBuilder();
		}

	
		private class PersistingRemovalListener implements RemovalListener<K, V> {

			public void onRemoval(RemovalNotification<K, V> notification) {
				if(notification.getCause() != RemovalCause.COLLECTED) {
					try {
						persistValue(notification.getKey(), notification.getValue());
					} catch (Exception e) {
						LOGGER.error(String.format("Could not persist key-value: %s, %s",
								notification.getKey(), notification.getValue()));
					}
				}
			}

			private void persistValue(K key, V value) throws Exception{
				if(!isPersist(key)) return;
				
			}

			private boolean isPersist(K key) {
				return (isPersist.get(key)==null) ? false : isPersist.get(key);
			}
		}
		
		public class PersistedStateCacheLoader extends CacheLoader<K, V> {

			@Override
			public V load(K key) throws Exception {
				
				V value = null;
				try{
					value = findValueOnDB(key);
				} catch (Exception e){
					LOGGER.error(String.format("Error on finding disk value to Key: %s", key), e);
				}
				if(value != null) {
					return value;
				} else {
					return makeValue(key);
				}
			}

			private V makeValue(K key) {
				return null;
			}

			private V findValueOnDB(K key) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
				
//				Object element = exec.execute("findOne", key);
//				return (V) element;
				return null;
			}
			
		}
		
		protected abstract List<String> directoryFor(K key);
		 
		protected abstract void persist(K key, V value); 
		 
		protected abstract V readPersisted(K key ); 
		 
		protected abstract boolean isPersist(K key);
	
}



	

