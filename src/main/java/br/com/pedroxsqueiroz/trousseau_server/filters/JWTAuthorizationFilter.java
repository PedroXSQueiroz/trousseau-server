package br.com.pedroxsqueiroz.trousseau_server.filters;

import br.com.pedroxsqueiroz.trousseau_server.dtos.Credentials;
import br.com.pedroxsqueiroz.trousseau_server.sevices.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthenticationService authService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  AuthenticationService authService) {
        super(authenticationManager);

        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");

        if( Objects.isNull( authorizationHeader ) )
        {
            this.error(response, "Authorization header is missing");
        }
        else
        {
            String[] authHeaderParts = authorizationHeader.split("Bearer");

            if(authHeaderParts.length == 2)
            {
                String token = authHeaderParts[1].trim();
                String login = this.authService.decreptyToken(token);

                Credentials credential = this.authService.getCredentials(login, token);

                if(Objects.nonNull( credential ) )
                {
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(login, credential, Collections.emptyList())
                            );

                    chain.doFilter(request, response);
                }
                else
                {
                    error(response, "Authorization invalid");
                }

            }
            else
            {
                this.error(response, "Authorization header is malformed");
            }
        }

    }

    private void error(HttpServletResponse response, String message) throws IOException {
        ObjectMapper unathorizedResponseMapper = new ObjectMapper();

        ObjectNode unathorizedResponse = unathorizedResponseMapper.createObjectNode();

        unathorizedResponse.put("error", message);

        byte[] unathorizedResponseBytes = unathorizedResponseMapper.writer().writeValueAsBytes(unathorizedResponse);

        response.setContentType("application/json");
        response.setStatus(403);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(unathorizedResponseBytes);
    }


}
