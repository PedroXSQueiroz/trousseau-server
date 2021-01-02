package br.com.pedroxsqueiroz.trousseau_server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity()
@Table(name = "trousseau_user")
@Data()
@Builder
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trousseau_user")
    private Integer id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;

    @JsonIgnore
    @Column(name = "user_password_hash")
    private byte[] passwordHash;

}
