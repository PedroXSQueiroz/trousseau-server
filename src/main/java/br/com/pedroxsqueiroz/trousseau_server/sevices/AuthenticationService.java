package br.com.pedroxsqueiroz.trousseau_server.sevices;

import br.com.pedroxsqueiroz.trousseau_server.dtos.Credentials;
import br.com.pedroxsqueiroz.trousseau_server.dtos.TokenDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

@Service
public class AuthenticationService {

    private CredentialFactory credentialFactory;

    public AuthenticationService( @Autowired(required = false) CredentialFactory credentialFactory  ){

        this.credentialFactory = credentialFactory != null ? credentialFactory : this::defaultCredentialFactory;

    }

    public Credentials<?> defaultCredentialFactory(String login, String token){

        return new Credentials<String>(token, login);
    }

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    @Qualifier("auth.props")
    private Properties authProperties;

    public boolean authenticate(String login, String password)
    {

        byte[] encryptedPassword = DigestUtils.sha256Hex(password).getBytes(StandardCharsets.UTF_8);


        String authQuery = new ST(  "SELECT <login_column>, <password_column> "
                                            + "FROM <table> "
                                            + "WHERE <login_column> = ? "
                                            + "AND <password_column> = ?"
                        )
                        .add("login_column", this.authProperties.getProperty("login_column"))
                        .add("password_column", this.authProperties.getProperty("password_column"))
                        .add("table", this.authProperties.getProperty("user_table"))
                        .render();

        Boolean credentialsMatch = DataAccessUtils.singleResult(this.jdbc.query(
                    authQuery,
                new Object[] {login, encryptedPassword},
                (resultSet, i) -> resultSet.isFirst() && resultSet.isLast()
            )
        );

        return Optional.ofNullable(credentialsMatch).orElse(false);
    }

    public byte[] encryptPassword (String password)
    {
        return DigestUtils.sha256Hex(password).getBytes(StandardCharsets.UTF_8);
    }

    public TokenDto createToken(String login)
    {
        String token = JWT.create().withSubject(login).sign(Algorithm.HMAC256("DWS8plhPZw") );

        TokenDto dto = new TokenDto();
        dto.setToken(token);

        return dto;
    }

    public String decreptyToken(String token)
    {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("DWS8plhPZw")).build();
        return verifier.verify(token).getSubject();
    }

    public Credentials getCredentials(String login, String token) {

        return this.credentialFactory.buildCredential(login, token);

    }
}
