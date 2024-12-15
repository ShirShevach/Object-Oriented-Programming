package oop.ex6.main;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternManager {
    private static final String EMPTY_LINES_PATTERN = "\\s";
    private static final String TYPE_NAME_REGEX = "((final +)?(int|double|boolean|String|char))";
    private static final String VARIABLE_REGEX = "([A-Za-z]\\w*|_\\w+)";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile(VARIABLE_REGEX);
    private static final String NAME_METHOD_REGEX = "([A-Za-z]\\w*)";
    private static final String FUNCTIONS_SIGNATURE_REGEX = "[A-Za-z]+ +"
            + NAME_METHOD_REGEX +" *(\\(("+ TYPE_NAME_REGEX +" +"
            + VARIABLE_REGEX +"( *, *"+ TYPE_NAME_REGEX +" +"+ VARIABLE_REGEX +")* *)? *\\)) *\\{";
    private static final String GLOBAL_VARIABLE_DECLARATION_REGEX =
            "^((final +)?(int |double |boolean |String |char ))(.*);$";
    private static final String VARIABLE_OR_VARIABLE_AND_ASSIGNMENT_REGEX =
            "("+ VARIABLE_REGEX +" *(?:=(.*+))?)";
    private static final String INT_BY_STRING = "[-+]?\\d+";
    private static final String DOUBLE_BY_STRING = "[-+]?(\\d+\\.\\d*|\\.\\d+|\\d+)";
    private static final String BOOLEAN_BY_STRING = "(?:true|false)|(?:" + DOUBLE_BY_STRING + ")";
    private static final String VARIABLE_AND_ASSIGNMENT_REGEX = "("+ VARIABLE_REGEX +" *(=(.*+)))";
    private static final String IF_OR_WHILE_REGEX_TEMP = "(?:"+ BOOLEAN_BY_STRING +"|"+ VARIABLE_REGEX +")";
    private static final String IF_OR_WHILE_REGEX = "(?:if|while) \\( *("+
            IF_OR_WHILE_REGEX_TEMP +" *(?:(?:(?:&&|\\|\\|)) *"+ IF_OR_WHILE_REGEX_TEMP +" *)*)\\) *";
    private static final String CALL_FUNCTION_REGEX = "([A-Za-z]\\w*) *\\((.*)\\) *";
    private static final String RETURN_REGEX = "\\s*return\\s*;";
    private static final String TYPES_REGEX = "int|double|boolean|char|String";
    private static final String EMPTY_LINES = "emptyLinesPattern";
    private static final String VARIABLE = "variablePattern";
    private static final String TYPES_PATTERN = "typesPattern";
    HashMap<String, Pattern> patternMap;

    /**
     * The constructor of the PatternManager. init the patternMap.
     */
    public PatternManager() {
        patternMap = new HashMap<>();
        makePatterns();
    }

    /**
     * The function get the name of the pattern and string to match, and return matcher.
     * @param patternName the name of the pattern
     * @param string the string we want to match to the pattern.
     * @return the matcher that we accepted with the string by the pattern.
     */
    public Matcher getMatcher(String patternName, String string) {
        return patternMap.get(patternName).matcher(string);
    }

    /*
    make the pattern and put them in the patternMap with their name.
     */
    private void makePatterns() {
        Pattern emptyLinesPattern = Pattern.compile(EMPTY_LINES_PATTERN);
        patternMap.put(EMPTY_LINES, emptyLinesPattern);
        patternMap.put(VARIABLE, VARIABLE_PATTERN);
        Pattern functionsSignaturePattern = Pattern.compile(FUNCTIONS_SIGNATURE_REGEX);
        patternMap.put(Analyzer.FUNCTIONS_SIGNATURE_PATTERN, functionsSignaturePattern);
        Pattern variableDeclarationPattern = Pattern.compile(GLOBAL_VARIABLE_DECLARATION_REGEX);
        patternMap.put(Analyzer.VARIABLE_DECLARATION_PATTERN, variableDeclarationPattern);
        Pattern variableOrVariableAndAssignmentPattern =
                Pattern.compile(VARIABLE_OR_VARIABLE_AND_ASSIGNMENT_REGEX);
        patternMap.put(Analyzer.VARIABLE_OR_VARIABLE_AND_ASSIGNMENT_PATTERN,
                variableOrVariableAndAssignmentPattern);
        Pattern intPattern = Pattern.compile(INT_BY_STRING);
        patternMap.put(Analyzer.INT_PATTERN, intPattern);
        Pattern doublePattern = Pattern.compile(DOUBLE_BY_STRING);
        patternMap.put(Analyzer.DOUBLE_PATTERN, doublePattern);
        Pattern booleanPattern = Pattern.compile(BOOLEAN_BY_STRING);
        patternMap.put(Analyzer.BOOLEAN_PATTERN, booleanPattern);
        Pattern variableAndAssignmentPattern = Pattern.compile(VARIABLE_AND_ASSIGNMENT_REGEX);
        patternMap.put(Analyzer.VARIABLE_AND_ASSIGNMENT_PATTERN, variableAndAssignmentPattern);
        Pattern ifOrWhilePattern = Pattern.compile(IF_OR_WHILE_REGEX);
        patternMap.put(Analyzer.IF_OR_WHILE_PATTERN, ifOrWhilePattern);
        Pattern callFunctionPattern = Pattern.compile(CALL_FUNCTION_REGEX);
        patternMap.put(Analyzer.CALL_FUNCTION_PATTERN, callFunctionPattern);
        Pattern returnPattern = Pattern.compile(RETURN_REGEX);
        patternMap.put(Analyzer.RETURN_PATTERN, returnPattern);
        Pattern typesPattern = Pattern.compile(TYPES_REGEX);
        patternMap.put(TYPES_PATTERN, typesPattern);
    }

}
