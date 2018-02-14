package new_tech_dev.development;

import org.junit.Test;

import new_tech_dev.development.executor_thing.DaoManager;


public class AppTest{
	
    @Test
    public void testQueryProccesor() {
    	try {
			new DaoManager(TestEntityDao.class).execute("testEntity", null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
