package br.com.pedroxsqueiroz.trousseau_server.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "flat_item")
@Data
public class FlatItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "flat_item_id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "flat_id")
	private Flat flat;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@Column(name = "quantity")
	private Integer quantity;
	
}
