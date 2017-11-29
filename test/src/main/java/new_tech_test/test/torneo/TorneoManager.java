package new_tech_test.test.torneo;

import java.util.List;
import java.util.Map;

import new_tech_test.test.arbolBinario.Abb;
import new_tech_test.test.arbolBinario.BusquedaManager;
import new_tech_test.test.arbolBinario.Nodo;

public class TorneoManager {
	
	private BusquedaManager busqueda;
	private List<String> jugadores;
	private Integer altura;
	private Abb arbol;
	
	public TorneoManager() {
		busqueda = new BusquedaManager();
	}
	
	public void setJugadores(List<String> jugadores){
		this.jugadores = jugadores;
		this.altura = (int) (Math.log(jugadores.size()) / Math.log(2)) + 1;
	}
	
	public void generarTorneo(){
		this.arbol = new Abb();
		int aux = (altura * 2) + 1;
		generarRondas(altura,(aux + 1)/2);
	}
	
	public void generarRondas(int altura, int aux){
		
		if (altura == 2) {
			arbol.addNodo(new Nodo(aux));
			arbol.addNodo(new Nodo(aux-1));
			arbol.addNodo(new Nodo(aux+1));
		} else {
			arbol.addNodo(new Nodo(aux));
			generarRondas(altura-1,aux/2);
			generarRondas(altura-1,aux+aux/2);
		}
		
	}
	
	public void listarNodos(){
		busqueda.preorden(arbol.getRaiz());
	}
	
	public void insertarJugadores(){
		
	}

	public Map<Integer,List<Nodo>> getTabla() {
		return  busqueda.convertToMap(arbol.getRaiz());
	}
	
	
}
