package oop.ex6.main.Exception;

public class MissCloseBracketException extends SJavaException {
    private static final String ERROR_MESSAGE = "Exception: '}' is missing at the" +
            " end of the scope, after line ";
            String line;

    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public MissCloseBracketException(String line) {
        super(line);
        this.line = line;
    }

    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return  ERROR_MESSAGE + line;
    }
}
