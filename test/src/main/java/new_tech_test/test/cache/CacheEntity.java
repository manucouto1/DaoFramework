package new_tech_test.test.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class CacheEntity<K,V> {
	
	private CacheManager cacheManager;
	private Cache<K, V> squareNumberCache;
	private Class<K> clazzKey;
	private Class<V> clazzValue;
	
	public CacheEntity(Class<K> clazzKey, Class<V> clazzValue) {
		this.clazzKey = clazzKey;
		this.clazzValue = clazzValue;
		cacheManager = CacheManagerBuilder
				.newCacheManagerBuilder().build();
		cacheManager.init();
	
		squareNumberCache = cacheManager
			.createCache(clazzValue.getSimpleName(), CacheConfigurationBuilder
				.newCacheConfigurationBuilder(
						clazzKey, clazzValue,
						ResourcePoolsBuilder.heap(10)));
	}
	
	public Cache<K,V> getCache(){
		return cacheManager.getCache(clazzValue.getSimpleName(), clazzKey, clazzValue);
	}
	
	
}
