package br.com.pedroxsqueiroz.trousseau_server.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;

@Component
public class AuthenticationClauseEnabledUsers implements AuthenticationUserQueryCustomClause{

    @Override
    public String getCustomClause(String login) {
        return "user_enabled = true";
    }

}
