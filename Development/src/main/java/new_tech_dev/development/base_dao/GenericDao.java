package new_tech_dev.development.base_dao;

import java.util.List;

public interface GenericDao<V,K>{
	
	
	public V findOne(K e);
	
	public K add(V e);
	
	public List<V> findAll();
	
	public void delete(V v);
	
	public void update(V e);
	
	public Class<V> getType();
	
}
