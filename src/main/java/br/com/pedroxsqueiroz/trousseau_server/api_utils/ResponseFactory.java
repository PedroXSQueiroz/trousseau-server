package br.com.pedroxsqueiroz.trousseau_server.api_utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    public static ResponseEntity<?> badRequestError(String error, String message)
    {
        return error(error, message, HttpStatus.BAD_REQUEST );
    }

    public static ResponseEntity<?> internalError(String error, String message)
    {
        return error(error, message, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public static ResponseEntity<?> forbiddenError(String error, String message)
    {
        return error(error, message, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<?> error(String error, String message, HttpStatus status)
    {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode errorResponse = mapper.createObjectNode();

        errorResponse.put("error", error);
        errorResponse.put("message", message);

        return new ResponseEntity<ObjectNode>(errorResponse, status);
    }
}
