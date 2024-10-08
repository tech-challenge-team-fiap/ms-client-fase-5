package br.com.fiap.ms.client.domain.exception.client;

public class InvalidCpfException extends InvalidClientProcessException {
    private static final String tittle = "Invalid CPF";
    private static final String message = "The CPF %s is a invalid number";

    public InvalidCpfException(String cpf) {
        super(tittle);
    }
}
