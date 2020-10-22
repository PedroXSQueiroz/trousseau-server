package br.com.pedroxsqueiroz.trousseau_server.models;

import java.beans.Transient;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "flat")
@Data
public class Flat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "flat_id")
	private Integer id;
	
	@Column(name = "floor")
	private Integer floor;
	
	@Column(name = "unity")
	private Integer unity;

	@OneToMany
	@JoinColumn(name = "flat_id")
	@Where(clause="up_to_date=true")
	private List<FlatItem> itens;

	@Transient
	public boolean initiated() 
	{
		return !Objects.isNull(this.id);
	}

	public static String buildCodeFromUnityAndFloor(Integer unity, Integer floor) {
		return String.format( "%s%s", floor, StringUtils.leftPad(unity.toString(), 2, "0")  );
	}

	public static String buildCodeFromFlat(Flat flat) {
		return buildCodeFromUnityAndFloor( flat.getUnity(), flat.getFloor() );
	}
	
}
