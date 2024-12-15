package oop.ex6.main.Exception;

public class DuplicateNameFunctionException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: Duplicate Name Function '%s' in line %s";
    private String line;

    /**
     * Constructs an instance of the class
     * @param string the line in which there is an exception.
     */
    public DuplicateNameFunctionException(String string, String line) {
        super(string);
        this.line = line;
    }


    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return String.format(ERROR_MESSAGE, super.getMessage(), line);
    }
}
