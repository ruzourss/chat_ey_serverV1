/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.util.ArrayList;

/**
 *
 * @author Tote
 */
public class mensajeria {
    
    private String mensajes;
    
    
    private void aniadir(String text){
        this.mensajes+=text+"\n";
    }
    
    public void escribir(String cadena,ArrayList<cliente> hilos){
        aniadir(cadena);
        System.out.println("Recibido: "+cadena);
        for(int i=0;i<hilos.size();i++){
            hilos.get(i).escribir(cadena);
        }
    }
}
