package br.com.pedroxsqueiroz.trousseau_server.sevices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.repositories.FlatDao;
import br.com.pedroxsqueiroz.trousseau_server.repositories.ItemDao;
import br.com.pedroxsqueiroz.trousseau_server.repositories.TrousseauDao;

@Service
@CrossOrigin("*")
public class FlatService {

	@Value("${building_specs.floors}")
	private Integer floors;
	
	@Value("${building_specs.unities_per_floor}")
	private Integer unittiesPerFloor;
	
	@Autowired
	private FlatDao dao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private TrousseauDao trousseauDao;
	
	public Page<Flat> list(Pageable pageable)
	{
		
		List<Flat> flats = new ArrayList();
		
		int pageSize = pageable.getPageSize();
		
		long offset 		= pageable.getPageNumber() * pageSize ;
		int startFloor  	= (int) offset / this.unittiesPerFloor ;
		int startUnity		= (int) offset % this.unittiesPerFloor + 1; 	
		
		for(int currentFloor = startFloor; currentFloor <= this.floors; currentFloor++) 
		{
			
			for(
					int currentUnity = currentFloor > startFloor ? 1 : startUnity; 
					currentUnity <= this.unittiesPerFloor; 
					currentUnity++
				) 
			{
				
				Flat flat = this.getFlatByUnityAndFloor(currentUnity, currentFloor + 1);
				flats.add(flat);
				
				if(flats.size() >= pageSize) 
				{
					return new PageImpl<Flat>(flats, pageable, pageSize );
				}
				
			}
			
		}
		
		//FIXME: estourar exceção, o loop não deveria seguir até o fim
		return null;
	}
	
	public Trousseau createTrousseauOnFlat( String flatCode, Trousseau trousseau ) 
	{
		Flat flat = this.getFlatByCode(flatCode);
		
		if(!flat.initiated()) 
		{
			flat = this.init(flat);
		}
		
		trousseau.setFlat(flat);
		
		Trousseau trousseauPersisted = this.trousseauDao.save(trousseau);
		
		return trousseauPersisted;
		
	}

	private Flat getFlatByCode(String flatCode) {
		
		int lastIndexCode = flatCode.length();
		String unityStr = flatCode.substring(lastIndexCode - 2, lastIndexCode);
		String floorStr = flatCode.substring( 0, lastIndexCode - 2);
		
		Integer unity = Integer.parseInt(unityStr);
		Integer floor = Integer.parseInt(floorStr);
		
		return getFlatByUnityAndFloor(unity, floor);
	}

	private Flat getFlatByUnityAndFloor(int unity, int floor) {
		Flat flat = this.dao.findByUnityAndFloor(unity, floor);
		
		if(Objects.isNull(flat)) 
		{
			
			flat = new Flat();
			flat.setFloor(floor);
			flat.setUnity(unity);
			
		}
		
		return flat;
	}
	
	private Flat init(Flat flat) 
	{
		return this.dao.save(flat);
	}

	public List<Item> getItensFromFlat(String code) 
	{
		return this.itemDao.findAll();
	}

	public List<Trousseau> listTrousseausByFlat(String flatCode) {
		
		Flat flat = this.getFlatByCode(flatCode);
		
		if(!flat.initiated()) 
		{
			return new ArrayList<Trousseau>();
		}
		
		return this.trousseauDao.findAll( ( root, query, cb ) -> 
			cb.equal( root.get("flat"), flat )
		);
	}

}
