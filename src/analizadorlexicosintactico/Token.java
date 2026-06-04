/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorlexicosintactico;

public class Token {
    public String lexema;
    public String categoria;
    public int atributoASCII;

    public Token(String lexema, String categoria) {
        this.lexema = lexema;
        this.categoria = categoria;
        
        //calculamos su ASCII
        if (lexema.length() == 1 && (categoria.equals("Símbolo") || categoria.equals("Caracter Simple"))) {
            this.atributoASCII = (int) lexema.charAt(0);
        } else {
            this.atributoASCII = 0;
        }
    }

    @Override
    public String toString() {
        if (atributoASCII != 0) {
            return String.format("Lexema: %-15s | Categoria: %-20s | ASCII: %d", lexema, categoria, atributoASCII);
        }
        return String.format("Lexema: %-15s | Categoria: %s", lexema, categoria);
    }
}