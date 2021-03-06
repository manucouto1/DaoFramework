package new_tech_test.test.repositories;

import new_tech_dev.development.base_dao.GenericDao;
import new_tech_test.test.entities.Autor;
import new_tech_test.test.entities.Comic;
import new_tech_test.test.entities.Novela;

public interface AutorDao extends GenericDao<Autor>{
	public Autor findByComicNovela(Comic comic, Novela novela);
}
