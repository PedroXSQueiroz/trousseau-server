package br.com.pedroxsqueiroz.trousseau_server.sevices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.TrousseauStatus;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.models.TrousseauItem;
import br.com.pedroxsqueiroz.trousseau_server.models.TrousseauItemDiff;
import br.com.pedroxsqueiroz.trousseau_server.repositories.TrousseauDao;

@Service
public class TrousseauService {

	@Autowired
	private TrousseauDao dao;
	
	public Trousseau get(Integer id) 
	{
		return this.dao.findById(id).get();
	}
	
	public void update(Trousseau trousseau) 
	{
		//FIXME: assegurar que enxoval existe previamente
		this.dao.save(trousseau);
	}
	
	public Trousseau setStatus( Integer trousseauId, TrousseauStatus status ) 
	{
		
		Trousseau trousseau = this.get(trousseauId);
		
		trousseau.setStatus(status);
		
		this.update(trousseau);
		
		return trousseau;
		
	}
	
	public void putDiff(Trousseau trousseau, List<TrousseauItemDiff> diff) 
	{
		trousseau.setDiff(diff);
		
		this.update(trousseau);
	}

	public Trousseau putDiff(Integer id, List<TrousseauItemDiff> diff) {
		
		Trousseau trousseau = this.get(id);
		
		this.putDiff(trousseau, diff);
		
		return trousseau;
	}

	public List<TrousseauItemDiff> generateDiff(Integer id, List<TrousseauItem> itensToCompare) {
		
		Trousseau trousseau = this.get(id);
		
		List<TrousseauItemDiff> diff = trousseau.getItens()
					.stream()
					.map( trousseauItem -> {
			
						TrousseauItemDiff trousseauItemDiff = new TrousseauItemDiff();
						trousseauItemDiff.setItem(trousseauItem.getItem());
						
						Integer trousseauItemQuantity = trousseauItem.getQuantity();
						
						if(!itensToCompare.contains(trousseauItem)) 
						{
							trousseauItemDiff.setQuantity(trousseauItemQuantity * -1);
							return trousseauItemDiff;
						}
						
						int itemToCompareIndex = itensToCompare.indexOf(trousseauItem);
						TrousseauItem itemToCompare = itensToCompare.get(itemToCompareIndex);
						Integer quantityToCompare = itemToCompare.getQuantity();
						
						trousseauItemDiff.setQuantity( quantityToCompare - trousseauItemQuantity );
						
						return trousseauItemDiff;
						
					})
					.filter( item -> item.getQuantity() != 0 )
					.collect(Collectors.toList());
		
		
		return diff;
	}
	
}
