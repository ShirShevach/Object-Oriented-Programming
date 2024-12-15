package oop.ex6.main;



import oop.ex6.main.Exception.SJavaException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sjavac {
    private static final int IO_ERROR = 2;
    private static final int INVALID_CODE = 1;
    private static final int VALID_CODE = 0;
    private static final String SPACE_REGEX = "^\\s*$";
    private static final String COMMENT = "//";
    private static ArrayList<String> code;

    /**
     * The main function. get the path of the file, read it and
     * copy it to list.
     * @param args - the path of the file we want to check
     */
    public static void main(String[] args) {
        code = new ArrayList<String>();
        String line;
        Matcher matcherEmptyLine;
        Pattern emptyLinesPattern = Pattern.compile(SPACE_REGEX);
        try (FileReader fileReader = new FileReader(args[0]);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while((line = bufferedReader.readLine()) != null) {
                matcherEmptyLine = emptyLinesPattern.matcher(line);
                if (!matcherEmptyLine.matches() && !line.startsWith(COMMENT)) {
                    code.add(line);
                } else {
                    code.add(null);
                }
            }
        } catch (IOException IOe) {
            System.err.println(IOe.getMessage());
            System.out.println(IO_ERROR);
            return;
        }
        try {
            new Analyzer(code);
        } catch (SJavaException e) {
            System.err.println(e.getMessage());
            System.out.println(INVALID_CODE);
            return;
        }
        System.out.println(VALID_CODE);
    }


}
