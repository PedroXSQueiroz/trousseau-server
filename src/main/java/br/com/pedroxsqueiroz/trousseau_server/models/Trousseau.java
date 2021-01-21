package br.com.pedroxsqueiroz.trousseau_server.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.TrousseauFailEnum;
import org.hibernate.annotations.Where;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.TrousseauStatus;
import lombok.Data;

@Entity
@Table(name = "trousseau")
@Data
public class Trousseau {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trousseau_id")
	private Integer id;
	
	@Column(name = "register_date", insertable = false)
	private Date date;
	
	@Where(clause = "product_type='inner'")
	@OneToMany(cascade = CascadeType.ALL, targetEntity = TrousseauItem.class)
	@JoinColumn(name = "trousseau_id")
	private List<TrousseauItem> itens = new ArrayList<TrousseauItem>();
	
	@Where(clause = "product_type='diff'")
	@OneToMany(cascade = CascadeType.ALL, targetEntity = TrousseauItemDiff.class)
	@JoinColumn(name = "trousseau_id")
	private List<TrousseauItemDiff> diff = new ArrayList<TrousseauItemDiff>();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "trousseau_status")
	private TrousseauStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "trousseau_fail")
	private TrousseauFailEnum fail;
	
	@ManyToOne
	@JoinColumn(name = "flat_id")
	private Flat flat;
}
