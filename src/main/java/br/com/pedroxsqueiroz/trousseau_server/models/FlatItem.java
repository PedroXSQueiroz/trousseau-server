package br.com.pedroxsqueiroz.trousseau_server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "flat_item")
@Data
public class FlatItem {

    public FlatItem()
    {

    }

    public FlatItem(Flat flat, Item item)
    {
        this.flat = flat;
        this.item = item;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flat_item_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "flat_id")
    @JsonIgnore
    private Flat flat;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "up_to_date")
    private Boolean upToDate;

}
