package oop.ex6.main.Exception;

public class SpaceBeforeCommentException extends SJavaException {
    private String line;

    private static final String ERROR_MESSAGE = "Exception: There should not be spaces before " +
            "the comment at the beginning of line ";

    /**
     * Constructs an instance of the class
     * @param line the line in which there is an exception.
     */
    public SpaceBeforeCommentException(String line) {
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
