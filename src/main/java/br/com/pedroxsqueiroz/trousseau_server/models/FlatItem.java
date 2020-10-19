package br.com.pedroxsqueiroz.trousseau_server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;

@Entity
@Table(name = "flat_item")
@Data
public class FlatItem {

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
