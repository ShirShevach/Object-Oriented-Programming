package oop.ex6.main;

import oop.ex6.main.Exception.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Analyzer {
    public static final String CALL_FUNCTION_PATTERN = "callFunctionPattern";
    public static final String IF_OR_WHILE_PATTERN = "ifOrWhilePattern";
    public static final String RETURN_PATTERN = "returnPattern";
    public static final String BOOLEAN_PATTERN = "booleanPattern";
    public static final String FUNCTIONS_SIGNATURE_PATTERN = "functionsSignaturePattern";
    public static final String VARIABLE_DECLARATION_PATTERN = "variableDeclarationPattern";
    public static final String VARIABLE_AND_ASSIGNMENT_PATTERN = "variableAndAssignmentPattern";
    public static final String VARIABLE_OR_VARIABLE_AND_ASSIGNMENT_PATTERN =
            "variableOrVariableAndAssignmentPattern";
    public static final String INT_PATTERN = "intPattern";
    public static final String DOUBLE_PATTERN = "doublePattern";

    private static final int TYPE_INDEX = 0;
    private static final int FINAL_INDEX = 1;
    private static final int INIT_INDEX = 2;
    private static final int NOT_FOUND = -1;
    private static final int IN_GLOBAL_MAP = -2;
    private static final String CLOSE_BRACKET = "}";
    private static final String COMMENT = "//";
    private static final String WHILE = "while";
    private static final String IF = "if";
    private static final String OPEN_BRACKET = "{";
    private static final String RETURN = "return";
    private static final String COMA = ",";
    private static final String EMPTY_STRING = "";
    private static final String ZERO_IN_STRING = "0";
    private static final String OR_AND_SPLIT = "&&|\\|\\|";
    private static final String VOID = "void";
    private static final String SEMI_COLON = ";";
    private static final String INIT = "init";
    private static final String STRING = "String ";
    private static final String INT = "int ";
    private static final String DOUBLE = "double ";
    private static final String BOOLEAN = "boolean ";
    private static final String CHAR = "char ";
    private static final String BACK_SLASH = "\"";
    private static final String QUOTATION_MARK = "'";
    private static final char CLOSE_BRACKET_IN_CHAR = '}';
    private static final char OPEN_BRACKET_IN_CHAR = '{';
    private static final int GROUP_OF_NAME = 1;
    private static final int GROUP_OF_PARAMETERS = 2;
    private static final int LENGTH_OF_CHAR = 3;


    private static ArrayList<String> code;
    private static HashMap<String, Function> functionsMap;
    private final HashMap<String, String[]> globalVariableMap; // {name : [type, final, isInit}
    private final PatternManager patternManager;

    /**
     * the constructor of the analyzer.
     * @param code the code, divided line by line in the data structure ArrayList<String>.
     * @throws SJavaException Throws all the errors in the case the code is not in SJava language.
     */
    public Analyzer(ArrayList<String> code) throws SJavaException {
        this.code = code;
        functionsMap = new HashMap<>();
        globalVariableMap = new HashMap<>();
        patternManager = new PatternManager();
        firstTraversal();
        functionTraversal();
    }

    /*
    The function goes through the functionMap
    and checks function by function if it is valid
     */
    private void functionTraversal() throws SJavaException {
        for (String functionName : functionsMap.keySet()) {
            checkLegalFunction(functionName);
        }
    }

    /*
    The function checks every line of a function from
     the file, and returns if it is a valid line. If not
     throws an error.
     */
    private void checkLegalFunction(String functionName) throws SJavaException {
        Function function = functionsMap.get(functionName);
        int i = function.getStart()+1;
        int scopeLevel = 0;

        while (i < function.getEnd()) {
            if (code.get(i) == null) {
                i++;
                continue;
            }
            String line = String.valueOf(i+1);
            String currLine = code.get(i).trim();
            String newLine = currLine.substring(0, currLine.length() - 1).trim();
            Matcher callFunctionMatcher = patternManager.getMatcher
                    (CALL_FUNCTION_PATTERN, newLine);
            if (currLine.equals(CLOSE_BRACKET)) {
                if (scopeLevel > 0) {
                    function.clearHashMap(scopeLevel);
                    scopeLevel -= 1;
                }
            }
            else if(currLine.startsWith(COMMENT)){
                throw new SpaceBeforeCommentException(line);
            }
            else if (currLine.startsWith(WHILE) || currLine.startsWith(IF)) {
                if (!currLine.endsWith(OPEN_BRACKET)) {
                    throw new MissOpenBracketException(line);
                }
                newLine = currLine.substring(0, currLine.length()-1).trim();
                Matcher matcher = patternManager.getMatcher(IF_OR_WHILE_PATTERN, newLine);
                if (matcher.matches()) {
                    checkCondition(matcher.group(1), function, line);
                    scopeLevel += 1;
                } else {
                    throw new IfWhileConditionException(line);
                }

            }
            else if (callFunctionMatcher.matches()) {
                String nameVariable = callFunctionMatcher.group(1);
                String parameters = callFunctionMatcher.group(2).trim();
                handleFunctionCall(nameVariable, parameters, line, function);
            }
            else if (newLine.equals(RETURN)) {
                i++;
                continue;
            }
            else {
                handleVariable(i, currLine, line, false, function, scopeLevel);
            }
            i++;
        }
        int j = function.getEnd()-1;
        while (j > function.getStart()) {
            String currLine = code.get(j);
            if (currLine == null ) {
                j--;
            }
            else if (!patternManager.getMatcher(RETURN_PATTERN, currLine).matches()) {
                throw new ReturnVoidException(function.getName());
            } else {
                break;
            }
        }
    }

    /*
    the function handle the case of calling to another function in function.
     */
    private void handleFunctionCall(String nameVariable, String parameters, String line, Function function)
            throws SJavaException {
        if (functionsMap.containsKey(nameVariable)) {
            Function callFunction = functionsMap.get(nameVariable);
            String[] parametersList = parameters.split(COMA);
            checkValidParametersCall(callFunction.getTypeArgumentOrder(), parametersList, line, function);
        } else {
            throw new UnknownFunctionNameException(line);
        }
    }

    /*
     the function check if the calling to the function is legal (legal type, count
     of variable...)
     */
    private void checkValidParametersCall(ArrayList<String> typeArgumentOrder, String[] parametersList,
                                          String line, Function function) throws SJavaException {
        if (typeArgumentOrder == null && parametersList.length == 0) {
            return;
        }
        if (typeArgumentOrder.size() > 0 && parametersList.length== 1 &&
                parametersList[0].equals(EMPTY_STRING)) {
            throw new InValidParametersCountException
                    (new String[]{String.valueOf(typeArgumentOrder.size()), ZERO_IN_STRING, line});
        }
        if (typeArgumentOrder.size() != parametersList.length) {
            throw new InValidParametersCountException
                    (new String[]{String.valueOf(typeArgumentOrder.size()),
                            String.valueOf(parametersList.length), line});
        }
        for (int i = 0; i < typeArgumentOrder.size(); i++) {
            String parameter = parametersList[i].trim();
            analyzeAssignment(typeArgumentOrder.get(i), parameter, line, function);
        }
    }

    /*
     the function check if the line of condition - while or if, is legal.
     */
    private void checkCondition(String cond, Function function, String line) throws SJavaException {
        String[] conditions = cond.split(OR_AND_SPLIT);
        for (String condition : conditions) {
            condition = condition.trim();
            Matcher booleanMatcher = patternManager.getMatcher(BOOLEAN_PATTERN, condition);
            if (!booleanMatcher.matches()) {
                int scope = findVariable(function, condition);
                if (scope == IN_GLOBAL_MAP) {
                    checkInitializedVariable(globalVariableMap.get(condition)[INIT_INDEX], line);
                }
                else if (scope >= 0) {
                    checkInitializedVariable(function.getPropertiesOfVariable
                            (scope, condition)[INIT_INDEX], line);
                }
                else {
                     throw new UnknownVariableException(line);
                }
            }
        }
    }

    /*
    the function travel for the first time on the file,
    it save the global variable and the location of the function.
     */

    private void firstTraversal() throws SJavaException {
        int i = 0;
        while (i < code.size()) {
            if (code.get(i) == null) {
                i++;
                continue;
            }
            String currLine = code.get(i).trim();
            String line = String.valueOf(i+1);
            Matcher functionsSignatureMatcher = patternManager.getMatcher
                    (FUNCTIONS_SIGNATURE_PATTERN, currLine);
            boolean matchFunction = functionsSignatureMatcher.matches();
            boolean matchVoid = currLine.startsWith(VOID);
            if ( matchFunction &&  matchVoid) {
                if (!currLine.endsWith(OPEN_BRACKET)) {
                    throw new MissOpenBracketException(line);
                }
                String name = functionsSignatureMatcher.group(GROUP_OF_NAME);
                String parameters = functionsSignatureMatcher.group(GROUP_OF_PARAMETERS);
                int endIndex = saveMethod(i, currLine, name, parameters);
                i = endIndex + 1;
            } else if (matchFunction && !matchVoid){
                throw new InvalidFunctionReturnTypeException(line);
            }else if (matchVoid &&!matchFunction){
                throw new FunctionSignatureException(String.valueOf(i+1));
            }
            else if (currLine.startsWith(COMMENT)){
                throw new SpaceBeforeCommentException(line);
            }
            else  {
                handleVariable(i, currLine, line, true, null, -1);
                i++;
            }
        }
    }

    /*
    The function handles cases where we suspect that
    the next line is a variable declaration
     */
    private void handleVariable(int i, String currLine, String line, boolean isGlobal,
                                   Function function, int currScope) throws SJavaException {
        Matcher variableMatcher = patternManager.getMatcher
                (VARIABLE_DECLARATION_PATTERN, currLine);
        if (variableMatcher.matches()) {
            String isFinal = variableMatcher.group(2);
            String type = variableMatcher.group(3);
            String nonType = variableMatcher.group(4);
            saveVariable(i, nonType, isFinal, type, isGlobal, function, currScope);
        }  else if (currLine.endsWith(SEMI_COLON)) {
            handleAssignment(currLine, line, function);
        } else {
            throw new MissSemiColonException(line);
        }
    }

    /*
    The function handles cases where the line is
     an assignment to variables (values or other variables)
     */
    private void handleAssignment(String currLine, String line, Function function) throws SJavaException {
        currLine = currLine.substring(0, currLine.length()-1);
        String[] expressions = currLine.split(COMA);
        for (String expression : expressions) {
            Matcher matcher = patternManager.getMatcher(VARIABLE_AND_ASSIGNMENT_PATTERN,
                    expression);
            if (!matcher.matches()) {
                throw new UnexpectedLineExpressionException(line);
            }
            String variableName = matcher.group(2);
            String[] properties = null;
            int scopeIndex = -1;
            if (function!=null) {
                scopeIndex = function.contains(variableName);
                if (scopeIndex == -1) {
                    properties = findGlobalVariableAtMap(line, variableName);
                    scopeIndex = 0;
                } else {
                    properties = function.getPropertiesOfVariable(scopeIndex, variableName);
                }
            } else {
                properties = findGlobalVariableAtMap(line, variableName);
            }
            analyzeAssignment(properties[TYPE_INDEX], matcher.group(4).trim(), line, function);
            if (updateVariable(line, function, variableName, properties, scopeIndex)) return;
        }
    }

    /*
    The function handles cases where there is an update to the variable.
    and checks whether it is legal or not.
     */
    private boolean updateVariable(String line, Function function, String variableName,
                                   String[] properties, int scopeIndex)
            throws AssignmentToFinalVariableException {
        if (properties[INIT_INDEX] != null) { // is INIT
            if (properties[FINAL_INDEX] != null) { //is FINAL
                throw new AssignmentToFinalVariableException(line);
            }
            return true;
        }
        if (function != null) {
            function.setProprietiesOfVariable(scopeIndex, variableName,
                    new String[]{properties[TYPE_INDEX], properties[FINAL_INDEX], INIT});
        }
        else {
            globalVariableMap.put(variableName,
                    new String[]{properties[TYPE_INDEX], properties[FINAL_INDEX], INIT});
        }
        return false;
    }

    /*
    The function looks for a variable to see if it is in the hashmap for the global variable.
     */
    private String[] findGlobalVariableAtMap(String line, String variableName)
            throws UnknownVariableException {
        String[] properties;
        if (!globalVariableMap.containsKey(variableName)){
            throw new UnknownVariableException(line);
        } else{
            properties = globalVariableMap.get(variableName);
        }
        return properties;
    }

    /*
    The function saves the variable in a hashmap.
     */
    private void saveVariable(int i, String currLine, String isFinal, String type, boolean isGlobal,
    Function function, int currScope) throws SJavaException {
        String nameVariable;
        String init = null;
        String line = String.valueOf(i+1);
        String[] expressions = currLine.split(COMA);
        for (String expression: expressions) {
            expression = expression.trim();
            Matcher matcher = patternManager.getMatcher
                    (VARIABLE_OR_VARIABLE_AND_ASSIGNMENT_PATTERN, expression);
            if (!matcher.matches()) {
                throw new InvalidVariableDeclarationException(line);
            }
            nameVariable = matcher.group(2);
            checkIfVariableIsExistInScope(line, nameVariable, isGlobal, function, currScope);
            if (matcher.group(3) != null) {
                analyzeAssignment(type, matcher.group(3).trim(), line, function);
                init = INIT;
            }
            String[] variableProprieties = new String[]{type, isFinal, init};
            putVariableInMap(nameVariable, variableProprieties, isGlobal, function, currScope);
        }
    }

    /*
    the function put the variable in the HashMap that in the function.
     */
    private void putVariableInMap(String nameVariable, String[] variableProprieties, boolean isGlobal,
                                  Function function, int currScope) {
        if (isGlobal) {
            globalVariableMap.put(nameVariable, variableProprieties);
        }
        else {
            function.putVariable(currScope, nameVariable, variableProprieties);
        }
    }

    /*
    the function check if the variable is not already in the hashmap that in the scope.
     */
    private void checkIfVariableIsExistInScope(String line, String nameVariable,
                                        boolean isGlobal, Function function, int index)
            throws SJavaException {
        if ((isGlobal && globalVariableMap.containsKey(nameVariable))
                || (!isGlobal && function.contains(nameVariable, index)!=-1))  {
            throw new DuplicateNameVariableException(line);
        }
    }

    /*
    the function get type and string and check if it's the same type.
     */
    private boolean checkLegalType(String type, String value, String line)
            throws SJavaException {
        boolean check = true;
        switch (type) {
            case STRING:
                check = value.startsWith(BACK_SLASH) && value.endsWith(BACK_SLASH);
                break;
            case INT:
                check = patternManager.getMatcher(INT_PATTERN, value).matches();
                break;
            case DOUBLE:
                check = patternManager.getMatcher(DOUBLE_PATTERN, value).matches();
                break;
            case BOOLEAN:
                check = patternManager.getMatcher(BOOLEAN_PATTERN, value).matches();
                break;
            case CHAR:
                check = value.startsWith(QUOTATION_MARK) && value.endsWith(QUOTATION_MARK)
                        && value.length() == LENGTH_OF_CHAR;
                break;
        }
        if (!check) {
            throw new InvalidValueForTypeException(line);
        }
        return true;
    }

    /*
    the function search the variable in the function.
     */
    private int findVariable(Function function, String value) {
        if (function != null) {
            int scope = function.contains(value);
            if (scope != NOT_FOUND) {
                return scope;
            }
        }
        if (globalVariableMap.containsKey(value)) {
            return IN_GLOBAL_MAP;
        }
        return NOT_FOUND;
    }

    /*
    the function check if the assignment is legal.
     */
    private void analyzeAssignment(String type, String value, String line, Function function)
            throws SJavaException {
        int scope = findVariable(function, value);
        String[] properties;
        if (scope == NOT_FOUND) {
            checkLegalType(type, value, line);
        }
        else {
            if (scope != IN_GLOBAL_MAP) {
                properties = function.getPropertiesOfVariable(scope, value);
            } else {
                properties = globalVariableMap.get(value);
            }
            checkIfVariableMatchesType(type, properties[TYPE_INDEX], line);
            checkInitializedVariable(properties[INIT_INDEX], line);
        }
    }

    /*
    the function check if variableMatchesType
     */
    private void checkIfVariableMatchesType(String typeVariable1, String typeVariable2, String line)
            throws SJavaException {
        if (!typeVariable1.trim().equals(typeVariable2.trim())) {
            throw new InvalidValueForTypeException(line);
        }
    }

    /*
    the function checks if a variable is initialized
     */
    private void checkInitializedVariable(String isInit, String line) throws SJavaException {
        if (isInit == null) {
            throw new UnInitializedVariableException(line);
        }
    }

    /*
    Ths function save the method segment in a Function object
     */
    private int saveMethod(int startIndex, String currLine,
                                  String name, String parameters) throws SJavaException {
        int endIndexScopeArray[] = findEndOfFunction(startIndex);
        int endIndex = endIndexScopeArray[0];
        int depthScope = endIndexScopeArray[1];
        Function newFunction = new Function(name, startIndex, endIndex, parameters, depthScope, patternManager);
        if (functionsMap.containsKey(name)) {
            throw new DuplicateNameFunctionException(name, String.valueOf(startIndex+1));
        }
        functionsMap.put(name, newFunction);
        return endIndex;
    }

    /*
    This function find the start and the end of each method segment according to
     the number of curly brackets.
     */
    private static int[] findEndOfFunction(int i) throws SJavaException{
        int depthCount = 1;
        int maxDepth = 1;
        int number = 1;
        String currLine;
        while (number > 0) {
            i++;
            if (i == code.size()) {
                throw new MissCloseBracketException(String.valueOf(i+1));
            }
            if (code.get(i) == null) {
                continue;
            }
            currLine = code.get(i).trim();
            if (currLine.contains(CLOSE_BRACKET)) {
                if (currLine.charAt(0) == CLOSE_BRACKET_IN_CHAR) {
                    number -= 1;
                    depthCount -= 1;
                } else {
                    throw new BracketOutOfPlaceException(String.valueOf(i+1));
                }
            }
            if (currLine.charAt(currLine.length()-1) == OPEN_BRACKET_IN_CHAR) {
                depthCount += 1;
                maxDepth = Math.max(maxDepth, depthCount);
                number += 1;
            }
        }
        return new int[]{i, maxDepth};
    }
}
