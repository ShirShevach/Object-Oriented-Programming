package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    private static final int NOT_FOUND = -1;
    private static final String COMA = ",";
    private static final String INITIALIZED = "init";
    private static final String SPACE_REGEX = " +";
    private static final int PROPERTIES_LENGTH = 3;
    private static final int TYPE_INDEX = 0;
    private static final int FINAL_INDEX = 1;
    private static final int OUTER_SCOPE_INDEX = 0;
    private static final int NAME_INDEX = 2;
    private static final int TYPE = 1;
    private static final int FINAL = 0;
    private static final int NAME = 1;
    private static final String EMPTY_STRING = "";
    private static final String TYPES_PATTERN = "typesPattern";
    private String name;
    private int start;
    private int end;
    private int depthScope;
    private ArrayList<String> typeArgumentOrder;
    private ArrayList<HashMap<String, String[]>> scopeArray;
    private PatternManager patternManager;


    /**
     * Construct an instance of the class Function
     *
     * @param name           string, name of the function
     * @param start          int, the index of the line in the code in which the function begins
     * @param end            int, the index of the line in the code in which the function ends
     * @param parameters     the string that contains all the parameters of the function
     * @param depthScope     the current depth of the scope
     * @param patternManager
     */
    public Function(String name, int start, int end, String parameters, int depthScope,
                    PatternManager patternManager) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.depthScope = depthScope;
        this.scopeArray = new ArrayList<HashMap<String, String []>>();
        this.patternManager = patternManager;
        for (int i = 0; i <depthScope; i++) {
            this.scopeArray.add(new HashMap<String, String[]>());
        }
        this.typeArgumentOrder = null;
        if (handleTypeInOrder(parameters))
            createHashMapLocalVariable(parameters);
    }

    /*
    This method saves the order of the parameters' type in an array
     */
    private boolean handleTypeInOrder(String parameters) {
        Matcher findTypesMatcher = patternManager.getMatcher(TYPES_PATTERN, parameters);
        if (!findTypesMatcher.find()) {
            return false;
        }
        this.typeArgumentOrder = new ArrayList<>();
        do {
            typeArgumentOrder.add(parameters.substring(findTypesMatcher.start(), findTypesMatcher.end()));
        } while (findTypesMatcher.find());
        return true;
    }


    /*
    This method puts the parameters in a new hashmap in the outer scope
     */
    private void createHashMapLocalVariable(String parameters) {
        parameters = parameters.substring(1, parameters.length() - 1);
        String[] parametersArray = parameters.split(COMA);
        for (String param : parametersArray) {
            String[] currParameter = {EMPTY_STRING, null, INITIALIZED};
            String[] input = param.trim().split(SPACE_REGEX);
            if (input.length == PROPERTIES_LENGTH) {
                currParameter[TYPE_INDEX] = input[TYPE]; //type
                currParameter[FINAL_INDEX] = input[FINAL]; //final
                this.scopeArray.get(OUTER_SCOPE_INDEX).put(input[NAME_INDEX], currParameter);
            } else {
                currParameter[TYPE_INDEX] = input[TYPE]; //type
                this.scopeArray.get(OUTER_SCOPE_INDEX).put(input[NAME], currParameter);
            }
        }
    }


    /**
     * Getter of the private field start
     * @return int start
     */
    public int getStart() {
        return start;
    }

    /**
     *  Getter of the private field end
     * @return int, end
     */
    public int getEnd() {
        return end;
    }

    /**
     *  Getter of the private field name
     * @return String, name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns whether a variable is anywhere in the local variables.
     * @param nameVariable the name of the variable that we wish to find
     * @return the index of the scope in which the variable is found, else returns -1
     */
    public int contains(String nameVariable) {
        for (int i = depthScope-1; i >= 0; i--) {
            if (!scopeArray.get(i).isEmpty()) {
                if (scopeArray.get(i).containsKey(nameVariable)) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    public int contains(String nameVariable, int i) {
        if (scopeArray.get(i).containsKey(nameVariable)) {
            return i;
        }
        return NOT_FOUND;
    }


    /**
     * Getter of the properties from a given variable
     * @param scope the scope in which the variable exists
     * @param variableName the name of the variable
     * @return the properties of the variable
     */
    public String[] getPropertiesOfVariable(int scope, String variableName) {
        return scopeArray.get(scope).get(variableName);
    }


    /**
     * Setter of the properties from a given variable
     * @param scope the scope in which the variable exists
     * @param variableName the name of the variable
     * @param variableProperties the properties that we wish to set to the variable
     */
    public void setProprietiesOfVariable(int scope, String variableName, String[] variableProperties) {
        scopeArray.get(scope).put(variableName, variableProperties);
    }

    /**
     * This method put a variable in the local variables of the function
     * along with its matching properties and scope.
     * @param scope  the scope the variable belongs to
     * @param nameVariable the name of the variable
     * @param variableProprieties the properties that we wish to set to the variable
     */
    public void putVariable(int scope, String nameVariable, String[] variableProprieties) {
        scopeArray.get(scope).put(nameVariable, variableProprieties);
    }


    /**
     * The method clear the hashmap of the local variables in the current scope
     * @param scope the scope that we wish to empty
     */
    public void clearHashMap(int scope) {
        scopeArray.get(scope).clear();
    }

    /**
     * Getter of the array typeArgumentOrder
     * @return typeArgumentOrder
     */
    public ArrayList<String> getTypeArgumentOrder() {
        return typeArgumentOrder;
    }
}
