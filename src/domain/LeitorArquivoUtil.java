package domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivoUtil {

	private LeitorArquivoUtil() {}
	
	public static List<String> lerArquivo(String caminho) {
		List<String> conteudoTexto = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminho))) {
            String linha;

            while ((linha = leitor.readLine()) != null) {
            	conteudoTexto.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conteudoTexto;
	}
	
}