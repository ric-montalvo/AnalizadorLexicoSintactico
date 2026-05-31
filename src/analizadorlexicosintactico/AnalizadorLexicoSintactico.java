/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package analizadorlexicosintactico;
import java.util.ArrayList;
import java.util.Scanner;

/*
RICARDO ANTONIO MUÑOZ MONTALVO
SALVADOR TABOADA RAMIREZ
Primera estructura, toda la gramatica
*/

public class AnalizadorLexicoSintactico {
    
    ArrayList<String> grammatic = new ArrayList<>();
    ArrayList<String> ritghtProduction = new ArrayList<>();
    ArrayList<String> nonTerminalSymbols = new ArrayList<>();
    ArrayList<String> terminalSymbols = new ArrayList<>(); 

    //llenar lista con la gramatica
    public void writeAll(){
        try {
            Scanner reader = new Scanner(new java.io.File("Gramatica.txt"));
            while(reader.hasNextLine()){
                grammatic.add(reader.nextLine());
            }
            reader.close(); // Buena práctica cerrar el lector
        } catch (Exception e) {
            System.out.println("no hay archivo");
        }
    }

    //solo guarda lo que este despues de la ->
    public void rightProductionWritter(ArrayList<String> grammatic){
        for (int i = 0; i < grammatic.size(); i++) {
            String actual = grammatic.get(i);
            int position = actual.indexOf("->");
            if(position>-1){
                    ritghtProduction.add(actual.substring(position + 2)); 
            }
        }
    }

    //new, No terminal
    public void nonTerminalSymbol(ArrayList<String> grammatic) {
        String word = "";
        
        for (int i = 0; i < grammatic.size(); i++) {
            String actual = grammatic.get(i);
            
            actual = actual + " "; 
            
            for (int j = 0; j < actual.length(); j++) {
                char character = actual.charAt(j);
                if (character == ' ') {
                    if (word.length() > 0) {
                        if (word.startsWith("<") && word.endsWith(">")) {
                            boolean exist = false;
                            for (int k = 0; k < nonTerminalSymbols.size(); k++) {
                                if (nonTerminalSymbols.get(k).equals(word)) {
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                nonTerminalSymbols.add(word);
                            }
                        }
                        word = ""; 
                    }
                } else {
                    word = word + character;
                }
            }
        }
    }

     //new, terminal
    public void TerminalSymbol(ArrayList<String> grammatic) {
        String word = "";
        
        for (int i = 0; i < grammatic.size(); i++) {
            String actual = grammatic.get(i);
            
            actual = actual + " "; 
            
            for (int j = 0; j < actual.length(); j++) {
                char character = actual.charAt(j);
                if (character == ' ') {
                    if (word.length() > 0) {
                        if (!word.startsWith("<") && !word.endsWith(">") && !word.equals("->") && !word.equals("E") && !word.contains(".")) {
                            boolean exist = false;
                            for (int k = 0; k < terminalSymbols.size(); k++) {
                                if (terminalSymbols.get(k).equals(word)) {
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                terminalSymbols.add(word);
                            }
                        }
                        word = ""; 
                    }
                } else {
                    word = word + character;
                }
            }
        }
    }

    public static void main(String[] args) {
        AnalizadorLexicoSintactico lector = new AnalizadorLexicoSintactico();

        lector.writeAll(); 
        System.out.println("grammatic");
        for (int i = 0; i < lector.grammatic.size(); i++) {
            System.out.println(lector.grammatic.get(i));
        }

        lector.rightProductionWritter(lector.grammatic);
        System.out.println("\nright side");
        for (int i = 0; i < lector.ritghtProduction.size(); i++) {
            System.out.println(lector.ritghtProduction.get(i));
        }

        lector.nonTerminalSymbol(lector.grammatic);
        System.out.println("\nno temrinal symbol");
        for (int i = 0; i < lector.nonTerminalSymbols.size(); i++) {
            System.out.println(lector.nonTerminalSymbols.get(i));
        }

        lector.TerminalSymbol(lector.grammatic);
        System.out.println("\nterminal symbol");
        for (int i = 0; i < lector.terminalSymbols.size(); i++) {
            System.out.println(lector.terminalSymbols.get(i));
        }
    }
}
