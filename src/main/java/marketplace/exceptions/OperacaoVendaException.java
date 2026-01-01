package marketplace.exceptions;

public class OperacaoVendaException extends RuntimeException {
    public OperacaoVendaException(String message) {
        super(message);
    }

    public OperacaoVendaException(String message, Throwable causa) {
        super(message, causa);
    }
}
