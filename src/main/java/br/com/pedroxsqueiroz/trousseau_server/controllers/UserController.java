package br.com.pedroxsqueiroz.trousseau_server.controllers;

import br.com.pedroxsqueiroz.trousseau_server.api_utils.ResponseFactory;
import br.com.pedroxsqueiroz.trousseau_server.dtos.UserDataDto;
import br.com.pedroxsqueiroz.trousseau_server.dtos.UserRegisterDto;
import br.com.pedroxsqueiroz.trousseau_server.dtos.UserUpdatePasswordDto;
import br.com.pedroxsqueiroz.trousseau_server.models.User;
import br.com.pedroxsqueiroz.trousseau_server.sevices.AuthenticationService;
import br.com.pedroxsqueiroz.trousseau_server.sevices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController()
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto)
    {
        Boolean alreadyExists = this.userService.alreadyExists(dto.getEmail(), dto.getName());
        if(alreadyExists)
        {
            return ResponseFactory.badRequestError("Usuário já existe", "Nome ou email de usuário já foi cadastrado");
        }

        return new ResponseEntity<User>( this.userService.register(dto), HttpStatus.OK );
    }

    @GetMapping("/logged/")
    @ResponseBody
    public ResponseEntity<?> getLoggedUser(@RequestHeader("Authorization") String authHeader)
    {
        User user = getUserByAuthorizationHeader(authHeader);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private User getUserByAuthorizationHeader(@RequestHeader("Authorization") String authHeader) {
        String[] headerParts = authHeader.split("Bearer");

        String token = headerParts[1].trim();

        String login = this.authenticationService.decreptyToken(token);

        return this.userService.getByLogin(login);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestBody UserDataDto user, @PathVariable("id") Integer id)
    {
        User updatedUser = this.userService.update(id, user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/password")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> updatePasswordUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody() UserUpdatePasswordDto newPasswordDto)
    {
        User user = getUserByAuthorizationHeader(authHeader);

        this.userService.updatePassword( user, newPasswordDto.getNewPassword() );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/")
    @Transactional
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader)
    {
        User user = this.getUserByAuthorizationHeader(authHeader);

        this.userService.delete(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
