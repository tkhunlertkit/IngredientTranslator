package core;

import java.io.*;
import java.util.*;

public class KeyMaster {

    private Map<String, String> keyKeeper;
    private String fileName;

    public KeyMaster(String fileName) throws FileNotFoundException {
        keyKeeper = new HashMap<>();
        this.fileName = fileName;
        this.defaultImport();
    }

    /**
     * Add new keys to the dictionary.
     * @param key key to the dictionary
     * @param val meaning of the key
     * @param update true to add (key, val) to database file.
     * @return true if the (key, val) can be added to the dictionary, false otherwise.
     */
    public boolean addNewKey(String key, String val, boolean update) {
        if (!keyKeeper.containsKey(key)) {
            keyKeeper.put(key, val);
            if (update)
                this.updateFile(key, val);
            return true;
        }
        return false;
    }

    public String translate(String key) {
        return keyKeeper.get(key);
    }

    public boolean train(String fileName, boolean doublyAdd) throws FileNotFoundException {
        try (Scanner s = new Scanner(new File(fileName))) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (!line.isEmpty() && line.contains(")")) {
                    String engMeaning = Parser.getEnglishWord(line);
                    String thaiWord = Parser.getThaiWord(line);
                    String quantity = Parser.getQuantity(line);
                    if (engMeaning != null && thaiWord != null && quantity != null) {
                        this.addNewKey(thaiWord, engMeaning, doublyAdd);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Cannot Train Model: No File Found");
        }
        return true;
    }

    public void printAllValues() {
        this.printAllValues(false);
    }

    public void printAllValues(boolean onlyProblems) {
        keyKeeper.forEach((k, v) -> {
            String formattedKey = String.format("%30.30s", k);
            String formattedVal = String.format("%30s", v);
            if (!onlyProblems) {
                System.out.println(formattedKey + " -> " + formattedVal);
            } else if (k.contains("|")) {
                System.out.println(formattedKey + " -> " + formattedVal);
            }

        });
    }

    public Set getAllKeys() {
        return keyKeeper.keySet();
    }

    public void updateFile(String key, String val) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter((fw));
             PrintWriter pw = new PrintWriter(bw) ) {
            pw.append(key + "," + val + "\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot open file to write");
        } catch (IOException e) {
            System.out.println("IOException ¯\\_(ツ)_/¯");
            e.printStackTrace();
        }
    }

    public void export(boolean appendFile) {
        try (FileWriter fw = new FileWriter(fileName, appendFile);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter p = new PrintWriter(bw) ) {
            keyKeeper.forEach((String k, String v) -> {
                p.append(k + "," + v + "\n");
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot open file to write");
        } catch (IOException e) {
            System.out.println("IOException ¯\\_(ツ)_/¯");
            e.printStackTrace();
        }
    }

    public void export(String fileName) {
        String tempFileName = this.fileName;
        this.fileName = fileName;
        this.export(false);
        this.fileName = tempFileName;
    }

    public void importFile(String fileName) throws FileNotFoundException {
        try (Scanner s = new Scanner(new File(fileName))) {
            this.importWithScanner(s, false);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Cannot open file ¯\\_(ツ)_/¯: " + fileName);
        }
    }

    public void defaultImport() throws FileNotFoundException {
        this.importFile(fileName);
    }

    public void importWithScanner(Scanner s, boolean update) {
        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            if (!line.isEmpty()) {
                String[] elems = line.split(",");
                if (elems.length == 2) {
                    if (!(elems[0].isEmpty() || elems[1].isEmpty()))
                        this.addNewKey(elems[0].trim(), elems[1].trim(), update);
                }

            }
        }
    }

    public void changeFileName(String newFilename) {
        this.fileName = newFilename;
    }

}
