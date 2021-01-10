package br.com.pedroxsqueiroz.trousseau_server.repositories;

import br.com.pedroxsqueiroz.trousseau_server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Modifying
    @Query("UPDATE User u SET u.passwordHash = :passwordHash WHERE u.id = :userId")
    void updatePasswordHash(@Param("userId") Integer userId, @Param("passwordHash") byte[] passwordHash);

}
