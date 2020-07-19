package br.com.pedroxsqueiroz.trousseau_server.models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "diff")
public class TrousseauItemDiff extends AbstractTrousseauItem{

}
