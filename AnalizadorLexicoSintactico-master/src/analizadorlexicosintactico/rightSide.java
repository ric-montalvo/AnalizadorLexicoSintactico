/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorlexicosintactico;

/*
SALVADOR TABOADA RMIREZ
RICARDO ANTONIO MUÑOZ MONTALVO
Escribir todo del lado derecho  
*/
import java.util.ArrayList;

public class rightSide{
    public ArrayList<String> ritghtProduction = new ArrayList<>();
    //solo guarda lo que este despues de la flechga 
    public void rightProductionWritter(ArrayList<String> grammatic){
        for (int i = 0; i < grammatic.size(); i++) {
            String actual = grammatic.get(i);
            int position = actual.indexOf("-> ");
            if(position>-1){
                ritghtProduction.add(actual.substring(position+3)); 
            }
        }
    }
}