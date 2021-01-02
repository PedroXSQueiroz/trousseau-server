package br.com.pedroxsqueiroz.trousseau_server.sevices;

import br.com.pedroxsqueiroz.trousseau_server.dtos.UserRegisterDto;
import br.com.pedroxsqueiroz.trousseau_server.models.User;
import br.com.pedroxsqueiroz.trousseau_server.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

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
}
