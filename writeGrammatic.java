/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grammatic;

/*
SALVADOR TABOADA RMIREZ
RICARDO ANTONIO MUÑOZ MONTALVO
LLenar la lista para la gramatica  
*/
import java.util.ArrayList;
import java.util.Scanner;

public class writeGrammatic{
    
    public ArrayList<String> grammatic = new ArrayList<>();
    
    //llenar lista con la gramatica
    public void writeAll(){
        try {
            Scanner reader = new Scanner(new java.io.File("Gramatica.txt"));
            while(reader.hasNextLine()){
                grammatic.add(reader.nextLine());
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("no hay archivo");
        }
    }
}

