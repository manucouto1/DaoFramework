package new_tech_test.test.entities;

public class Novela extends Libro{
	
	public Novela(){}
	public Novela(Integer id, String titulo, Integer paginas){
		setId(id);
		setTitulo(titulo);
		setPaginas(paginas);
	}
	@Override
	public void leer() {
		System.out.println("Leyendo Novela "+getTitulo());
		
	}

}
