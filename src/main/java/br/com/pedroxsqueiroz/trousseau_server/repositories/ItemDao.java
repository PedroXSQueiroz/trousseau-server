package br.com.pedroxsqueiroz.trousseau_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.pedroxsqueiroz.trousseau_server.models.Item;

@Repository
public interface ItemDao extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {

}
