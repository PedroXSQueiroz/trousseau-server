package br.com.pedroxsqueiroz.trousseau_server.sevices;

import br.com.pedroxsqueiroz.trousseau_server.dtos.UserDataDto;
import br.com.pedroxsqueiroz.trousseau_server.dtos.UserRegisterDto;
import br.com.pedroxsqueiroz.trousseau_server.models.User;
import br.com.pedroxsqueiroz.trousseau_server.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private UserDao dao;

    public User register(UserRegisterDto dto)
    {

        String email = dto.getEmail();
        String name = dto.getName();

        byte[] encryptedPassword = this.authService.encryptPassword(dto.getPassword());

        User newUser = User
                .builder()
                .email(email)
                .name(name)
                .passwordHash(encryptedPassword)
                .build();

        return this.dao.save(newUser);

    }

    public Boolean alreadyExists(String email, String name) {

        Boolean alreadyExists = this.dao.count( (root, query, cb) ->
                    cb.or(
                        cb.equal(root.get("name"), name),
                        cb.equal( root.get("email"), email)
                    )
        ) >= 1;

        return alreadyExists;
    }

    public User get(Integer id) {

        return this.dao.getOne(id);

    }

    public User getByLogin(String login) {
        return this.dao.findOne( (root, query , cb) ->
             cb.equal( root.get("email"), login )
        ).get();
    }

    public User update(Integer id, UserDataDto userData) {

        User user = this.dao.getOne(id);

        user.setEmail(userData.getEmail());
        user.setName(userData.getName());

        return this.dao.save(user);
    }

    public void updatePassword( User user, String newPassword)
    {
        byte[] passwordHash = this.authService.encryptPassword(newPassword);

        this.dao.updatePasswordHash(user.getId(), passwordHash);
    }

    public void delete(User user) {
        user.setEnabled(false);
        this.dao.save(user);
    }

    public User getLoggedUser()
    {
        Optional<HttpServletRequest> currentRequestGetter =  Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
                .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
                .map(ServletRequestAttributes::getRequest);

        HttpServletRequest request;
        if( Objects.nonNull( request = currentRequestGetter.get() ) )
        {

            String authorizationHeader = request.getHeader("Authorization");

            String login = this.authService.decreptyToken(authorizationHeader);

            User user = this.getByLogin(login);

            return user;

        }

        return null;
    }
}
