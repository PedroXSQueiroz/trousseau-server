package br.com.pedroxsqueiroz.trousseau_server.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import br.com.pedroxsqueiroz.trousseau_server.exceptions.FlatItemAlreadyExists;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.sevices.FlatService;

@RestController
@RequestMapping("/flats")
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
	public FlatItem addItemToFlat( @PathVariable("code") String code, @RequestBody FlatItem item ) throws FlatItemAlreadyExists {
		return this.flatService.addItemToFlat(code, item);
	}

	@PutMapping("/{code}/item/{name}")
	@ResponseBody
	public FlatItem updateItemOfFlat(@PathVariable("code") String code, @PathVariable("name") String name, @RequestBody FlatItem item) throws FlatItemAlreadyExists {
		return this.flatService.updateFlatItem(code, name, item);
	}

	@DeleteMapping("/{code}/item/{name}")
	@ResponseBody
	public ResponseEntity deleteItemFromFlat(@PathVariable("code") String code, @PathVariable("name") String name)
	{
		this.flatService.deleteFlatItem(code, name);

		return new ResponseEntity(HttpStatus.OK);
	}
	
	/*
	@PostMapping("/itens")
	@ResponseBody 
	ResponseEntity<?> loadItens( @RequestParam("trousseaus_flats") MultipartFile trousseausFlatsSpreedsheet) throws IOException
	{
		
		InputStream trousseausFlatsSpreedsheetInputSream = trousseausFlatsSpreedsheet.getInputStream();
		
		this.flatService.loadItensToFlats(trousseausFlatsSpreedsheetInputSream);
		
		return null;
		
	}
	*/

}
