package analizadorlexicosintactico;

import java.util.ArrayList;

class AnalizadorSintactico {
    noTemrianlSymbolList search = new noTemrianlSymbolList();
    TemrinalSymbols searchTermianl = new TemrinalSymbols();
    writeGrammatic grammatic = new writeGrammatic();
    rightSide derivation = new rightSide();

    // La matriz predictiva estática de Salva queda intacta
    public int matrizPredicitva[][] = {
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,2,0,0,0,2,0,0,2,0,0,0,0,2,2,2,0,0,0,0},
        {0,3,4,0,0,3,0,0,3,0,0,0,0,3,3,3,0,0,0,0},
        {0,6,0,0,0,7,0,0,8,0,0,0,0,5,5,5,0,0,0,0},
        {0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,11,0,0,0,11,0,10,0,0,0,0,0,0,0,0,0},
        {0,12,0,0,0,0,0,0,0,0,12,12,12,0,0,0,0,0,0},
        {0,0,0,14,0,0,0,14,0,0,0,0,0,0,0,0,13,13,13,13},
        {0,15,0,0,0,0,0,0,0,0,16,17,18,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,19,20,21,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,22,23,25,24}
    };

    // Mapeo estricto para asegurar que coincidan las posiciones con la matriz de Salva
    private String[] filasNoTerminales = {"<inicio>", "<sentencias>", "<sentf>", "<sent>", "<listaid>", "<listaf>", "<expresion>", "<exprf>", "<expr>", "<tipo>", "<oper>"};
    private String[] columnasTerminales = {"programa", "id", "finprograma", ";", "=", "leer", "(", ")", "escribir", ",", "litbinaria", "litoctal", "lithexa", "binario", "octal", "hexad", "+", "-", "*", "/"};

    public void sintacticAnalyzer(AnalizadorLexico lexer) {
        // Cargar las estructuras base de la gramática antes de iniciar
        grammatic.writeAll();
        derivation.rightProductionWritter(grammatic.grammatic);
        search.nonTerminalSymbol(grammatic.grammatic);
        searchTermianl.TerminalSymbol(grammatic.grammatic);

        Stack<String> stackAnalyzer = new Stack<>();
        
        // El algoritmo del LIDriver requiere colocar el fondo de pila y el símbolo inicial
        stackAnalyzer.push("$");
        stackAnalyzer.push(filasNoTerminales[0]); // Pone <inicio> en el tope

        Token tokenOriginal = lexer.obtenerSiguienteToken();
        String a = traducirToken(tokenOriginal);

        System.out.println("\n=== INICIANDO TRAZA DEL ANÁLISIS SINTÁCTICO (LIDriver) ===");

        while (!stackAnalyzer.empty()) {
            // Tope de la pila actualizado paso a paso
            String x = stackAnalyzer.peek();

            // Condición de aceptación exitosa completa
            if (x.equals("$") && a.equals("EOF")) {
                System.out.println("\n>> ANÁLISIS EXITOSO: La estructura de este programa es totalmente válida.");
                stackAnalyzer.pop();
                break;
            }

            boolean isnotTermianl = false;
            int index = -1;
            int indexTerminal = -1;

            // Identificar si x es No Terminal usando el arreglo estricto
            for (int i = 0; i < filasNoTerminales.length; i++) {
                if (filasNoTerminales[i].equals(x)) {
                    isnotTermianl = true;
                    index = i;
                    break;
                }
            }

            // Identificar la columna del terminal de entrada
            for (int i = 0; i < columnasTerminales.length; i++) {
                if (columnasTerminales[i].equals(a)) {
                    indexTerminal = i;
                    break;
                }
            }

            if (isnotTermianl == true) {
                if (indexTerminal == -1) {
                    System.err.println("\nError de sintaxis: Token actual fuera de alfabeto gramatical -> " + a);
                    break;
                }

                if (matrizPredicitva[index][indexTerminal] != 0) {
                    int number = matrizPredicitva[index][indexTerminal];
                    
                    String produccionCompleta = grammatic.grammatic.get(number - 1);
                    String getSize = derivation.ritghtProduction.get(number - 1);

                    // Impresión detallada para la corrida paso a paso solicitada en el reporte
                    System.out.print("Token actual: " + tokenOriginal.lexema + " \t| Tope pila: " + x + " \t| Regla usada: " + produccionCompleta);
                    if (tokenOriginal.categoria.equals("Identificador")) {
                        System.out.print(" \t[Tabla de Símbolos Actualizada]");
                    }
                    System.out.println();

                    stackAnalyzer.pop();

                    // Si la regla de producción genera un vacío, no agregamos elementos
                    if (!getSize.equals("E")) {
                        String word = "";
                        ArrayList<String> tempWord = new ArrayList<>();

                        for (int i = 0; i < getSize.length(); i++) {
                            if (getSize.charAt(i) == ' ') {
                                if (!word.isEmpty()) {
                                    tempWord.add(word);
                                    word = "";
                                }
                            } else {
                                word = word + getSize.charAt(i);
                            }
                        }
                        if (!word.isEmpty()) {
                            tempWord.add(word);
                        }

                        // Inserción inversa en la pila
                        for (int i = tempWord.size() - 1; i >= 0; i--) {
                            stackAnalyzer.push(tempWord.get(i));
                        }
                    }
                } else {
                    System.err.println("\nError de sintaxis: Casilla vacía en matriz para [" + x + ", " + a + "]");
                    break;
                }
            } else {
                // Validación cuando x es un Símbolo Terminal
                if (x.equals(a)) {
                    System.out.println("Match alcanzado: " + x + " | Acción: Consumo de entrada (Pop)");
                    stackAnalyzer.pop();
                    
                    if (!x.equals("$")) {
                        tokenOriginal = lexer.obtenerSiguienteToken();
                        a = traducirToken(tokenOriginal);
                    }
                } else {
                    System.err.println("\nError de sintaxis: Se esperaba el componente '" + x + "' pero se leyó '" + a + "'");
                    break;
                }
            }
        }
    }

    // Traduce las clasificaciones genéricas del léxico a los terminales de la matriz
    private String traducirToken(Token t) {
        if (t.lexema.equals("EOF")) {
            return "EOF";
        }
        if (t.categoria.equals("Palabra reservada") || t.categoria.equals("Caracter Simple")) {
            return t.lexema;
        }
        if (t.categoria.equals("Identificador")) {
            return "id";
        }
        if (t.categoria.equals("Número Binario")) {
            return "litbinaria";
        }
        if (t.categoria.equals("Número Octal")) {
            return "litoctal";
        }
        if (t.categoria.equals("Número Hexadecimal")) {
            return "lithexa";
        }
        return t.lexema;
    }
}