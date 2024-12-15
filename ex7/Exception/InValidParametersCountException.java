package oop.ex6.main.Exception;

public class InValidParametersCountException extends SJavaException {

    private static final String ERROR_MESSAGE = "Exception: expected %s argument but found %s in line %s";
    String numArgument1;
    String numArgument2;

    /**
     * Constructs an instance of the class
     * @param strings array of the number of arguments in the relevant functions
     */
    public InValidParametersCountException(String[] strings) {
        super(strings[2]);
        numArgument1 = strings[0];
        numArgument2 = strings[1];
    }


    /**
     * Override of the method getMessage from Exception
     * @return the error message of the exception
     */
    @Override
    public String getMessage() {
        return String.format(ERROR_MESSAGE, numArgument1 ,numArgument2 , super.getMessage());
    }
}
