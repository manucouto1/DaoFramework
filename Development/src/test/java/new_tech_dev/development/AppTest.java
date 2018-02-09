package new_tech_dev.development;

import org.junit.Test;

import new_tech_dev.development.query_processors_thing.QueryProcessor;

public class AppTest{
	
    @Test
    public void testQueryProccesor() {
    	QueryProcessor.proccess("select * from tabla where id_uno = [esto] and id_dos = [esto.otro]", null, null, null);
    }
}
