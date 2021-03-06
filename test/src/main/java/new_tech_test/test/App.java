package new_tech_test.test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.container.Container;
import new_tech_test.test.arbolBinario.BusquedaManager;
import new_tech_test.test.arbolBinario.Nodo;
import new_tech_test.test.entities.Autor;
import new_tech_test.test.entities.Comic;
import new_tech_test.test.entities.Novela;
import new_tech_test.test.repositories.AutorDao;
import new_tech_test.test.repositories.ComicDao;
import new_tech_test.test.repositories.NovelaDao;
import new_tech_test.test.stream.TestConcurrency;
import new_tech_test.test.torneo.TorneoManager;


public class App 
{
    public static void main( String[] args )
    {
    	testArbolBB();
//    	testDev();
//    	testStream();
    }
    
    public static void testArbolBB(){
    	List test = Arrays.asList("1","2","3","4");
    	TorneoManager torneo = new TorneoManager();
    	
    	torneo.setJugadores(test);
    	torneo.generarTorneo();
//    	torneo.listarNodos();
    	
    	Map<Integer,List<Nodo>> tabla = torneo.getTabla();
    	
    	for(Map.Entry<Integer, List<Nodo>> entry : tabla.entrySet()){
    		System.out.println(" Ronda > "+entry.getKey());
    		for(Nodo nodo : entry.getValue()){
    			System.out.print(" "+nodo);
    		}
    		System.out.println(" ");
    	}
    }
    public static void testStream(){
    	TestConcurrency conc = new TestConcurrency();
    }
    
    public static void testDev() {
    	Comic comic3 = new Comic(4,"Comic3",53,1);
        Novela nov = new Novela(5,"Novela",53,1);
        Autor autor = new Autor(1,"Autor1");
        Novela nov2 = new Novela(7,"NewNov",5342,1);
        
        Container container = new Container();
        
        try {
			container.put(NovelaDao.class);
			container.put(AutorDao.class);
			container.put(ComicDao.class);
			
			NovelaDao novDao = container.getDao(NovelaDao.class); 
			AutorDao autDao = container.getDao(AutorDao.class);
			ComicDao comicDao = container.getDao(ComicDao.class);
			
			System.out.println("\nTESTING FIND BY AUTOR TITULO");
			System.out.println(comicDao.findByAutorTitle(autor, "NOVELA"));
			
			System.out.println("\nTESTING FIND BY TWO ENTITYES");
			System.out.println(autDao.findByComicNovela(comic3, nov));
			System.out.println("\nTESTING DELETE:");
			novDao.delete(nov2);
			comicDao.delete(comic3);
			novDao.delete(nov);
			autDao.delete(autor);
			
			System.out.println("\nTESTING FINDALL:");
			
			List<Novela> novelas = novDao.findAll();
			for(Novela novela: novelas){
				System.out.println(novela);
			}
			
			System.out.println("\nTESTING ADD:");
			autDao.add(autor);
			novDao.add(nov);
			
			System.out.println("\nTESTING UPDATE:");
			novDao.update(7,"NewNov",5342,1);
			novDao.update(4,"Updated",777,1);
			
			System.out.println("\nTESTING FIND LIBROS FROM AUTOR");
			novelas = novDao.findLibrosFromAutor(autor);
			if(null != novelas){
				for(Novela novela: novelas){
					System.out.println(novela);
				}
			}
			System.out.println("\nTESTING FIND BY TITLE PAGINAS:");
			System.out.println(novDao.findByTitlePages("Comic",50));
			
			System.out.println("\nTESTING FINDONE:");
			System.out.println(novDao.findOne(nov)+"\n\n");
			
			System.out.println("\nTESTING FINDONE CACHE:");
			System.out.println(novDao.findOne(nov)+"\n\n");

			System.out.println("\nTESTING FINDALL:");
			
			novelas = novDao.findAll();
			for(Novela novela: novelas){
				System.out.println(novela);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
