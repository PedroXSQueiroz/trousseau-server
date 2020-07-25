package br.com.pedroxsqueiroz.trousseau_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;
import br.com.pedroxsqueiroz.trousseau_server.models.FlatItem;

@Repository
public interface FlatItemDao extends JpaRepository<FlatItem, Integer>, JpaSpecificationExecutor<FlatItem>{

	List<FlatItem> findAllByFlat(Flat flatByCode);

}
