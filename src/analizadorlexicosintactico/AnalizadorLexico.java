package analizadorlexicosintactico;

import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Ricardo Montalvo & Salvador Taboada
 */
public class AnalizadorLexico {
    
    private String codigoFuente;
    private int apuntador; 
    private int inicioTokenActual; //Marca dónde empezó la palabra para retroceder si fallamos
    private Token ultimoTokenGenerado; // Variable pivote para el modo On-Demand
    
    //Nuestro arreglo normal
    private String[] diccionarioReservadas = {"programa", "binario", "octal", "hexa", "leer", "escribir", "finprograma"};    
    
    // SOLAMENTE SE CONSERVAN LAS TABLAS PERMITIDAS POR EL PDF
    public ArrayList<Token> tablaPalabrasReservadas = new ArrayList<>();
    public ArrayList<Token> tablaSimbolos = new ArrayList<>();

    public AnalizadorLexico(String codigoFuente) {
        this.codigoFuente = codigoFuente;
        this.apuntador = 0;
    }

    // NUEVO MOTOR ON-DEMAND (Sustituye a analizarLinea)
    public Token obtenerSiguienteToken() {
        ultimoTokenGenerado = null;
        
        while (apuntador < codigoFuente.length()) {
            
            char caracterActual = codigoFuente.charAt(apuntador);
            
            //Verificamos si hay espacio en blanco
            if (Character.isWhitespace(caracterActual)) {
                apuntador = apuntador + 1; 
                continue;
            }
          
            //Se guarda la posicion inicial, si un camino falla, volvemos aca
            inicioTokenActual = apuntador;
            
            //Inicia boolean en true, si cambia a false por que un camino no es final
            //y retorna false
            boolean fueAceptado = q0(apuntador);
            
            if (fueAceptado == false) {
                qError(); 
            }
            
            // Retorna el único token encontrado en este ciclo al sintáctico
            return ultimoTokenGenerado;
        }
        
        // Si se termina el código fuente, avisa con un token especial
        return new Token("EOF", "Fin de Archivo");
    }

    //q0: Estado inicial
    private boolean q0(int i) {
        int longitudDelCodigo = codigoFuente.length();
        if (i >= longitudDelCodigo) {
            return false;
        }

        //Intentamos todos los caminos
        if (q1(i)) {
            return true;  //Rama Identificadores
        } else if (q3(i)) {
            return true;  //Rama Octales
        } else if (q6(i)) {
            return true;  //Rama Símbolos
        } else if (q8(i)) {
            return true;  //Rama Binarios
        } else if (q11(i)) {
            return true; //Rama Hexadecimales
        }
        
        return false; //Todos los caminos murieron
    }

    //RAMA IDENTIFICADORES Y RESERVADAS
    private boolean q1(int i) {
        if (i >= codigoFuente.length()) {
            return false;
        }
        
        char c = codigoFuente.charAt(i);
        
        if (c >= 'a' && c <= 'z') {
            return q2(i + 1); //Primera letra, pasa al bucle
        }
        return false;
    }

    private boolean q2(int i) { //Estado Bucle y Final
        //Si llegamos al fin del texto o a un delimitador se acepta
        if (i >= codigoFuente.length()) {
            aceptarIdentificador(i);
            return true;
        } else if (esFinDePalabra(codigoFuente.charAt(i))) {
            aceptarIdentificador(i);
            return true;
        }
        
        char c = codigoFuente.charAt(i);
        if (c >= 'a' && c <= 'z') {
            return q2(i + 1); //Bucle
        }
        
        return false; 
    }

    //RAMA OCTALES
    private boolean q3(int i) { //Inicio
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        if (c >= '0' && c <= '7') {
            return q4(i + 1);
        }
        return false;
    }

    private boolean q4(int i) { //Bucle
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        
        if (c >= '0' && c <= '7') {
            return q4(i + 1); //Bucle
        } else if (c == 'O') {
            return q5(i + 1); //Letra de terminación
        }
        
        return false;
    }

    private boolean q5(int i) { //Final
        if (i < codigoFuente.length()) {
            if (!esFinDePalabra(codigoFuente.charAt(i))) {
                return false;
            }
        }
        
        aceptarToken(i, "Número Octal");
        return true;
    }

    //RAMA CARACTERES SIMPLES
    private boolean q6(int i) { //Inicio
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        
        if (esCaracterSimple(c) == true) {
            return q7(i + 1);
        }
        return false;
    }

    private boolean q7(int i) { // Final
        aceptarToken(i, "Caracter Simple");
        return true;
    }

    //RAMA BINARIOS
    private boolean q8(int i) { //Inicio
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        if (c == '0' || c == '1') {
            return q9(i + 1);
        }
        return false;
    }

    private boolean q9(int i) { //Bucle
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        
        if (c == '0') {
            return q9(i + 1); 
        } else if (c == '1') {
            return q9(i + 1); 
        } else if (c == 'B') {
            return q10(i + 1); 
        }
        
        return false;
    }

    private boolean q10(int i) { //Final
        if (i < codigoFuente.length()) {
            if (!esFinDePalabra(codigoFuente.charAt(i))) {
                return false;
            }
        }
        
        aceptarToken(i, "Número Binario");
        return true;
    }

