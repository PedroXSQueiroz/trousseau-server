package br.com.pedroxsqueiroz.trousseau_server.models;

import java.beans.Transient;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "flat")
@Data
public class Flat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "flat_id")
	private Integer id;
	
	@Column(name = "floor")
	private Integer floor;
	
	@Column(name = "unity")
	private Integer unity;
	
	@Transient
	public boolean initiated() 
	{
		return !Objects.isNull(this.id);
	}
	
}
