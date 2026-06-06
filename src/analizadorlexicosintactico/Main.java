package analizadorlexicosintactico;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        String rutaArchivo = "programa.txt"; 
        
        try {
            // Cargar archivo
            String codigoFuente = new String(Files.readAllBytes(Paths.get(rutaArchivo)), StandardCharsets.UTF_8);
            
            if (codigoFuente == null || codigoFuente.trim().isEmpty()) {
                System.out.println("El archivo programa.txt no contiene líneas de código.");
                return;
            }

            // Instanciar el motor sintáctico
            AnalizadorSintactico parser = new AnalizadorSintactico();
            
            // Cargar y procesar la gramática para cumplir los PUNTOS 2 y 3 del PDF
            parser.grammatic.writeAll();
            parser.derivation.rightProductionWritter(parser.grammatic.grammatic);
            parser.search.nonTerminalSymbol(parser.grammatic.grammatic);
            parser.searchTermianl.TerminalSymbol(parser.grammatic.grammatic);
            
            System.out.println("=============================================================");
            System.out.println("2. IMPRESIÓN DE LA GRAMÁTICA");
            for (int i = 0; i < parser.grammatic.grammatic.size(); i++) {
                System.out.println(parser.grammatic.grammatic.get(i));
            }
            
            System.out.println("\n3. IMPRESIÓN DE ESTRUCTURAS");
            System.out.println("--- Símbolos No Terminales ---");
            for (int i = 0; i < parser.search.nonTerminalSymbols.size(); i++) { 
                System.out.println(parser.search.nonTerminalSymbols.get(i));
            }
            System.out.println("--- Símbolos Terminales ---");
            for (int i = 0; i < parser.searchTermianl.terminalSymbols.size(); i++) {
                System.out.println(parser.searchTermianl.terminalSymbols.get(i));
            }
            System.out.println("--- Lados Derechos ---");
            for (int i = 0; i < parser.derivation.ritghtProduction.size(); i++) {
                System.out.println(parser.derivation.ritghtProduction.get(i));
            }
            
            // Cumpliendo el PUNTO 4 del PDF
            System.out.println("=============================================================");
            System.out.println("4. PROGRAMA A ANALIZAR (Código Fuente):");
            System.out.println(codigoFuente);
            System.out.println("=============================================================");

            // Instanciar el analizador léxico en modo de transmisión a demanda
            AnalizadorLexico lexer = new AnalizadorLexico(codigoFuente);
            
            // Iniciar el procesamiento LIDriver (PUNTO 5 del PDF)
            parser.sintacticAnalyzer(lexer);

        } catch (Exception e) {
            System.out.println("Ocurrió una excepción durante el análisis del compilador.");
            System.out.println("Mensaje detallado: " + e.getMessage());
        }
    }
}