    //RAMA HEXADECIMALES
    private boolean q11(int i) { //Inicio
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        
        //Lo separamos en dos if directos,que siguen el mismo camino
        if (c >= '0' && c <= '9') {
            return q12(i + 1);
        } else if (c >= 'A' && c <= 'F') {
            return q12(i + 1);
        }
        
        return false;
    }

    private boolean q12(int i) { //Bucle
        if (i >= codigoFuente.length()) {
            return false;
        }
        char c = codigoFuente.charAt(i);
        
        if (c >= '0' && c <= '9') {
            return q12(i + 1); //Bucle
        } else if (c >= 'A' && c <= 'F') {
            return q12(i + 1); //Bucle
        } else if (c == 'X') {
            return q13(i + 1); //Letra de terminacion
        }
        
        return false;
    }

    private boolean q13(int i) { //Final
        if (i < codigoFuente.length()) {
            if (!esFinDePalabra(codigoFuente.charAt(i))) {
                return false;
            }
        }
        
        aceptarToken(i, "Número Hexadecimal");
        return true;
    }
    

    //ESTADO DE ERROR
    private void qError() {
        int indexError = inicioTokenActual;
        
        //Recogemos toda la palabra basura hasta topar con un espacio o símbolo, esto para fragmnetar
        while (indexError < codigoFuente.length()) {
            char c = codigoFuente.charAt(indexError);
            if (esFinDePalabra(c)) {
                break; 
            }
            indexError++;
        }
        //obtiene sus puntos de referencia del fragfmento
        String lexemaError = codigoFuente.substring(inicioTokenActual, indexError);
        String mensajeError = "Error Lexico: Palabra no pertenece a ningun lenguaje"; 
        
        //clasificacion del error
        int longitudError = lexemaError.length();
        if (longitudError > 0) {
            boolean tieneLetras = false;
            boolean tieneNumeros = false;
            
            for (int k = 0; k < lexemaError.length(); k++) {
                char caracterRevisado = lexemaError.charAt(k);
                if (Character.isDigit(caracterRevisado)) {
                    tieneNumeros = true;
                } else if (Character.isLetter(caracterRevisado)) {
                    tieneLetras = true;
                }
            }
            
            if (tieneLetras && !tieneNumeros) {
                mensajeError = "Error Lexico: Identificador invalido (debe contener solo letras minusculas)";
            } else if (Character.isDigit(lexemaError.charAt(0))) {
                mensajeError = "Error Lexico: Posible malformacion numerica (revise reglas de bin, oct, hexa)";
            }
        }
        
        // Modificación: Solo guardamos el token en la variable, ya no hay tabla de errores
        ultimoTokenGenerado = new Token(lexemaError, mensajeError);
        
        apuntador = indexError; 
    }

    //METODOS AUXILIARES DE ACEPTACION
    private void aceptarToken(int finIndex, String categoria) {
        String lexema = codigoFuente.substring(inicioTokenActual, finIndex);
        
        // Modificación: Solo pasamos el token al pivote, se eliminaron las tablas inválidas
        ultimoTokenGenerado = new Token(lexema, categoria);
        
        apuntador = finIndex; 
    }
    
    //primero vemos si es palabra reservada, si encuentra se agrega a la lista de las reservadas, si no pues es un identificador
    private void aceptarIdentificador(int finIndex) {
        String lexema = codigoFuente.substring(inicioTokenActual, finIndex);
        boolean esReservada = false;
        
        for (int j = 0; j < diccionarioReservadas.length; j++) {
            String palabraActual = diccionarioReservadas[j];
            if (lexema.equals(palabraActual)) { 
                esReservada = true;
                break; 
            }
        }

        if (esReservada == true) {
            ultimoTokenGenerado = new Token(lexema, "Palabra reservada");
            tablaPalabrasReservadas.add(ultimoTokenGenerado);
        } else {
            ultimoTokenGenerado = new Token(lexema, "Identificador");
            
            // MODIFICACIÓN: Filtrado estricto para que la tabla de símbolos no repita identificadores
            boolean existe = false;
            for (int k = 0; k < tablaSimbolos.size(); k++) {
                if (tablaSimbolos.get(k).lexema.equals(lexema)) {
                    existe = true;
                    break;
                }
            }
            if (existe == false) {
                tablaSimbolos.add(ultimoTokenGenerado);
            }
        }
        
        apuntador = finIndex; 
    }

    //se evalua que la letra esta entre 0-8, cualquier otro numero lo enviara a false
    private boolean esCaracterSimple(char c) {
        String simples = ";=+-*/,()"; 
        int posicion = simples.indexOf(c);
        
        if (posicion != -1) {
            return true;
        } else {
            return false;
        }
    }

    //para evluar la palabra el apuntar debe encontrar un espacio o un sibolo, cualquiera de estos dos encuntra el fin
    private boolean esFinDePalabra(char c) {
        boolean esEspacio = Character.isWhitespace(c);
        boolean esSimbolo = esCaracterSimple(c);
        
        if (esEspacio || esSimbolo) {
            return true;
        }
        
        return false;
    }
    
    // EL METODO DE EXPORTAR TABLAS FUE ELIMINADO POR COMPLETO COMO INDICA EL PDF
}