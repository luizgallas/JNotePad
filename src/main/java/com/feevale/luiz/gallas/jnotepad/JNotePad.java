package com.feevale.luiz.gallas.jnotepad;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
/**
 *
 * @author luizgallas
 */
public class JNotePad extends JFrame {
    
    String text = "";
    boolean modified = false;
    Integer charCount = 0;
    
    TextArea textArea;
    TextField textField;
    
    public void mountScreen() {
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    
    }
    
    public JMenu mountArchiveMenu() {
        JMenu archiveMenu = new JMenu("Arquivo");
        
        JMenuItem openFile = new JMenuItem("Abrir");
        openFile.addActionListener((ActionEvent e) -> {
            List<String> fileContent = readFile();
            
            StringBuilder builder = new StringBuilder();
            for (String element : fileContent) {
                builder.append(element);
                builder.append(", ");
            }
            
            String stringBuilded = builder.toString();
            textArea.setText(stringBuilded);
            modified = false;
            charCount = stringBuilded.length();
            updateBottomMenu();
        });
        
        
        JMenuItem saveFile = new JMenuItem("Salvar");
        saveFile.addActionListener((ActionEvent e) -> {
            saveFile();
            modified = false;
            updateBottomMenu();
        });
        
        
        JMenuItem exit = new JMenuItem("Sair");
        exit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
        archiveMenu.add(openFile);
        archiveMenu.add(saveFile);
        archiveMenu.add(exit);
        
        return archiveMenu;
    }
    
    public JMenu mountFontMenu() {
        JMenu fontMenu = new JMenu("Fontes");
        
        JMenuItem arialItem = new JMenuItem("Arial");
        Font arialFont = new Font("Courier New", Font.PLAIN, 14);
        arialItem.setFont(arialFont);
        
        arialItem.addActionListener((ActionEvent e) -> {
            textArea.setFont(arialFont);
        });
        
        
        JMenuItem fontBItem = new JMenuItem("Fonte B");
        Font courierNewFont = new Font("Courier New", Font.PLAIN, 24);
        fontBItem.setFont(courierNewFont);
        
        fontBItem.addActionListener((ActionEvent e) -> {
            textArea.setFont(courierNewFont);
        });
       
        
        fontMenu.add(arialItem);
        fontMenu.add(fontBItem);
        
        return fontMenu;
    }
    
    public JMenu mountHelpMenu() {
        JMenu helpMenu = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        
        aboutItem.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(
                this, 
                "JNodepad: editor de texto simples.\nConstruído na disciplina de Programação II", 
                "Sobre o JNotepad", 
                JOptionPane.INFORMATION_MESSAGE);
        });
                
        
        helpMenu.add(aboutItem);
        
        return helpMenu;
    }
    
    private List<String> readFile() {
        List<String> data = new ArrayList<>();
        try {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);

        FileReader fr = new FileReader(fc.getSelectedFile());
        BufferedReader br = new BufferedReader(fr);
        String line;

        while((line = br.readLine()) != null)
          data.add(line);
        } catch (Exception ex) {
          ex.printStackTrace();
        }

        return data;
    }
    
    private void saveFile() {
        JFrame frame = new JFrame("Salvar Arquivo");
        
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Salvar Arquivo");
        fc.setDialogType(JFileChooser.SAVE_DIALOG);

        int result = fc.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            String content = textArea.getText();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                writer.write(content);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void updateBottomMenu() {
        String modifiedText = modified == true ? "sim" : "nao";
        textField.setText("Caracteres: " + charCount + " Modificado: " + modifiedText);
    }

    public static void main(String[] args) {
        JNotePad jNotePad = new JNotePad();
        jNotePad.mountScreen();
        jNotePad.setTitle("JNotepad");

        Container pane = jNotePad.getContentPane();
        JMenuBar menuBar = new JMenuBar();
        
        JMenu archiveMenu = jNotePad.mountArchiveMenu();
        JMenu fontMenu = jNotePad.mountFontMenu();
        JMenu helpMenu = jNotePad.mountHelpMenu();
        
        menuBar.add(archiveMenu);
        menuBar.add(fontMenu);
        menuBar.add(helpMenu);
        
        jNotePad.textArea = new TextArea();
        jNotePad.textArea.setText(jNotePad.text);
        jNotePad.textArea.addTextListener(l -> {
            String textAreaText = jNotePad.textArea.getText();

            if ("".equals(textAreaText)) {
                jNotePad.charCount = 0;
            } else if (textAreaText.length() < jNotePad.text.length()) {
              jNotePad.charCount--;
            } else {
                jNotePad.charCount++;
            }
            jNotePad.modified = true;
            jNotePad.text = jNotePad.textArea.getText();
            jNotePad.updateBottomMenu();
        });  
        
        jNotePad.textField = new TextField();
        jNotePad.textField.setEditable(false);
        
        jNotePad.updateBottomMenu();
        
        pane.add(menuBar, BorderLayout.NORTH);
        pane.add(jNotePad.textArea, BorderLayout.CENTER);
        pane.add(jNotePad.textField, BorderLayout.SOUTH);
        
                
        jNotePad.setVisible(true);
    }
}
