package br.com.pedroxsqueiroz.trousseau_server.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "item")
@Data
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Integer id;
	
	@Column(name = "item_name")
	private String name;
	
	@Column(name = "item_value")
	private Float value;
	
	@Override
	public int hashCode() 
	{
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		
		if(obj instanceof Item) 
		{
			Item otherItem = (Item) obj;
			
			return this.getId() == otherItem.getId();
		}
		
		return false;
	}
	
}
