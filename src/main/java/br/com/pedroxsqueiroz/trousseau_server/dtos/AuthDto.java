package br.com.pedroxsqueiroz.trousseau_server.dtos;

import lombok.Data;

@Data
public class AuthDto {

    private String login;

    private String password;

}
