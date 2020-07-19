package br.com.pedroxsqueiroz.trousseau_server.models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "inner")
public class TrousseauItem extends AbstractTrousseauItem {
	
}
