package exceptions;

public class PasswordLogDoesNotExistException extends Exception {

    public PasswordLogDoesNotExistException() {
        super("A password log with that title does not exist");
    }
}
