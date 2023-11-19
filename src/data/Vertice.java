package data;

import java.util.HashMap;
import java.util.Map;

public class Vertice {

	private String nome;
	private Map<String, Double> conexoes = new HashMap<>();

	public Vertice(String nome, Map<String, Double> conexoes) {
		this.nome = nome;
		this.conexoes = conexoes;
	}

	public Map<String, Double> getConexoes() {
		return conexoes;
	}

	public void setConexoes(Map<String, Double> conexoes) {
		this.conexoes = conexoes;
	}

	public String getNome() {
		return nome.toUpperCase();
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}