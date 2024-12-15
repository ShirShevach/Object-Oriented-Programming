package oop.ex6.main.Exception;

public class InvalidVariableDeclarationException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: Invalid variable declaration in line ";

    /**
     * Constructs an instance of the class
     * @param string the line in which there is an exception.
     */
    public InvalidVariableDeclarationException(String string) {
        super(string);
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
