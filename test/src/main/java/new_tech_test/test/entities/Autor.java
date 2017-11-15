package new_tech_test.test.entities;

import new_tech_dev.development.base_entity.BaseEntity;

public class Autor extends BaseEntity{
	private String name;
	public Autor(){}
	public Autor(Integer id, String name) {
		setId(id);
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
