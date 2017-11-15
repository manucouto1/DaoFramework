package new_tech_test.test.entities;

public class Novela extends Libro{
	
	public Novela(){}
	public Novela(Integer id, String titulo, Integer paginas, Integer autor){
		setId(id);
		setTitulo(titulo);
		setPaginas(paginas);
		setAutor(autor);
	}
	@Override
	public void leer() {
		System.out.println("Leyendo Novela "+getTitulo());
		
	}

}
