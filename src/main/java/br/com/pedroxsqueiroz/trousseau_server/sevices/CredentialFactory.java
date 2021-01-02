package br.com.pedroxsqueiroz.trousseau_server.sevices;

import br.com.pedroxsqueiroz.trousseau_server.dtos.Credentials;

import javax.servlet.http.HttpServletRequest;

public interface CredentialFactory {

    Credentials buildCredential(String login, String token);

}
