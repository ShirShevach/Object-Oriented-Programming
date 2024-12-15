package oop.ex6.main.Exception;

public class ReturnVoidException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: Function '%s' should return " +
            "void at the end of the function";

    /**
     * Constructs an instance of the class
     * @param nameFunction the name of the function in which there is an exception.
     */
    public ReturnVoidException(String nameFunction) {
        super(nameFunction);
    }


    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return  String.format(ERROR_MESSAGE , super.getMessage());
    }
}
