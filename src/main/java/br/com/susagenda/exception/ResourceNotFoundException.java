package br.com.susagenda.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s não encontrado(a) com ID: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(String.format("%s não encontrado(a) com %s: %s", resourceName, field, value));
    }
}
