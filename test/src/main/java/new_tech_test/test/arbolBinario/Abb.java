package new_tech_test.test.arbolBinario;

public class Abb {
	
	/* Atributos */
    private Nodo raiz;
 
    /* Contructories */    
    public Abb() {}
 
    public Abb( Nodo raiz ) {
        this.raiz = raiz;
    }
 
    /* Setters y Getters */
    public Nodo getRaiz() {
        return raiz;
    }
 
    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }
    
    private void addNodo( Nodo nodo, Nodo raiz ) {
        /* 2.- Partiendo de la raíz preguntamos: Nodo == null ( o no existe ) ? */
        if ( raiz == null ) {
            /* 
             * 3.- En caso afirmativo X pasa a ocupar el lugar del nodo y ya 
             * hemos ingresado nuestro primer dato. 
             * ==== EDITO =====
             * Muchas gracias a @Espectro por la corrección de esta línea
             */
        	nodo.setNivel(1);
            this.setRaiz(nodo);
            System.out.println(" -- Nodo Raiz >> "+nodo);
        }
        else {
            /* 4.- En caso negativo preguntamos: X < Nodo */
            if ( nodo.getPeso() <= raiz.getPeso() ) {
                /* 
                 * 5.- En caso de ser menor pasamos al Nodo de la IZQUIERDA del
                 * que acabamos de preguntar y repetimos desde el paso 2 
                 * partiendo del Nodo al que acabamos de visitar 
                 */
                if (raiz.getHojaIzquierda() == null) {
                	nodo.setNivel(raiz.getNivel()+1);
                	nodo.setPadre(raiz);
                	System.out.println(" -- hojaIzquierda > "+nodo);
                    raiz.setHojaIzquierda(nodo);
                }
                else {
                    addNodo( nodo , raiz.getHojaIzquierda() );
                }
            }
            else {
                /* 
                 * 6.- En caso de ser mayor pasamos al Nodo de la DERECHA y tal
                 * cual hicimos con el caso anterior repetimos desde el paso 2
                 * partiendo de este nuevo Nodo.
                 */
                if (raiz.getHojaDerecha() == null) {
                	nodo.setNivel(raiz.getNivel()+1);
                	nodo.setPadre(raiz);
                    raiz.setHojaDerecha(nodo);
                    System.out.println(" -- hojaDerecha > "+nodo);
                }
                else {
                    addNodo( nodo, raiz.getHojaDerecha() );
                }
            }
        }
    }
 
    public void addNodo( Nodo nodo ) {
        this.addNodo( nodo , this.raiz );
    }

}
