package new_tech_test.test.entities;

import new_tech_dev.development.base_entity.BaseEntity;

public abstract class Libro extends BaseEntity{
	
	private String titulo;
	private int paginas;
	private int autor;
	
	public Integer getId(){
		return super.id;
	}
	
	public void setId(Integer id){
		super.id = id;
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
		return "Libro [id=" + getId() + ", titulo=" + titulo + ", paginas=" + paginas + "]";
	}
	
	
	
	
}
