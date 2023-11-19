package domain;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtil {

	private CollectionsUtil() {}
	
	public static List<Double> montaPilhaApartirListaPesos(List<Double> lPesos) {
		List<Double> pilha = new ArrayList<>();
		
		if(!lPesos.isEmpty()) {			
			for (int index = lPesos.size()-1; index >= 0; index--) {
				pilha.add(lPesos.get(index));
			}
		}
		
		return pilha;
	}
	
	public static List<Double> montaFilaApartirListaPesos(List<Double> lPesos) {
		List<Double> fila = new ArrayList<>();
		
		if(!lPesos.isEmpty()) {				
			for (int index = 0; index < lPesos.size(); index++) {
				fila.add(lPesos.get(index));
			}
		}
		
		return fila;
	}

}