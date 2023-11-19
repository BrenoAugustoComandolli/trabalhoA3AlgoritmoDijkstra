package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import data.Vertice;

public class LogicaDijkstraUtil {
	
	private LogicaDijkstraUtil() {}
	
	private static List<Vertice> vertices = new ArrayList<>();
	private static List<Double> pesos = new ArrayList<>();

	public static List<Vertice> getVertices() {
		return vertices;
	}

	public static List<Double> getPesos() {
		return pesos;
	}

	public static Vertice recuperarVertice(String nomeVertice) {
		for (Vertice umVertice : getVertices()) {
			if (umVertice.getNome().equals(nomeVertice)) {
				return umVertice;
			}
		}
		return null;
	}
	
	public static boolean isVerticeExistente(String nomeVertice) {
		for (Vertice umVertice : getVertices()) {
			if (umVertice.getNome() != null && 
				umVertice.getNome().equals(nomeVertice)) {
				return true;
			}
		}
		System.out.println("Nome do vertice inexistente!");
		return false;
	}
	
	public static void adicionaConexoes(List<String> listaConexoes) {
		for (String umaLinha : listaConexoes) {			
			String[] dadosConexao = umaLinha.split(";");
			
			if(dadosConexao.length != 3) {								
				System.out.println("Conexoes estam no formato invalido");
				return;
			}
			
			Vertice umVertice = recuperarVertice(dadosConexao[0]);
			if(umVertice == null) {
				umVertice = new Vertice(dadosConexao[0], new HashMap<>());
				getVertices().add(umVertice);
			}
			
			Vertice outroVertice = recuperarVertice(dadosConexao[1]);
			if(outroVertice == null) {
				outroVertice = new Vertice(dadosConexao[1], new HashMap<>());
				getVertices().add(outroVertice);
			}
			
			try {
				montaConexaoNaoDirecionada(umVertice, outroVertice, dadosConexao[2]);
			} catch (Exception e) {
				System.out.println("Conexoes esta no formato invalido");
				return;
			}
		}
	}
	
	private static void montaConexaoNaoDirecionada(Vertice umVertice, Vertice outroVertice, String peso) {
		Map<String, Double> conexoes = new HashMap<>();
		Map<String, Double> conexoesInversas = new HashMap<>();
						
		conexoes.put(outroVertice.getNome(), Double.parseDouble(peso));
		conexoesInversas.put(umVertice.getNome(), Double.parseDouble(peso));
			
		getPesos().add(Double.parseDouble(peso));
		
		umVertice.getConexoes().putAll(conexoes);
		outroVertice.getConexoes().putAll(conexoesInversas);
	}

	public static String recuperarCaminhoMenorEntreVertices(Vertice verticeOrigem, Vertice verticeDestino) {
		if(verticeOrigem == null || verticeDestino == null) {
			return "Nao foi possivel calcular";
		}
		
		Map<Vertice, Double> distancias = new HashMap<>();
		Map<Vertice, Vertice> predecessores = new HashMap<>();
		
		PriorityQueue<Vertice> filaPrioridade = new PriorityQueue<>(compareToPrioridadeVertice(distancias));
		
		defineValoresIniciaisCalculoMenorCaminho(verticeOrigem, distancias, predecessores, filaPrioridade);
		calculaMenorDistanciaAteVerticesPartindoOrigem(distancias, predecessores, filaPrioridade);
		
		List<Vertice> caminho = new ArrayList<>();
		montaCaminhoOlhandoPredecessoresApartirDestino(verticeDestino, predecessores, caminho);
		
		List<Double> pesos = new ArrayList<>();
		montaPesosOlhandoPredecessoresApartirDestino(verticeDestino, distancias, predecessores, pesos);

		StringBuilder resultado = montaDescricaoMenorCaminhoPercorridoAteDestino(verticeOrigem, verticeDestino, caminho);
		montaDescricaoPesosPercorridoAteDestino(resultado, pesos);
		
		return resultado.toString();
	}

