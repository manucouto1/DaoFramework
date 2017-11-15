package new_tech_test.test.cache;

import java.util.List;

import new_tech_test.test.entities.Comic;

public class ComicCache extends GenericCacheObject<Integer,Comic>{

	public ComicCache(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected List<String> directoryFor(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void persist(Integer key, Comic value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Comic readPersisted(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isPersist(Integer key) {
		// TODO Auto-generated method stub
		return false;
	}

}
