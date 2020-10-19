package br.com.pedroxsqueiroz.trousseau_server.controllers;

import java.util.List;

import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
	public List<FlatItem> getItensFromFlat(@PathVariable("code") String code)
	{
		return this.flatService.getItensFromFlat(code);
	}

	@PostMapping("/{code}/item")
	@ResponseBody
	public FlatItem addItemToFlat( @PathVariable("code") String code, @RequestBody FlatItem item )
	{
		return this.flatService.addItemToFlat(code, item);
	}

	@PutMapping("/item/{id}")
	@ResponseBody
	public FlatItem updateItem(@PathVariable("id") Integer id, @RequestBody FlatItem item)
	{
		return this.flatService.updateFlatItem(id, item);
	}
	
}
