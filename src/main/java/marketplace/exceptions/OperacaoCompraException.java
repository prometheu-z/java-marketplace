package marketplace.exceptions;

public class OperacaoCompraException extends RuntimeException {
    public OperacaoCompraException(String message, Throwable causa) {
        super(message, causa);
    }
}
