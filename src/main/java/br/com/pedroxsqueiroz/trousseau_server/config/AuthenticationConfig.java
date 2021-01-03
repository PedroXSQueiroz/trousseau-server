package br.com.pedroxsqueiroz.trousseau_server.config;

import br.com.pedroxsqueiroz.trousseau_server.filters.JWTAuthenticationFilter;
import br.com.pedroxsqueiroz.trousseau_server.filters.JWTAuthorizationFilter;
import br.com.pedroxsqueiroz.trousseau_server.sevices.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
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
            .csrf().disable()
            .cors().configurationSource(this.corsConfigurationSource())
            .and()
            .authorizeRequests()
            .antMatchers("/auth/", "/user/").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter( new JWTAuthenticationFilter( this.authService ) )
            .addFilter( new JWTAuthorizationFilter( this.authenticationManager(), this.authService ) );

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/user/");
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        //the below three lines will add the relevant CORS response headers
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
