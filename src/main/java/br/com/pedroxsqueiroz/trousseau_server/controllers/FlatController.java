package br.com.pedroxsqueiroz.trousseau_server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.sevices.FlatService;

@RestController
@RequestMapping("flats")
@CrossOrigin("*")
public class FlatController {

	@Autowired
	private FlatService flatService;
	
	@GetMapping("/")
	@ResponseBody
	public Page<Flat> list(Pageable page)
	{
		return this.flatService.list(page);
	}
	
	@PostMapping("/{code}/trousseau")
	@ResponseBody
	public Trousseau createTrousseauOnFlat( @PathVariable("code") String code, 
											@RequestBody Trousseau trousseau) 
	{
		return this.flatService.createTrousseauOnFlat(code, trousseau);
	}
	
	@GetMapping("/{code}/trousseaus")
	@ResponseBody
	public List<Trousseau> listTrousseausByFlat(@PathVariable("code") String flatCode)
	{
		return this.flatService.listTrousseausByFlat( flatCode );
	}  
	
	@GetMapping("/{code}/itens")
	@ResponseBody
	public List<Item> getItensFromTrousseau(@PathVariable("code") String code) 
	{
		
		return this.flatService.getItensFromFlat(code);
		
	}
	
}
