package br.com.fiap.ms.client.domain.exception.client;

public class InvalidPhoneNumberException extends InvalidClientProcessException{

    private static final String tittle = "Invalid phone number";
    private static final String message = "The phone number %s is invalid";

    public InvalidPhoneNumberException(String phone) {
        super(tittle);
    }
}
