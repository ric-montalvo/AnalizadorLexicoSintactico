package analizadorlexicosintactico;

import java.util.ArrayList;

class AnalizadorSintactico {
    noTemrianlSymbolList search = new noTemrianlSymbolList();
    TemrinalSymbols searchTermianl = new TemrinalSymbols();
    writeGrammatic grammatic = new writeGrammatic();
    rightSide derivation = new rightSide();

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
    
    private String[] filasNoTerminales = {"<inicio>", "<sentencias>", "<sentf>", "<sent>", "<listaid>", "<listaf>", "<expresion>", "<exprf>", "<expr>", "<tipo>", "<oper>"};
    private String[] columnasTerminales = {"programa", "id", "finprograma", ";", "=", "leer", "(", ")", "escribir", ",", "litbinaria", "litoctal", "lithexa", "binario", "octal", "hexad", "+", "-", "*", "/"};
    
    public void sintacticAnalyzer(AnalizadorLexico lexer) {
        grammatic.writeAll();
        derivation.rightProductionWritter(grammatic.grammatic);
        search.nonTerminalSymbol(grammatic.grammatic);
        searchTermianl.TerminalSymbol(grammatic.grammatic);

        Stack<String> stackAnalyzer = new Stack<>();
        
        stackAnalyzer.push("$");//fondo de la pila
        stackAnalyzer.push(search.nonTerminalSymbols.get(0));//<inicio> en el tope

        Token tokenOriginal = lexer.obtenerSiguienteToken();
        String a = traducirToken(tokenOriginal);

        System.out.println("\n===Inicio del algoritmo===");

        while (!stackAnalyzer.empty()) {
            String x = stackAnalyzer.peek();

            //se acepto y termina
            if (x.equals("$") && a.equals("EOF")) {
                System.out.println("\nLa estructura de este programa es totalmente valida.");
                stackAnalyzer.pop();
                break;
            }

            boolean isnotTermianl = false;
            int index = -1;
            int indexTerminal = -1;

            //det si x es NO TERMINAL y de paso ver su coordenada
            for (int i = 0; i < search.nonTerminalSymbols.size(); i++) {
                if (filasNoTerminales[i].equals(x)) {
                    isnotTermianl = true;
                    index = i;
                    break;
                }
            }

            //identificar la columna del terminal de entrada y tmabien ver la coordenada
            for (int i = 0; i < searchTermianl.terminalSymbols.size(); i++) {
                if (columnasTerminales[i].equals(a)) {
                    indexTerminal = i;
                    break;
                }
            }

            if (isnotTermianl == true) {
                if (indexTerminal == -1) {
                    System.err.println("\nError de sintaxis: Token actual fuera de alfabeto gramatical: " + a);
                    break;
                }

                if (matrizPredicitva[index][indexTerminal] != 0) {
                    int number = matrizPredicitva[index][indexTerminal];
                    
                    String produccionCompleta = grammatic.grammatic.get(number - 1);
                    String getSize = derivation.ritghtProduction.get(number - 1);

                    imprimirTrazaPasoAPaso(tokenOriginal, x, produccionCompleta, stackAnalyzer, lexer);
                    stackAnalyzer.pop();
                    
                    //no agrgear el vacio
                    if (!getSize.equals("E")) {
                        String word = "";
                        ArrayList<String> tempWord = new ArrayList<>();
                        //en la lista viene toda la cadena, sustituto del split, ir 1 en 1
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
                        if (!word.isEmpty()) {//checkpoint para ver q se vacio ttodo
                            tempWord.add(word);
                        }

                        //pila alreves
                        for (int i = tempWord.size() - 1; i >= 0; i--) {
                            stackAnalyzer.push(tempWord.get(i));
                        }
                    }
                } else {
                    System.err.println("\nError de sintaxis: hay 0 en la matriz en " + x + " y " + a);
                    break;
                }
            } else {
                //x es un Símbolo Terminal
                if (x.equals(a)) {
                    imprimirTrazaPasoAPaso(tokenOriginal, x, "Son iguales", stackAnalyzer, lexer);
                    
                    stackAnalyzer.pop();
                    
                    if (!x.equals("$")) {
                        tokenOriginal = lexer.obtenerSiguienteToken();
                        a = traducirToken(tokenOriginal);
                    }
                } else {
                    System.err.println("\nError de sintaxis: esperaba " + x + " pero se llego " + a + "");
                    break;
                }
            }
        }
    }

    //traduce para la matriz, lo toma como NO TERMINAL
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
    
    private void imprimirTrazaPasoAPaso(Token tokenActual, String topePila, String produccion, Stack<String> pila, AnalizadorLexico lexer) {
        
        if (produccion.equals("[Match directo - Consumo de Token]")) {
            System.out.println("Hay un TERMINAL en la pila: " + topePila);
            System.out.println("Token actual: " + tokenActual.lexema);
            System.out.println("Como son iguales, hace match");
            System.out.println("Sacamos el tope (" + topePila + ") de la pila y avanzamos a la siguiente palabra");
            System.out.println("Asi quedo la pila ahora: [" + pila.obtenerContenidoPila() + "], en la sig impresion se elimina: " + topePila);
            
            if (tokenActual.categoria.equals("Identificador")) {
                System.out.println("Guardamos esta nueva variable en la Tabla de Simbolos:");
                for (int s = 0; s < lexer.tablaSimbolos.size(); s++) {
                    System.out.println("    - " + lexer.tablaSimbolos.get(s).lexema);
                }
            }
        } 
        else {
            System.out.println("Encontramos un NO TERMINAL en la pila: " + topePila);
            System.out.println("Token actual: " + tokenActual.lexema);
            System.out.println("Buscamos en la matriz y derivamos usando la regla: " + produccion);
            System.out.println("Sacamos a (" + topePila + ") y hacemos push al reves");
            System.out.println("Asi quedo la pila ahora: [" + pila.obtenerContenidoPila() + "], en la sig impresion se elimina: " + topePila);
        }
        
        System.out.println("------------------------------------------------------------------");
    }
}