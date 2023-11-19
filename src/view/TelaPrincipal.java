package view;

import domain.LogicaDijkstraUtil;
import domain.OperacoesMenuUtil;

public class TelaPrincipal {
	
	public static void main(String[] args) {
		OperacoesMenuUtil.preencheVerticesVindosArquivo();
		
		if(!LogicaDijkstraUtil.getVertices().isEmpty()) {
			OperacoesMenuUtil.carregarMenuOpcoes();
		}
	}
	
}