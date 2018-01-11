import core.Compute;
import core.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by MSDK on 8/4/17.
 */
public class GUI extends JFrame implements UI {

    public static void main(String[] args) throws IOException {
        new GUI();
    }

    private final TextArea thaiIngredients = new TextArea();
    private final TextArea outputText      = new TextArea();
    private final TextArea thaiProcedure   = new TextArea();
    private final TextArea engProcedure    = new TextArea();


    public GUI() {

        outputText.setEditable(false);

        JPanel main = new JPanel();
        main.setLayout(new GridLayout(0, 1));

        // ingredients panel
        JPanel ingredientsPanel = new JPanel();
        ingredientsPanel.add(thaiIngredients);
        ingredientsPanel.setLayout(new GridLayout(1, 1));
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("Ingredient List (Thai)"));
        main.add(ingredientsPanel);

        // Middle Section Construction
        JPanel engProcedurePanel = this.createLabelPanel(engProcedure, "English Procedures");
        JPanel thaiProcedurePanel = this.createLabelPanel(thaiProcedure, "Thai Procedures");
        JPanel middleSection = new JPanel();
        middleSection.setLayout(new GridLayout(1, 2));
        middleSection.add(thaiProcedurePanel);
        middleSection.add(engProcedurePanel);
        main.add(middleSection);

        // Output Panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridLayout(1, 1));
        outputPanel.add(outputText);
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output as HTML"));
        main.add(outputPanel);

        JButton submit = new JButton("Convert & Copy");
        submit.addActionListener(e -> {
            String ingredientsText = thaiIngredients.getText();
            String thaiProcedureText = thaiProcedure.getText();
            String engProcedureText = engProcedure.getText();

            try {
                String res = new Compute(this).generate(ingredientsText, thaiProcedureText, engProcedureText);
                outputText.setText(res);

                StringSelection stringSelection = new StringSelection(outputText.getText());
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            } catch (FileNotFoundException e1) {
                showErrorMessage(e1.getMessage(), "File Not Found");
            }


        });
        setLayout(new BorderLayout());
        add(main);
        add(submit, BorderLayout.SOUTH);
        setSize(1000, 800);
        setVisible(true);
        this.setTitle("For Aomy");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(new Frame(),
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createLabelPanel(Component input, String title) {
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(1, 1));
        res.add(input);
        res.setBorder(BorderFactory.createTitledBorder(title));
        return res;
    }

    @Override
    public String translateWord(String word) {
        return JOptionPane.showInputDialog("Please translate " + word + " to English");
    }

    private int indexOfFirstInt(String s) {
        int i = 0;
        while (i < s.length() && !Character.isDigit(s.charAt(i))) i++;
        return (i == s.length())? -1 : i;
    }
}
