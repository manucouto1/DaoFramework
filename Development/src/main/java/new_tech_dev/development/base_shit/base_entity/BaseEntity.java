package new_tech_dev.development.base_shit.base_entity;


import new_tech_dev.development.base_shit.base_cache.CachedObject;

public class BaseEntity extends CachedObject {

	protected Integer id;

	@Override
	public Integer getId() {
		return id;
	}
	
}
