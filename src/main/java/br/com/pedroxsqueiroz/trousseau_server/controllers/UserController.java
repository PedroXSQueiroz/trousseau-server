package br.com.pedroxsqueiroz.trousseau_server.controllers;

import br.com.pedroxsqueiroz.trousseau_server.api_utils.ResponseFactory;
import br.com.pedroxsqueiroz.trousseau_server.dtos.UserRegisterDto;
import br.com.pedroxsqueiroz.trousseau_server.models.User;
import br.com.pedroxsqueiroz.trousseau_server.sevices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

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

}
