package new_tech_test.test;

import java.util.List;

import new_tech_dev.development.container.Container;
import new_tech_dev.development.proxy.Dao;
import new_tech_test.test.entities.Comic;
import new_tech_test.test.entities.Libro;
import new_tech_test.test.entities.Novela;
import new_tech_test.test.repositories.NovelaDao;


public class App 
{
    public static void main( String[] args )
    {
        Libro comic = new Comic(1,"Comic",50);
        Libro comic1 = new Comic(2,"Comic1",51);
        Libro comic2 = new Comic(3,"Comic2",52);
        Libro comic3 = new Comic(4,"Comic3",53);
        Novela nov = new Novela(5,"Novela",50);
        Libro nov1 = new Novela(6,"Novela1",51);
        Novela nov2 = new Novela(7,"NewNov",5342);
        Libro nov3 = new Novela(8,"Novela3",53);
        Container container = new Container();
        try {
			container.put(NovelaDao.class);
			NovelaDao novDao = (NovelaDao) container.getDao(NovelaDao.class); 
			
			System.out.println("\nTESTING FINDALL:");
			List<Novela> novelas = novDao.findAll();
			for(Novela novela: novelas){
				System.out.println(novela);
			}
			
			System.out.println("\nTESTING DELETE:");
			novDao.delete(nov);
			
			System.out.println("\nTESTING UPDATE:");
			novDao.update(nov2);
			
			System.out.println("\nTESTING ADD:");
			novDao.add(nov);
			
			System.out.println("\nTESTING FINDONE:");
			System.out.println(novDao.findOne(nov)+"\n\n");
			
			
			
//			container.getExecutor(Comic.class).execute("delete", comic);
//			container.getExecutor(Comic.class).execute("delete", comic1);
//			container.getExecutor(Comic.class).execute("delete", comic2);
//			container.getExecutor(Comic.class).execute("delete", comic3);
//			container.getExecutor(Novela.class).execute("delete", nov);
//			container.getExecutor(Novela.class).execute("delete", nov1);
//			container.getExecutor(Novela.class).execute("delete", nov2);
//			container.getExecutor(Novela.class).execute("delete", nov3);
//			container.getExecutor(Novela.class).execute("add", nov);
//			container.getExecutor(Novela.class).execute("add", nov1);
//			container.getExecutor(Novela.class).execute("add", nov2);
//			container.getExecutor(Novela.class).execute("add", nov3);
//			nov1 = new Novela(6,"Nov1Act",777);
//			container.getExecutor(Novela.class).execute("update", nov1);
//			container.getExecutor(Comic.class).execute("add", comic);
//			container.getExecutor(Comic.class).execute("add", comic1);
//			container.getExecutor(Comic.class).execute("add", comic2);
//			container.getExecutor(Comic.class).execute("add", comic3);
//			container.getExecutor(Comic.class).execute("findAll", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
