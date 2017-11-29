package new_tech_test.test.arbolBinario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BusquedaManager {
	
	public void preorden(Nodo  nodo){
		if(nodo == null)
			return;
		//Primero se imprime el valor despues se pasa al siguiente nodo
		System.out.println("Nodo Peso => "+nodo.getPeso());
		preorden(nodo.getHojaIzquierda());
		preorden(nodo.getHojaDerecha());
	}
	
	public void posorden(Nodo nodo){
		if (nodo == null) 
			return;
		//Primero se recorre el arbol despues se van imprimiendo los valores hacia atras
		preorden(nodo.getHojaIzquierda());
		preorden(nodo.getHojaDerecha());
		System.out.println("Nodo Peso => "+nodo.getPeso());
		
	}
	
	public Nodo busquedaAnchura(Nodo raiz, Object c) {
		Queue<Nodo> colaAuxiliar = new LinkedList<Nodo>();
		colaAuxiliar.add(raiz);
		
		while(colaAuxiliar.size() != 0) {
			Nodo nodo = colaAuxiliar.poll();
			System.out.println("Nodo Peso => "+nodo.getPeso());
			if(nodo.getValor() == c) {
				return nodo;
			}else{
				colaAuxiliar.add(nodo.getHojaIzquierda());
				colaAuxiliar.add(nodo.getHojaDerecha());
			}
		}
		return null;
	}
	
	public Map<Integer,List<Nodo>> convertToMap(Nodo raiz) {
		Queue<Nodo> colaAuxiliar = new LinkedList<Nodo>();
		List<Nodo> listaAux = new ArrayList<>();
		Map<Integer,List<Nodo>> result = new HashMap<Integer,List<Nodo>>();
		colaAuxiliar.add(raiz);
		int aux = 1;
		while(colaAuxiliar.size() != 0) {
			Nodo nodo = colaAuxiliar.poll();
			if(null != nodo){
				if(aux != nodo.getNivel()){
					result.put(aux, listaAux);
					aux = nodo.getNivel();
					listaAux = new ArrayList<>();
				}
				listaAux.add(nodo);
				colaAuxiliar.add(nodo.getHojaIzquierda());
				colaAuxiliar.add(nodo.getHojaDerecha());
			}
			result.put(aux, listaAux);
		}
		return result;
	}
	
}
