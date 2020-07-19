package br.com.pedroxsqueiroz.trousseau_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.pedroxsqueiroz.trousseau_server.models.Trousseau;

@Repository
public interface TrousseauDao extends JpaRepository<Trousseau, Integer>, JpaSpecificationExecutor<Trousseau> {

}
