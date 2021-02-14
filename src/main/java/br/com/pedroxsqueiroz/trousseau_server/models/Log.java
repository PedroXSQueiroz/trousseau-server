package br.com.pedroxsqueiroz.trousseau_server.models;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.LogType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log")
@Data
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_trousseau_user")
    private User user;

    @Column(name = "message")
    private String message;

    @Enumerated( EnumType.STRING )
    @Column(name = "log_type")
    private LogType type;

    @Column(name = "register_date")
    private Date registerDate;

}
