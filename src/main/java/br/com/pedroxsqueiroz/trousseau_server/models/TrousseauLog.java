package br.com.pedroxsqueiroz.trousseau_server.models;

import br.com.pedroxsqueiroz.trousseau_server.contants.enums.TrousseauStatus;
import lombok.Data;

import javax.persistence.*;

@Table(name = "trousseau_log")
@Entity()
@PrimaryKeyJoinColumn( name = "trousseau_log_id" )
@AttributeOverride( name = "id", column = @Column(name = "trousseau_log_id" ) )
@Data
public class TrousseauLog extends Log{

    @Column(name = "status_attributed")
    private TrousseauStatus status;

}