	private static void montaPesosOlhandoPredecessoresApartirDestino(Vertice verticeDestino,
			Map<Vertice, Double> distancias, Map<Vertice, Vertice> predecessores, List<Double> pesos) {
		
		Vertice verticeAtual = predecessores.get(verticeDestino);
		Vertice verticeSucessor = verticeDestino;
		
		pesos.add(distancias.get(verticeSucessor));
		
		while (verticeAtual != null) {
			pesos.add(distancias.get(verticeSucessor)-distancias.get(verticeAtual));
			verticeSucessor = verticeAtual;
			verticeAtual = predecessores.get(verticeAtual);
		}
	}

	private static StringBuilder montaDescricaoMenorCaminhoPercorridoAteDestino(Vertice verticeOrigem,
			Vertice verticeDestino, List<Vertice> caminho) {
		
		StringBuilder resultado = new StringBuilder(verticeOrigem.getNome() + " ate " + verticeDestino.getNome() + ": [");
		for (int l = caminho.size() - 1; l >= 0; l--) {
			resultado.append(caminho.get(l).getNome());
			if (l > 0) {
				resultado.append(" -> ");
			}
		}
		
		resultado.append("]");
		return resultado;
	}
	
	private static StringBuilder montaDescricaoPesosPercorridoAteDestino(StringBuilder resultado, List<Double> pesos) {
		resultado.append(" - [");
		
		for (int l = pesos.size() - 1; l > 0; l--) {
			resultado.append(pesos.get(l));
			if (l > 1) {
				resultado.append(",");
			}
		}
		
		resultado.append("] = " + (pesos.isEmpty() ? "0.0" : pesos.get(0)));
		return resultado;
	}

	private static void montaCaminhoOlhandoPredecessoresApartirDestino(Vertice verticeDestino,
			Map<Vertice, Vertice> predecessores, List<Vertice> caminho) {
		
		Vertice verticeAtual = verticeDestino;
		
		while (verticeAtual != null) {
			caminho.add(verticeAtual);
			verticeAtual = predecessores.get(verticeAtual);
		}
	}

	private static void calculaMenorDistanciaAteVerticesPartindoOrigem(Map<Vertice, Double> distancias,
			Map<Vertice, Vertice> predecessores, PriorityQueue<Vertice> filaPrioridade) {
		
		while (!filaPrioridade.isEmpty()) {
			Vertice verticeAtual = filaPrioridade.poll();
			
			for (Map.Entry<String, Double> conexao : verticeAtual.getConexoes().entrySet()) {
				Vertice vizinho = recuperarVertice(conexao.getKey());
				double novaDistancia = getDistanciaAteVerticeVizinhoPartidoOrigem(distancias, verticeAtual, conexao);
				
				if (novaDistancia < distancias.get(vizinho)) {
					distancias.put(vizinho, novaDistancia);
					predecessores.put(vizinho, verticeAtual);
					filaPrioridade.add(vizinho);
				}
			}
		}
	}

	private static double getDistanciaAteVerticeVizinhoPartidoOrigem(Map<Vertice, Double> distancias,
			Vertice verticeAtual, Map.Entry<String, Double> conexao) {
		
		return distancias.get(verticeAtual) + conexao.getValue();
	}

	private static void defineValoresIniciaisCalculoMenorCaminho(Vertice verticeOrigem, Map<Vertice, Double> distancias, 
			Map<Vertice, Vertice> predecessores, PriorityQueue<Vertice> filaPrioridade) {
		
		for (Vertice vertice : getVertices()) {
			distancias.put(vertice, Double.POSITIVE_INFINITY);
			predecessores.put(vertice, null);
		}
		
		distancias.put(verticeOrigem, 0.0);
		filaPrioridade.add(verticeOrigem);
	}

	private static Comparator<? super Vertice> compareToPrioridadeVertice(Map<Vertice, Double> distancias) {
		return (v1, v2) -> Double.compare(distancias.get(v1), distancias.get(v2));
	}
	
}
