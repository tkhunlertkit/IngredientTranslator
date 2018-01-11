import core.KeyMaster;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Scratch {

    public static void main(String[] args) {
        try {
            KeyMaster km = new KeyMaster("");
            String text = ",\ntest,a";
            km.importWithScanner(new Scanner(text), false);
            km.printAllValues(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

