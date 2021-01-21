package br.com.pedroxsqueiroz.trousseau_server.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.TrousseauStatus;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.models.TrousseauItem;
import br.com.pedroxsqueiroz.trousseau_server.models.TrousseauItemDiff;
import br.com.pedroxsqueiroz.trousseau_server.sevices.TrousseauService;

@RestController
@RequestMapping("/trousseaus")
@CrossOrigin("*")
public class TrousseauController {

	@Autowired
	private TrousseauService service;
	
	@GetMapping("/{id}")
	@ResponseBody
	public Trousseau getById(@PathVariable("id") Integer id ) 
	{
		return this.service.get(id);
	}
	
	@PostMapping("/{id}/diff")
	@ResponseBody
	public List<TrousseauItemDiff> generateDiff(
				@PathVariable("id") Integer id,
				@RequestBody List<TrousseauItem> itensToCompare
			)
	{
		
		return this.service.generateDiff(id, itensToCompare);
		
	}
	
	@PutMapping("/{id}/status/{status}")
	@ResponseBody
	public Trousseau setStatus(	@PathVariable("id") Integer id, 
								@PathVariable("status") String statusStr,
								@RequestBody(required = false) List<TrousseauItemDiff> diff) 
	{
		
		TrousseauStatus status = TrousseauStatus.valueOf(statusStr);
		
		if(status != TrousseauStatus.NOT_OK) 
		{
			if(!Objects.isNull(diff)) 
			{
				//FIXME: estourar exceção
				// diff só pode ser eviado em caso de NOT_OK 
			}
			
			Trousseau setStatus = this.service.setStatus( id, status );
			
			return setStatus;			
		}
		else 
		{
			Trousseau trousseau = this.getById(id);

			if(!Objects.isNull(diff))
			{
				this.service.putDiff(trousseau, diff);
			}

			this.service.setStatus(trousseau, status);

			return trousseau;
		}
		
	}
	
	
}
