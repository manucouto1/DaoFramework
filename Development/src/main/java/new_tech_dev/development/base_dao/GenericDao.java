package new_tech_dev.development.base_dao;

import java.util.List;

public interface GenericDao <T> {
	
	public T findOne(T e);
	
	public Object add(T e);
	
	public List<T> findAll();
	
	public void delete(T e);
	
	public void update(T e);
	
	public Class<T> getType();
	
}
