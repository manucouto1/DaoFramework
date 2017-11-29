package new_tech_test.test.arbolBinario;

public class Nodo {
	/* Declaraciones de variables */
    private Object valor;
    private Integer peso;
    private Integer nivel;
    
 
    private Nodo padre;
    private Nodo hojaIzquierda;
    private Nodo hojaDerecha;
 
    /* Constructor */
    public Nodo(int peso) {
        this.peso = peso;
    }
 
    /* Setters y Getters */
    public void setPeso(int peso){
    	this.peso = peso;
    }
    
    public Integer getPeso() {
    	return peso;
    }
    
    public void setNivel(Integer nivel) {
    	this.nivel = nivel;
    }
    public int getNivel() {
    	return nivel;
    }
    
    public void setValor(Object valor) {
        this.valor = valor;
    }
 
    public Object getValor() {
        return valor;
    }
 
    public Nodo getPadre() {
        return padre;
    }
 
    public void setPadre(Nodo padre) {
        this.padre = padre;
    }
 
    public Nodo getHojaIzquierda() {
        return hojaIzquierda;
    }
 
    public void setHojaIzquierda(Nodo hojaIzquierda) {
        this.hojaIzquierda = hojaIzquierda;
    }
 
    public Nodo getHojaDerecha() {
        return hojaDerecha;
    }
 
    public void setHojaDerecha(Nodo hojaDerecha) {
        this.hojaDerecha = hojaDerecha;
    }

	@Override
	public String toString() {
		return "Nodo [valor=" + valor + ", peso=" + peso + ", nivel=" + nivel + ", hojaIzquierda="
				+ ((null != hojaIzquierda)?hojaIzquierda.getPeso():null) + ", hojaDerecha=" + ((null != hojaDerecha)?hojaDerecha.getPeso():null) + "]";
	}
    
    
}
