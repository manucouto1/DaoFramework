package new_tech_test.test.repositories;

import new_tech_dev.development.base_dao.GenericDao;
import new_tech_test.test.entities.Autor;
import new_tech_test.test.entities.Comic;

public interface ComicDao extends GenericDao<Comic>{
	public Comic findByAutorTitle(Autor autor, String titulo);
}
