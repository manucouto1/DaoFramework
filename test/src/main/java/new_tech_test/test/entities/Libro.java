package new_tech_test.test.entities;


public abstract class Libro {
	
	private int id;
	private String titulo;
	private int paginas;
	private int autor;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getPaginas() {
		return paginas;
	}

	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}

	public int getAutor() {
		return autor;
	}

	public void setAutor(int autor) {
		this.autor = autor;
	}

	public abstract void leer();

	@Override
	public String toString() {
		return "Libro [id=" + id + ", titulo=" + titulo + ", paginas=" + paginas + "]";
	}
	
	
	
	
}
