package br.com.pedroxsqueiroz.trousseau_server.exceptions;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;

public class FlatItemAlreadyExists extends Exception {

    public FlatItemAlreadyExists(FlatItem flatItem)
    {
        this(flatItem.getFlat(), flatItem.getItem());
    }

    public FlatItemAlreadyExists(Flat flat, Item item)
    {
        super( String.format("Flat %s já possui o item %s", Flat.buildCodeFromFlat( flat ), item.getName() ) );
    }

}
