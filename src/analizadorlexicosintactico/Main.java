/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package analizadorlexicosintactico;

/*
SALVADOR TABOADA RMIREZ
RICARDO ANTONIO MUÑOZ MONTALVO
main
*/

public class Main {
    public static void main(String[] args) {     
        writeGrammatic read = new writeGrammatic();
        rightSide rightSide = new rightSide();
        noTemrianlSymbolList noTerminal = new noTemrianlSymbolList();
        TemrinalSymbols terminal = new TemrinalSymbols();
        read.writeAll(); 
        System.out.println("=============================================================");
        System.out.println("-----------------Gramatica-----------------");
        for (int i = 0; i < read.grammatic.size(); i++) {
            System.out.println(read.grammatic.get(i));
        }
        System.out.println("=============================================================");
        rightSide.rightProductionWritter(read.grammatic);
        System.out.println("-----------------Lado derecho de la gramatica-----------------");
        for (int i = 0; i < rightSide.ritghtProduction.size(); i++) {
            System.out.println(rightSide.ritghtProduction.get(i));
        }
        System.out.println("=============================================================");
        noTerminal.nonTerminalSymbol(read.grammatic);
        System.out.println("-----------------No terminales-----------------");
        for (int i = 0; i < noTerminal.nonTerminalSymbols.size(); i++) { 
            System.out.println(noTerminal.nonTerminalSymbols.get(i));
        }
        System.out.println("=============================================================");
        terminal.TerminalSymbol(read.grammatic);
        System.out.println("-----------------Terminales-----------------");
        for (int i = 0; i < terminal.terminalSymbols.size(); i++) {
            System.out.println(terminal.terminalSymbols.get(i));
        }
        System.out.println("=============================================================");
    }
}