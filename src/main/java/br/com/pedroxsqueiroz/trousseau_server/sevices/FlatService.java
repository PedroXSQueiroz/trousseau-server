package br.com.pedroxsqueiroz.trousseau_server.sevices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hpsf.Array;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;
import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;
import br.com.pedroxsqueiroz.trousseau_server.repositories.FlatDao;
import br.com.pedroxsqueiroz.trousseau_server.repositories.FlatItemDao;
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
	
	private Flat init(Flat flat) 
	{
		return this.dao.save(flat);
	}

	public List<FlatItem> getItensFromFlat(String code) 
	{
		Flat flat = this.getFlatByCode(code);
		
		if(!flat.initiated()) 
		{
			return new ArrayList<FlatItem>();
		}
		
		return this.flatItemDao.findAllByFlat(flat);
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

	public void loadItensToFlats(InputStream trousseausFlatsSpreedsheetInputSream) throws EncryptedDocumentException, IOException {
		
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
		
	}

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

}
