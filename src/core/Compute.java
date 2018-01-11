package core;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Compute {

    private static String defaultDictFilename = "IngredientsDict.txt";

    private String begin = "<!--more-->\n";
    private String end = "<figure class=\"op-ad\">\n" +
            "  <iframe width=\"300\" height=\"250\" style=\"border:0; margin:0;\" src=\"https://www.facebook.com/adnw_request?placement=1939098986132591_1939099016132588&adtype=banner300x250\"></iframe>\n" +
            "</figure>\n";
    private KeyMaster ingredientKeyMaster;
    // TODO Add unit dictionary to catch thai unit and convert to Eng units.
    private KeyMaster unitKeyMaster;
    private Parser parser;
    private UI ui;


    public Compute()throws FileNotFoundException {
        this(null);
    }

    public Compute(UI ui) throws FileNotFoundException {
        this(defaultDictFilename, ui);
        System.out.println("Filename: " + defaultDictFilename);

    }

    public Compute(String fileName, UI ui) throws FileNotFoundException {
        this.ingredientKeyMaster = new KeyMaster(fileName);
        this.ui = ui;
    }

    public boolean train(String filename) throws FileNotFoundException {
        return this.ingredientKeyMaster.train(defaultDictFilename, true);
    }

    private String list2OlTag(List<String> list) {
        String res = "<ol>\n";
        for (String comp : list) {
            res += "<li>" + comp + "</li>\n";
        }
        res += "</ol>\n";

        return res;
    }

    private List<String> linesToList(String lines) {
        ArrayList<String> procedures = new ArrayList<>();
        try (Scanner s = new Scanner(lines)) {
            while (s.hasNextLine()) {
                procedures.add(s.nextLine());
            }
        }
        return procedures;
    }

    public String generate(String ingredientsThai, String procedureThai, String procedureEng) {
        String thaiIngredientHeader = "<h1><strong>ส่วนผสม</strong></h1>\n";
        String thaiProcedureHeader = "<h1><strong>วิธีทำ</strong></h1>\n";
        String engIngredientHeader = "<h1><strong>Ingredients</strong></h1>\n";
        String engProcedureHeader = "<h1><strong>Procedures</strong></h1>\n";
        String line = "";
        ArrayList<String> thaiIngredients = new ArrayList<>();
        ArrayList<String> engIngredients = new ArrayList<>();
        List<String> thaiProcedureList = linesToList(procedureThai);
        List<String> engProcedureList = linesToList(procedureEng);


        // Translate from thai ingredient list to English ingredient list.
        try (Scanner s = new Scanner(ingredientsThai)) {
            while (s.hasNextLine()) {
                line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                thaiIngredients.add(line);
                ArrayList<String> translatedLine = new ArrayList<>();
                String components[] = line.split("[+]");
                for (String component : components) {
                    String unit = Parser.getUnit(component);
                    String thaiWord = Parser.getThaiWord(component);
                    String quantity = Parser.getQuantity(component);
                    String engWord = ingredientKeyMaster.translate(thaiWord);
                    if (engWord == null) {
                        engWord = ui.translateWord(thaiWord);
                        this.ingredientKeyMaster.addNewKey(thaiWord, engWord, true);
                    }
                    translatedLine.add(String.join(" ", new String[] {engWord, quantity, unit}).trim());
                }
                engIngredients.add(String.join(" + ", translatedLine));
            }
        }

        // Table Tag construction
        String combinedResult = begin;
        combinedResult += "<table>\n";
        combinedResult += "<tr><th>" + thaiIngredientHeader + "</th><th>" + thaiProcedureHeader + "</th></tr>\n";
        combinedResult += "<tr><td valign=\"top\"><div style=\"width: 300px\">" + list2OlTag(thaiIngredients) + "</div></td><td valign=\"top\">" + list2OlTag(thaiProcedureList) + "</td></tr>\n";
        combinedResult += "<tr><th>" + engIngredientHeader + "</th><th>" + engProcedureHeader + "</th></tr>\n";
        combinedResult += "<tr><td valign=\"top\"><div style=\"width: 300px\">" + list2OlTag(engIngredients) + "</div></td><td valign=\"top\">" + list2OlTag(engProcedureList) + "</td></tr>\n";
        combinedResult += "</table>";
        combinedResult += end;

        return combinedResult;
    }

    private <T> List<List<T>> zip(List<T>... lists) {
        List<List<T>> result = new ArrayList<>();
        for (List<T> list : lists) {
            for (int i=0; i<list.size(); i++) {
                List<T> currList;
                if (i >= result.size()) {
                    currList = new ArrayList<>();
                    result.add(currList);
                }
                result.get(i).add(list.get(i));
            }
        }

        return result;
    }

    public List<String> translate(List<String> words) {
        ArrayList<String> res = new ArrayList<>();
        for (String word : words) {
            String tempTranslate = ingredientKeyMaster.translate(word);

        }

        return null;
    }
}
