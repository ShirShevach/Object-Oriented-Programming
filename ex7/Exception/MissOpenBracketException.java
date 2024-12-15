package oop.ex6.main.Exception;

public class MissOpenBracketException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: '{' is missing at the end of the line ";

    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public MissOpenBracketException(String line) {
        super(line);
    }


    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return  ERROR_MESSAGE + super.getMessage();
    }
}