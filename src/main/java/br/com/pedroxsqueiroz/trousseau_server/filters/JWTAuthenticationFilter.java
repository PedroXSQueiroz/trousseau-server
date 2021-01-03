package br.com.pedroxsqueiroz.trousseau_server.filters;

import br.com.pedroxsqueiroz.trousseau_server.dtos.AuthDto;
import br.com.pedroxsqueiroz.trousseau_server.dtos.Credentials;
import br.com.pedroxsqueiroz.trousseau_server.dtos.TokenDto;
import br.com.pedroxsqueiroz.trousseau_server.sevices.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationService authService;

    static UsernamePasswordAuthenticationToken EMPTY_AUTH = new UsernamePasswordAuthenticationToken(null, null);

    public JWTAuthenticationFilter(AuthenticationService authService)
    {
        this.authService = authService;
        setFilterProcessesUrl("/auth/");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            ObjectMapper serializer = new ObjectMapper();

            ServletInputStream requestInput = request.getInputStream();

            AuthDto auth = serializer.readValue(requestInput, AuthDto.class);

            String login = auth.getLogin();
            boolean authenticated = this.authService.authenticate(login, auth.getPassword());

            if(authenticated)
            {
                TokenDto token = this.authService.createToken(login);
                Credentials credentials = this.authService.getCredentials(login, token.getToken());

                return new UsernamePasswordAuthenticationToken(login, credentials);
            }

            throw new AuthenticationCredentialsNotFoundException("Login or password not found");

        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException,
            ServletException {

        ObjectMapper serializer = new ObjectMapper();

        ObjectNode authResponse = serializer.createObjectNode();
        Credentials credentials = (Credentials) authResult.getCredentials();
        authResponse.put("token", credentials.getToken() );

        byte[] responseBytes = serializer.writer().writeValueAsBytes(authResponse);

        response.setContentType("application/json");

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(responseBytes);

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        ObjectMapper serializer = new ObjectMapper();

        ObjectNode responseJson = serializer.createObjectNode();
        responseJson.put("error", failed.getMessage());

        byte[] responseBytes = serializer.writer().writeValueAsBytes(responseJson);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(responseBytes);
        outputStream.flush();
    }

}
