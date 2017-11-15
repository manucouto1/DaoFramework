package new_tech_test.test.repositories;

import java.util.List;

import new_tech_dev.development.base_dao.GenericDao;
import new_tech_test.test.entities.Autor;
import new_tech_test.test.entities.Novela;

public interface NovelaDao extends GenericDao<Novela>{
	public void update (Integer id, String titulo, Integer paginas, Integer autor);
	public List<Novela> findByTitlePages(String titulo, Integer paginas);
	public List<Novela> findLibrosFromAutor(Autor autor);
}
