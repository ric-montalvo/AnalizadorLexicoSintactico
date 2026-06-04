package analizadorlexicosintactico;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

/**
 * @author Ricardo Montalvo & Salvador Taboada
 */
public class Main_1 {
    public static void main(String[] args) {
        String rutaArchivo = "programa.txt"; 
        
        try {
            String codigoFuente = new String(Files.readAllBytes(Paths.get(rutaArchivo)), StandardCharsets.UTF_8);
            System.out.println("Iniciando simulacion On-Demand desde: " + rutaArchivo + "\n");
            
            if(codigoFuente == null || codigoFuente.length() == 0){
                System.out.println("El archivo esta vacio.");
                return;
            }
           
            AnalizadorLexico lexer = new AnalizadorLexico(codigoFuente);
            Token tokenRecibido;
            
            // SIMULACIÓN: El Sintáctico pidiendo tokens uno por uno 
            do {
                tokenRecibido = lexer.obtenerSiguienteToken();
                
                if (!tokenRecibido.lexema.equals("EOF")) {
                    System.out.println("Sintactico pide token -> Recibe: " + tokenRecibido.toString());
                }
                
            } while (!tokenRecibido.lexema.equals("EOF"));
            
            // Imprimir la única tabla que sobrevive (Identificadores sin repetir) 
            System.out.println("\n=== TABLA DE SÍMBOLOS (ÚNICAMENTE IDENTIFICADORES ÚNICOS) ===");
            for (Token t : lexer.tablaSimbolos) {
                System.out.println(t.toString());
            }

        } catch (Exception e) {
            System.out.println("Error: No se pudo leer el archivo");
            System.out.println("Ocurrio un error: " + e.getMessage());
        }
    }
}