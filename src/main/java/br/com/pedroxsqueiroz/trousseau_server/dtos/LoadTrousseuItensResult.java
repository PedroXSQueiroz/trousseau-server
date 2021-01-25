package br.com.pedroxsqueiroz.trousseau_server.dtos;

import java.util.List;

import lombok.Data;

@Data
public class LoadTrousseuItensResult {

	public Integer totalFlatsLoaded;
	
	public Integer totalItensLoadad;
	
	public List<String> errors;
	
}
