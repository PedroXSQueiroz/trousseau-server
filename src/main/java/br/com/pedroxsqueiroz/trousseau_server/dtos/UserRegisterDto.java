package br.com.pedroxsqueiroz.trousseau_server.dtos;

import lombok.Data;

@Data
public class UserRegisterDto {

    private String name;

    private String email;

    private String password;

}
