package new_tech_test.test.entities;

public class Comic extends Libro{
	
	public Comic () {}
	public Comic (Integer id, String titulo, Integer paginas, Integer autor) {
		setId(id);
		setTitulo(titulo);
		setPaginas(paginas);
		setAutor(autor);
	}
	@Override
	public void leer() {
		System.out.println("Leyendo Comic "+getTitulo());
		
	}

}
