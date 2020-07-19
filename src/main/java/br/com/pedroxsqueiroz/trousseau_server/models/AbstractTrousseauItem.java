package br.com.pedroxsqueiroz.trousseau_server.models;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "trousseau_item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(	name="product_type", 
						discriminatorType = DiscriminatorType.STRING)
@Data
public abstract class AbstractTrousseauItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "trousseau_item_id")
	private Integer id;
	
	
	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Override
	public int hashCode() 
	{
		return this.item.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof TrousseauItem) 
		{
			TrousseauItem otherTrousseau = (TrousseauItem) obj;
			
			Item otherItem = otherTrousseau.getItem();
			
			return this.getItem().equals( otherItem ); 
			
		}
		
		return false;
	}
	
}
