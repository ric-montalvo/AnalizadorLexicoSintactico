/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorlexicosintactico;

/*
SALVADOR TABOADA RMIREZ
RICARDO ANTONIO MUÑOZ MONTALVO
LLenar lista co no terminales
*/
import java.util.ArrayList;

public class noTemrianlSymbolList{
    public ArrayList<String> nonTerminalSymbols = new ArrayList<>();
    public void nonTerminalSymbol(ArrayList<String> grammatic) {    
    //new, No terminal
    String word = "";
    for (int i = 0; i < grammatic.size(); i++) {
        String actual = grammatic.get(i);
        actual = actual.replace(");", ") ;");
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
}