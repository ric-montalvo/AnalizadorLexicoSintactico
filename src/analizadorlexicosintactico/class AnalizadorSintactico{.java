
import analizadorlexicosintactico.AnalizadorLexico;
import analizadorlexicosintactico.TemrinalSymbols;
import analizadorlexicosintactico.noTemrianlSymbolList;
import analizadorlexicosintactico.rightSide;
import analizadorlexicosintactico.writeGrammatic;
import java.util.ArrayList;

class AnalizadorSintactico{
    noTemrianlSymbolList search = new noTemrianlSymbolList();
    TemrinalSymbols searchTermianl = new TemrinalSymbols();
    writeGrammatic grammatic = new writeGrammatic();
    rightSide derivation = new rightSide();
    //metodo de mamtriz
    //algorimto ll
    //diccionario

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

    public void sintacticAnalyzer(){
        Stack<String> stackAnalyzer = new Stack<>();
        stackAnalyzer.push(search.nonTerminalSymbols.get(0));

        String x = stackAnalyzer.peek();
        String demo = "programa variable ; finprograma";
        AnalizadorLexico demoProgram = new AnalizadorLexico(demo);

        String a = demoProgram.obtenerSiguienteToken().lexema;

        while(!stackAnalyzer.empty()){
            boolean isnotTermianl = false;
            int index = -1;//no hay coordeanada, x
            int indexTerminal=-1;//y
            for (int i = 0; i < search.nonTerminalSymbols.size(); i++) {//If x in noterminals Then
                String actual = search.nonTerminalSymbols.get(i);
                if(actual.equals(x)){
                    isnotTermianl = true;
                    index = i;
                    break;
                }
            }
            for (int i = 0; i <searchTermianl.terminalSymbols.size(); i++) {
                String actual = searchTermianl.terminalSymbols.get(i);
                if(actual.equals(a)){
                    indexTerminal=i;
                }
            }
            if(isnotTermianl==true){
                if(matrizPredicitva[index][indexTerminal]!=0){//If Predict[x,a] != 0
                    //variables
                    int number = matrizPredicitva[index][indexTerminal];//saco el numero
                    String getSize = derivation.ritghtProduction.get(number);//Replace x With Production[ Predict[x,a] ]
                    String word ="";
                    ArrayList<String> tempWord = new ArrayList<>();

                    //amos a separa la palabra pq no podemos recibirla junta, ni usar el .split()
                    for (int i = 0; i < getSize.length(); i++) {
                        if(getSize.charAt(i)==' '){
                            tempWord.add(word);
                            word = "";
                        }else{
                            word = word + getSize.charAt(i);
                        }
                    }
                    if(!word.isEmpty()){
                        tempWord.add(word);
                    }

                    stackAnalyzer.pop();//pop y ciclo de push
                    for (int i=tempWord.size()-1; i>=0; i--){
                        stackAnalyzer.push(tempWord.get(i));
                    }
                }else{
                    System.err.println("Error de sintaxis");
                    break;
                }
            }else{
                //falta jaja
                if(x.equals(a)){//x==a
                    stackAnalyzer.pop();
                    a = demoProgram.obtenerSiguienteToken().lexema;
                }else{
                    System.err.println("Error de sintaxis");
                    break;
                }
                break;
            }
        }
    }

}