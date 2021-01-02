package br.com.pedroxsqueiroz.trousseau_server.config;

import br.com.pedroxsqueiroz.trousseau_server.filters.JWTAuthenticationFilter;
import br.com.pedroxsqueiroz.trousseau_server.filters.JWTAuthorizationFilter;
import br.com.pedroxsqueiroz.trousseau_server.sevices.AuthenticationService;
import br.com.pedroxsqueiroz.trousseau_server.sevices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.Properties;

@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authService;

    @Bean(name = "auth.props")
    public Properties configProperties(
            @Value("${auth.props.user_table}") String userTable,
            @Value("${auth.props.user_table.login_column}") String loginColumn,
            @Value("${auth.props.user_table.password_column}") String passwordColumn,
            @Value("${auth.props.encryption_algorithm_password}") String encryptionAlgorithmPassword,
            @Value("${auth.props.encryption_token}") String encryptionAlgorithmToken)
    {
        Properties configProps = new Properties();

        configProps.setProperty("user_table", userTable);
        configProps.setProperty("login_column", loginColumn);
        configProps.setProperty("password_column", passwordColumn);
        configProps.setProperty("encryption_password", encryptionAlgorithmPassword);
        configProps.setProperty("encryption_token", encryptionAlgorithmToken);

        return configProps;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/auth/", "/user/").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter( this.authService ) )
            .addFilter(new JWTAuthorizationFilter(this.authenticationManager(), this.authService ) );

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/user/");
    }
}
