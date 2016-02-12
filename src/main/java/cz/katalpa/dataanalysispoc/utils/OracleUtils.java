package cz.katalpa.dataanalysispoc.utils;

/**
 * @author jaroslav.repik
 */

public class OracleUtils {

    public static final String normalizeOracleName(String input) {
        //TODO all keywords etc.
        String result = input.toLowerCase().replace("#","").replace(".", "_");
        if (Character.isDigit(result.charAt(0))) {
            result = "X" + result;
        }
        return result;
    }
}
