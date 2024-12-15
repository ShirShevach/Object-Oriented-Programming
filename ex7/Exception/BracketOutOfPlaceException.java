package oop.ex6.main.Exception;

public class BracketOutOfPlaceException extends SJavaException {
    private static final String ERROR_MESSAGE = "Exception: '}' out of place line ";
            String line;
    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public BracketOutOfPlaceException(String line) {
        super(line);
        this.line = line;
    }


    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE + line;
    }
}