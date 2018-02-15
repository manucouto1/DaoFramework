package new_tech_dev.development;

import new_tech_dev.development.base_shit.base_dao.BaseTestDao;

public interface TestEntityDao extends BaseTestDao<Integer,Void>{
	public void cleanDb();
}
