package br.com.pedroxsqueiroz.trousseau_server.sevices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.pedroxsqueiroz.trousseau_server.exceptions.FlatItemAlreadyExists;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;
import br.com.pedroxsqueiroz.trousseau_server.repositories.FlatItemDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.repositories.FlatDao;
import br.com.pedroxsqueiroz.trousseau_server.repositories.ItemDao;
import br.com.pedroxsqueiroz.trousseau_server.repositories.TrousseauDao;

import javax.transaction.Transactional;

@Service
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

	@Autowired
	private FlatItemDao flatItemDao;
	
	public Page<Flat> list(Pageable pageable)
	{
		
		int pageSize 		= pageable.getPageSize();		
		int pageNumber 		= pageable.getPageNumber();
		
		long offset 		= pageNumber * pageSize ;
		int startFloor  	= (int) offset / this.unittiesPerFloor ;
		int startUnity		= (int) offset % this.unittiesPerFloor + 1; 	
		
		List<Flat> flats = new ArrayList<Flat>();
		
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
					return new PageImpl<Flat>(flats, pageable, this.unittiesPerFloor * this.floors );
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
	
	private String getCode(Flat flat) 
	{
		return this.getCode(flat.getFloor(), flat.getUnity());
	}

	private String getCode(Integer floor, Integer unity) 
	{
		return String.format("%s%s", floor, StringUtils.leftPad( unity.toString(), 2, "0" ) );
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

	@Transactional
	private Flat init(Flat flat)
	{
		return this.dao.saveAndFlush(flat);
	}

	public List<FlatItem> getItensFromFlat(String code)
	{
		Flat flat = this.getFlatByCode(code);
		
		if(!flat.initiated()) 
		{
			return new ArrayList<FlatItem>();
		}

		return this.flatItemDao.findByFlatAndUpToDate(flat, true);
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

	//FIXME: AINDA É NECESSÁRIO ARRANJAR TODAS AS PLANILHAS PARA COM PREÇOS, FLATS E ITENS PARA IMPLMENTAR ESTE MÉTODO
	//ASSIM QUE CONSEGUIR AS PLANILHAS, SERÁ NECESSÁRIO REIMPORTAR O POI NO PROJETO
	/*public void loadItensToFlats(InputStream trousseausFlatsSpreedsheetInputSream) throws EncryptedDocumentException, IOException {
		
		Workbook workbook = WorkbookFactory.create(trousseausFlatsSpreedsheetInputSream);
		
		this.flatItemDao.deleteAll();
		
		Page<Flat> 		currentPage 			= null;
		Pageable 		currentPagination 		= PageRequest.of(0, 20);
		
		final Integer	START_ROW_NUM 			= 2;
		final Integer 	LAST_ROW_NUM 			= 14;
		final Integer	ITEM_COLUMN_NAME 		= 0;
		final Integer	ITEM_COLUMN_QUANTITY 	= 3;
		
		do
		{
			currentPage = this.list(currentPagination);
			
			currentPage.getContent().forEach( currentFlat -> {
				
				Sheet currentFlatSheet = workbook.getSheet(this.getCode(currentFlat));
				
				if(!Objects.isNull(currentFlatSheet)) 
				{
					
					for(
							Integer currentItemRow = START_ROW_NUM; 
							currentItemRow < LAST_ROW_NUM; 
							currentItemRow++
						) 
					{
						Row 	currentRow 			= currentFlatSheet.getRow(currentItemRow);
						
						CellType quantityCellType 	= currentRow.getCell(ITEM_COLUMN_QUANTITY).getCellTypeEnum();
						
						//CASO SEJA NUMÉRICO HAVERÁ UM VALOR
						//CASO CONTRÁRIO HAVERÁ TEXTO
						if( CellType.NUMERIC.equals(quantityCellType)) 
						{
							
							Integer	itemQuantity 		= (int) currentRow.getCell(ITEM_COLUMN_QUANTITY).getNumericCellValue() ;
							String 	itemName 			= currentRow.getCell(ITEM_COLUMN_NAME).getStringCellValue();
							Float   itemValue 			= 0F;
							
							Item item = new Item();
							item.setName(itemName);
							item.setValue(itemValue);
							
							this.setItemToFlat( item, itemQuantity, currentFlat );
							
						}
					}

				}
				
			} );
			
			currentPagination = currentPagination.next();
			
		}while(currentPage.hasNext());
		
	}*/

	private void setItemToFlat(Item item, Integer quantity, Flat flat) 
	{
		
		if(!flat.initiated()) 
		{
			this.init(flat);
		}
		
		FlatItem flatItem = new FlatItem();
		flatItem.setItem(item);
		flatItem.setFlat(flat);
		flatItem.setQuantity(quantity);
		
		this.flatItemDao.save(flatItem);
	}

	public FlatItem addItemToFlat(String code, FlatItem flatItem) throws FlatItemAlreadyExists {

		final Flat flat = this.getFlatByCode(code);

		if(!flat.initiated())
		{
			this.init(flat);
		}

		return this.addItemToFlat(flat, flatItem);
	}

	@Transactional
	public FlatItem addItemToFlat(Flat flat, FlatItem flatItem) throws FlatItemAlreadyExists {

        Item item = flatItem.getItem();

        boolean exists = this.flatItemExists(flat, item);

        if(exists)
		{
			throw new FlatItemAlreadyExists(flat, item);
		}

		this.itemDao.save(item);

		flatItem.setFlat(flat);
		flatItem.setUpToDate(true);

		return this.flatItemDao.save(flatItem);
	}

	private boolean flatItemExists(Flat flat, Item item)
    {
        FlatItem flatItem = new FlatItem(flat, item);
        return this.flatItemExists(flatItem);
    }

	private boolean flatItemExists(FlatItem flatItem) {
        flatItem.setUpToDate(true);

        ExampleMatcher flatExampleMatcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "quantity", "item.id", "item.value");

        Example<FlatItem> flatExample = Example.of(flatItem, flatExampleMatcher);

        boolean exists = this.flatItemDao.exists(flatExample);

        return exists;
    }

    //FIXME: criar mecânica para criar novo e manter itens anteriores
	@Transactional
	public FlatItem updateFlatItem(String code, String name, FlatItem flatItemUpdated) throws FlatItemAlreadyExists {

		Flat flat = this.getFlatByCode(code);

		this.deleteFlatItem(flat, name);

		flatItemUpdated.setId(null);
		flatItemUpdated.getItem().setId(null);

		return this.addItemToFlat( flat, flatItemUpdated );
	}

	@Transactional
	public void deleteFlatItem(Flat flat, String name)
	{
		FlatItem flatItem = this.flatItemDao.findByFlatAndName(flat, name);

		//WARNING: não será feita utilizando o bean, somente id, para ser feita uma query delecão lógica
		this.flatItemDao.delete(flatItem.getId());
	}

	@Transactional
	public void deleteFlatItem(String code, String name) {

		Flat flat = this.getFlatByCode(code);
		this.deleteFlatItem(flat, name);

	}
}
