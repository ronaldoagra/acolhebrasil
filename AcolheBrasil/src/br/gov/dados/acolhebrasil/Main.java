package br.gov.dados.acolhebrasil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * 	Acolhe Brasil: aplicação que identifica as comunidades terapêuticas que acolhem pessoas
	com transtornos decorrentes do uso, abuso ou dependência de substâncias psicoativas.
	
    Copyright (C) 2013	Ronaldo Agra <jose-ronaldo.souza@serpro.gov.br>
    					Viviane Malheiros <viviane.malheiros@serpro.gov.br>
    					Andre Menezes <andre.menezes@serpro.gov.br>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
public class Main {

	public static void main(String[] args) {
		Scanner scanner = null;
		List<String> enderecos = new ArrayList<String>();

		try {
			scanner = new Scanner(new FileReader(new File("comunidades_terapeuticas.csv").getAbsolutePath())).useDelimiter("\\;|\\n");
			String linha = scanner.nextLine(); // skip da linha de cabeçalho
			
			while (scanner.hasNext()) {
				linha = scanner.nextLine();
				linha = linha.replaceAll("\"", "");
				
				String[] atributos = linha.split(";", 19);
				/*"Nome da instituição/unidade";
				 * "Tipo de Logradouro";
				 * "Nome do Logradouro";
				 * "Número no Logradouro";
				 * "Complemento";
				 * "Bairro";
				 * "CEP";
				 * "Estado";
				 * "Municipio";
				 * "Latitude";
				 * "Longitude";
				 * "Telefone 1";
				 * "Telefone 2";
				 * "E-mail ";
				 * "Público atendido pela instituição";
				 * "A instituição atende algum outro público específico?";
				 * "Qual?";
				 * "Sexo do público atendido";
				 * "Modalidade de internação";;*/
				
				/* ATRIBUTOS DE CADA LINHA DO ARQUIVO
					Nome da instituição/unidade
					Tipo de Logradouro
					Nome do Logradouro
					Número no Logradouro
					Complemento
					Bairro
					CEP
					Estado
					Municipio
					Latitude
					Longitude
					Telefone 1
					Telefone 2
					E-mail
					Público atendido pela instituição
					A instituição atende algum outro público específico?
					Qual?
					Sexo do público atendido
					Modalidade de internação
				*/
				
				String nome = atributos[0];
				String endereco = getEndereco(atributos[1], atributos[2], atributos[3], atributos[4], atributos[5], atributos[6], atributos[7],atributos[8]);
				String latitude = atributos[9];
				String longitude = atributos[10];
				String telefones = getTelefones(atributos[11], atributos[12]);
				String email = atributos[13];
				String publicoAlvo = atributos[14];
				String publicoEspecifico = !"".equals(atributos[16]) ? " (" + atributos[16] + ")" : "";
				String sexoPublico = getSexo(atributos[17]);
				String modalidadeInternacao = atributos[18];
				
				if("-".equals(latitude) || "-".equals(longitude)) {
					continue;
				}
				
				latitude = latitude.replace(",", ".");
				longitude = longitude.replace(",", ".");
				modalidadeInternacao = modalidadeInternacao.replaceAll(";", "");
				
				String itemArray = "{\"nome\": \"" + nome + "\", " +
									"\"endereco\": \"" + endereco + "\", " +
									"\"fone\": \"" + telefones + "\", " +
									"\"email\": \"" + email + "\", " +
									"\"publico\": \"" + publicoAlvo + "\", " +
									"\"publicoEspec\": \"" + publicoEspecifico + "\", " +
									"\"sexo\": \"" + sexoPublico + "\", " +
									"\"internacao\": \"" + modalidadeInternacao + "\", " +
									"\"lat\": " + latitude + ", " +
									"\"lng\": " + longitude + "}";
				
				enderecos.add(itemArray);
			}
			
			System.out.println(enderecos.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	private static String getSexo(String sexo) {
		return sexo.equals("Feminino") || sexo.equals("Masculino") ? sexo.charAt(0)+"" : "A";
	}

	private static String getEndereco(String tipo, String logradouro, String num, String compl, String bairro,
			String cep, String uf, String cidade) {
		
		String endereco = tipo + " " + logradouro + ", " + num;
		
		if(!"".equals(compl)) {
			endereco += ", " + compl;
		}
		
		endereco += ",<br>" + bairro + ", " + cep + ", " + cidade + "-" + uf;
		
		return endereco;
	}
	
	private static String getTelefones(String fone1, String fone2) {
		String telefones = fone1;
		
		if(!"".equals(fone2)) {
			telefones += " / " + fone2;
		}
		
		return telefones;
	}

}
