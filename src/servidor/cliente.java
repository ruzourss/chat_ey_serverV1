
package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tote
 */
public class cliente extends Thread {
    
    private Socket canal;
    private mensajeria mensaje;
    private String cadena;
    private ArrayList<cliente> hilos; 
    private DataOutputStream out;
    private DataInputStream input;
    

    public cliente() {
    }

    public cliente(Socket canal, mensajeria mensaje,ArrayList<cliente> hilos ) {
        this.canal = canal;
        this.mensaje = mensaje;
        this.hilos=hilos;
    }
    
    @Override
    public void run() {
        ObtieneCanales();
        leer();
        cierraCanales();
    }
    
    private void leer(){
        try {
            while((cadena=input.readUTF())!=null){
                   mensaje.escribir(cadena,hilos);
            } 
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    public void escribir(String cadena){
        try {
            System.out.println("Enviar: "+cadena);
            out.writeUTF(cadena);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void ObtieneCanales(){
        try {
            out = new DataOutputStream(canal.getOutputStream());
            input = new DataInputStream(canal.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    private void cierraCanales(){
        if(input!=null){
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }if(out!=null){
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }if(canal!=null){
            try {
                canal.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
