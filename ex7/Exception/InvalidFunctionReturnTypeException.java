package oop.ex6.main.Exception;

public class InvalidFunctionReturnTypeException extends SJavaException {
    private static final String ERROR_MESSAGE = "Exception: Invalid function return type in line ";
    String line;

    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public InvalidFunctionReturnTypeException(String line) {
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
