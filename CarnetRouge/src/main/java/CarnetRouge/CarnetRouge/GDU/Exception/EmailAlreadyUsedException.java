package CarnetRouge.CarnetRouge.GDU.Exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String s) {
        super(s);
    }
}
