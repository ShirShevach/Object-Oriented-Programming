package oop.ex6.main.Exception;

public class UnInitializedVariableException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: Uninitialized variable in line ";

    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public UnInitializedVariableException(String line) {
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
