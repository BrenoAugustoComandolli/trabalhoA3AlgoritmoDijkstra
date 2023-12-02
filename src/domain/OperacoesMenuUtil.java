package domain;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import data.Vertice;

public class OperacoesMenuUtil {
	
	private OperacoesMenuUtil() {}

	public static void preencheVerticesVindosArquivo() {
		List<String> conteudoArq = LeitorArquivoUtil.lerArquivo("src/conteudo.txt");
		LogicaDijkstraUtil.adicionaConexoes(conteudoArq);
		
		if(LogicaDijkstraUtil.getVertices().isEmpty()) {
			System.out.println("-----------------------------");
			System.out.println("  Nenhum vertice encontrado  ");
			System.out.println("-----------------------------");
		}
	}
	
	private static void exibirMenuPrincipal() {
		System.out.println("-------------------Menu-------------------");
		System.out.println("1 - Calcular a distancia entre dois pontos");
		System.out.println("2 - Criar uma Pilha dos vertices");
		System.out.println("3 - Criar uma Fila dos vertices");
		System.out.println("4 - Sair");
		System.out.println("------------------------------------------");
	}
	
	public static void carregarMenuOpcoes() {
		var teclado = new Scanner(System.in);
		int opcao;
		
		do {
			exibirMenuPrincipal();
			try {				
				opcao = teclado.nextInt();
			} catch (InputMismatchException e) {
				opcao = 5;
			}
			
			switch (opcao) {
			case 1: calcularMenorCaminho(teclado); break;
			case 2: montaPilha(); break;
			case 3: montaFila(); break;
			case 4: break;
			default:
				System.out.println("Opcao digitada invalida!");
				break;
			}
		} while (opcao != 4);
		teclado.close();
	}
	
	public static void calcularMenorCaminho(Scanner teclado) {
		String nomeOrigem = "";
		String nomeDestino = "";
		
		do {
			System.out.println("Vertice de origem:");
			nomeOrigem = teclado.next().toUpperCase();
		}while(!LogicaDijkstraUtil.isVerticeExistente(nomeOrigem));
		
		do {
			System.out.println("Vertice de destino:");
			nomeDestino = teclado.next().toUpperCase();
		}while(!LogicaDijkstraUtil.isVerticeExistente(nomeDestino));
		
		if(nomeOrigem.equals(nomeDestino)) {
			System.out.println("Vertices de origem e destino sao iguais!");
		}
		
		Vertice verticeOrigem = LogicaDijkstraUtil.recuperarVertice(nomeOrigem);
		Vertice verticeDestino = LogicaDijkstraUtil.recuperarVertice(nomeDestino);
		
		String caminho = LogicaDijkstraUtil.recuperarCaminhoMenorEntreVertices(verticeOrigem, verticeDestino);
		
		montraCaminhoFeito(caminho);
	}
	
	private static void montaPilha() {
		List<Double> pilha = CollectionsUtil.montaPilhaApartirListaPesos(LogicaDijkstraUtil.getPesos());
		exibeListaValores("Pilha", pilha);
	}

	private static void montaFila() {
		List<Double> fila = CollectionsUtil.montaFilaApartirListaPesos(LogicaDijkstraUtil.getPesos());
		exibeListaValores("Fila", fila);
	}

	private static void montraCaminhoFeito(String caminho) {
		System.out.println("");
		System.out.println("----------------------------");
		System.out.println("Caminho mais curto: "+ caminho);
		System.out.println("----------------------------");
		System.out.println("");
	}

	private static void exibeListaValores(String titulo, List<Double> fila) {
		System.out.println("----------------------------");
		System.out.println(titulo+":");
		System.out.println("----------------------------");
		System.out.println("INICIO[");
		
		if(!fila.isEmpty()) {
			for (Double umValor : fila) {
				System.out.println(" "+ umValor);
			}
		}
		
		System.out.println("]FIM");
		System.out.println("----------------------------");
	}
	
}