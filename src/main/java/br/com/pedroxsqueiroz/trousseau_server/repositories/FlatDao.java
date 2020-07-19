package br.com.pedroxsqueiroz.trousseau_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.pedroxsqueiroz.trousseau_server.models.Flat;

@Repository
public interface FlatDao extends JpaRepository<Flat, Integer>, JpaSpecificationExecutor<Flat>{

	Flat findByUnityAndFloor(Integer unity, Integer floor);

}
