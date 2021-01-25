package br.com.pedroxsqueiroz.trousseau_server.repositories;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;
import br.com.pedroxsqueiroz.trousseau_server.models.Item;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;

@Repository
public interface FlatItemDao extends JpaRepository<FlatItem, Integer>, JpaSpecificationExecutor<FlatItem> {

    @Modifying
    @Query("update FlatItem fi set up_to_date=false where fi.id = :id")
    void delete(@Param("id") Integer id);

    List<FlatItem> findByFlat(Flat flatByCode);

    @Query("from FlatItem fi where fi.flat = :flat and fi.item.name = :name and fi.upToDate = true")
    FlatItem findByFlatAndName(@Param("flat") Flat flat, @Param("name") String name);

    List<FlatItem> findByFlatAndUpToDate(Flat flat, boolean b);

}
