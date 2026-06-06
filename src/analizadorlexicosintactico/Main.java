package analizadorlexicosintactico;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        String rutaArchivo = "programa.txt"; 
        
        try {
            // Leer el programa del usuario con codificación UTF-8
            String codigoFuente = new String(Files.readAllBytes(Paths.get(rutaArchivo)), StandardCharsets.UTF_8);
            System.out.println("=== COMPILADOR LÉXICO - SINTÁCTICO ===");
            System.out.println("Procesando código fuente desde: " + rutaArchivo + "\n");
            
            if (codigoFuente == null || codigoFuente.trim().isEmpty()) {
                System.out.println("El archivo programa.txt no contiene líneas de código.");
                return;
            }
           
            // Instanciar el analizador léxico en modo de transmisión a demanda
            AnalizadorLexico lexer = new AnalizadorLexico(codigoFuente);
            
            // Instanciar el motor sintáctico predictivo LL(1)
            AnalizadorSintactico parser = new AnalizadorSintactico();
            
            // Iniciar el procesamiento LIDriver
            parser.sintacticAnalyzer(lexer);
            
            // Impresión final de las estructuras autorizadas por el reporte del PDF
            System.out.println("\n=== TABLA DE PALABRAS RESERVADAS ===");
            for (int i = 0; i < lexer.tablaPalabrasReservadas.size(); i++) {
                System.out.println(lexer.tablaPalabrasReservadas.get(i).toString());
            }
            
            System.out.println("\n=== TABLA DE SÍMBOLOS FINALES (IDENTIFICADORES ÚNICOS) ===");
            for (int i = 0; i < lexer.tablaSimbolos.size(); i++) {
                System.out.println(lexer.tablaSimbolos.get(i).toString());
            }

        } catch (Exception e) {
            System.out.println("Ocurrió una excepción durante el análisis del compilador.");
            System.out.println("Mensaje detallado: " + e.getMessage());
        }
    }
}