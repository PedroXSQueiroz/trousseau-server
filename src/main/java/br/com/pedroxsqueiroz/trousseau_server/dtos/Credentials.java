package br.com.pedroxsqueiroz.trousseau_server.dtos;

public class Credentials<T> {

    private T data;

    private String token;

    public Credentials(String token,T data)
    {
        this.data = data;
        this.token = token;
    }

    public T getAuthenticationData()
    {
        return data;
    }

    public String getToken()
    {
        return this.token;
    }

}
