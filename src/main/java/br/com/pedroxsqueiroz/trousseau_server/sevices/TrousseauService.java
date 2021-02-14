package br.com.pedroxsqueiroz.trousseau_server.sevices;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.LogType;
import br.com.pedroxsqueiroz.trousseau_server.contants.enums.TrousseauFailEnum;
import br.com.pedroxsqueiroz.trousseau_server.models.TrousseauLog;
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

	@Autowired
	private UserService userService;
	
	public Trousseau get(Integer id) 
	{
		return this.dao.findById(id).get();
	}
	
	public void update(Trousseau trousseau) 
	{
		//FIXME: assegurar que enxoval existe previamente
		this.dao.save(trousseau);
	}

	public Trousseau setStatus( Trousseau trousseau, TrousseauStatus status )
	{
		if( TrousseauStatus.NOT_OK.equals( status ) )
		{
			TrousseauFailEnum fail = this.resolveFailEnum( trousseau );
			trousseau.setFail(fail);
		}

		trousseau.setStatus(status);

		TrousseauLog log = new TrousseauLog();
		log.setStatus( status );
		log.setRegisterDate( new Date() );
		log.setUser( this.userService.getLoggedUser() );
		log.setType( LogType.TROUSSEAU_CHANGE );

		trousseau.addLog(log);


		this.update(trousseau);

		return trousseau;
	}

	public Trousseau setStatus( Integer trousseauId, TrousseauStatus status ) 
	{
		
		Trousseau trousseau = this.get(trousseauId);

		return this.setStatus(trousseau, status);
	}

	private TrousseauFailEnum resolveFailEnum(Trousseau trousseau) {

		switch (trousseau.getStatus())
		{
			case SENT:
				return TrousseauFailEnum.MISPLACED;

			case RETRIEVED:

				List<TrousseauItemDiff> diff = trousseau.getDiff();
				if(Objects.isNull(diff) || diff.size() == 0 )
				{
					return TrousseauFailEnum.INVALIDATED_BY_USER;
				}

				return TrousseauFailEnum.INVALIDATED_BY_AUTOCHECK;

			default:
				return null;
		}
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
