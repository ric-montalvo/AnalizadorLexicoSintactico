/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorlexicosintactico;

/*
SALVADOR TABOADA RMIREZ
RICARDO ANTONIO MUÑOZ MONTALVO
LLenar la lista con terminales  
*/
import java.util.ArrayList;

public class TemrinalSymbols{
    public ArrayList<String> terminalSymbols = new ArrayList<>();
    public void TerminalSymbol(ArrayList<String> grammatic) {
        String word = "";
        
        for (int i = 0; i < grammatic.size(); i++) {
            String actual = grammatic.get(i);
            actual = actual.replace(");", ") ;");
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
}