package new_tech_test.test.entities;

import java.util.List;

import new_tech_test.test.cache.GenericCacheObject;

public class Persona extends GenericCacheObject<Integer,Persona>{

	public Persona(Class<?> clazz) {
		super(clazz);
	}
	
	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	protected List<String> directoryFor(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void persist(Integer key, Persona value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected Persona readPersisted(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected boolean isPersist(Integer key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
