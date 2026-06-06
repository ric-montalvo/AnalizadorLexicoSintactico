package analizadorlexicosintactico;
/*
MUÑOZ MONTALVO RICARDO ANTONIO
TABOADA RAMIREZ SALVADOR
27/05/2026
Strings, <T> generico
*/
class Symbol<T> {
    T data;          
    Symbol<T> next; 
    public Symbol(T data) {
        this.data = data;
        this.next = null; 
    }
}

public class Stack<T> {
    private Symbol<T> top; 

    public Stack() {
        top = null;
    }

    public boolean empty() {
        return top == null; 
    }
    
    public void push(T element) {
        Symbol<T> newSymbol = new Symbol<>(element); 
        newSymbol.next = top;                        
        top = newSymbol;                            
    }
    
    public T pop() {
        if (empty()) {
            return null; 
        }
        T topData = top.data;
        top = top.next;   
        return topData; 
    }

    public T peek() {
        if (empty()) {
            return null;
        }
        return top.data; 
    }
    
    // Método agregado para cumplir el punto 5 (Imprimir contenido de la pila)
    public String obtenerContenidoPila() {
        if (empty()) {
            return "Vacía";
        }
        String resultado = "";
        Symbol<T> temporal = top;
        while (temporal != null) {
            resultado = temporal.data + " " + resultado;
            temporal = temporal.next;
        }
        return resultado;
    }
}