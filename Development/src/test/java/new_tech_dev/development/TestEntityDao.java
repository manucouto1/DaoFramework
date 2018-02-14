package new_tech_dev.development;

import new_tech_dev.development.base_dao.BaseTestDao;

public interface TestEntityDao extends BaseTestDao<Integer>{
	public void cleanDb();
}
