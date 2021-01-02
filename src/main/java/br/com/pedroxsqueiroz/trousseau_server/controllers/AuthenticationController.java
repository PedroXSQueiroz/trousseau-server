package br.com.pedroxsqueiroz.trousseau_server.controllers;

import br.com.pedroxsqueiroz.trousseau_server.api_utils.ResponseFactory;
import br.com.pedroxsqueiroz.trousseau_server.dtos.AuthDto;
import br.com.pedroxsqueiroz.trousseau_server.dtos.TokenDto;
import br.com.pedroxsqueiroz.trousseau_server.sevices.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

//    @PostMapping("/")
//    @ResponseBody
//    public ResponseEntity<?> authenticate(@RequestBody AuthDto auth)
//    {
//        boolean authenticated = this.authService.authenticate(auth.getLogin(), auth.getPassword());
//
//        if(authenticated)
//        {
//            TokenDto token = this.authService.createToken( auth.getLogin() );
//            return new ResponseEntity<>(token, HttpStatus.OK);
//        }
//
//        return ResponseFactory.forbiddenError("Autenticação falhou", "Email ou senha incorretos");
//    }

}
