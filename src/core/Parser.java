package core;

public class Parser {

    public static String getQuantity(String line) {
        return getSubstringOrEmpty(line, getIndexOfQuantity(line), getLastIndexOfQuantity(line));
    }

    public static String getThaiWord(String line) {
        return getSubstringOrEmpty(line, line.indexOf('|') + 1, getIndexOfQuantity(line));
    }

    public static String getEnglishWord(String line) {
        return getSubstringOrEmpty(line, line.indexOf(')') + 1, line.indexOf('|'));
    }

    public static String getUnit(String line) {
        return getSubstringOrEmpty(line, getLastIndexOfQuantity(line), line.length());
    }

    public static String getSubstringOrEmpty(String line, int begin, int end) {
        String res = "";
        try {
            res = line.substring(begin, end).trim();
        } catch (StringIndexOutOfBoundsException e) {}

        return res;
    }

    public static int getIndexOfQuantity(String line) {
        int index = line.indexOf(')') + 1;
        while (index < line.length()) {
            if (Character.isDigit(line.charAt(index))) return index;
            index++;
        }
        return line.length();
    }

    public static int getLastIndexOfQuantity(String line) {
        int beginIndex = line.indexOf(')') + 1;
        for (int i = line.length() - 1; i>beginIndex; i--) {
            if (Character.isDigit(line.charAt(i))) return ++i;
        }
        return line.length();
    }
}
