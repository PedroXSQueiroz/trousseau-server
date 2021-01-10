package br.com.pedroxsqueiroz.trousseau_server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Entity()
@Table(name = "trousseau_user")
@Data()
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "user_password_hash", updatable = false)
    private byte[] passwordHash;

    @Column(name = "user_enabled", insertable = false)
    private Boolean enabled;

}
