package new_tech_test.test.repositories;

import java.util.List;

public interface GenericDao <T>{
	public void add(T e);
	public T findOne(T e);
	public List<T> findAll();
	public void delete(T id);
	public void update(T e);
}
