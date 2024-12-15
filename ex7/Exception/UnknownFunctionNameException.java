package oop.ex6.main.Exception;

public class UnknownFunctionNameException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: Unknown Function Name in line ";

    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public UnknownFunctionNameException(String line) {
        super(line);
    }


    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE + super.getMessage();
    }
}
