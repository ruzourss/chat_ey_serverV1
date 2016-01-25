package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo que simula el funcionamiento de un nuevo cliente
 * @author Tote
 */
public class cliente extends Thread {
    //declaramos todos los objetos y variables que vamos a necesitar
    private Socket canal;
    private mensajeria mensaje;
    private String cadena;
    private ArrayList<cliente> hilos; 
    private DataOutputStream out;
    private DataInputStream input;
    
    /**
     * Constructor por defecto
     */
    public cliente() {
    }
    /**
     * Constructor que recibe por parámetro
     * @param canal cliente que se ha conectado
     * @param mensaje objeto mensaje donde irán guardandose todos los mensajes
     * @param hilos el array de hilos donde estarán todos los clientes , esto es
     * necesario para realizar la silumación de un mensaje multicast, ya que cuando 
     * un cliente escriba debe llegar a todos los usuarios conectados.
     */
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
    /**
     * Método leer que simplemente esta a la espera de recibir una cadena.
     * Después la envia al objeto mensaje que ahi es donde se "contabiliza"
     * el mensaje y se le renvia a todos los clientes conectados
     */
    private void leer(){
        try {
            while((cadena=input.readUTF())!=null){
                   mensaje.escribir(cadena,hilos);
            } 
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    /**
     * Este método debe ser público porque es llamado desde mensaje para renviar el mensaje 
     * a los clientes.
     * @param cadena cadena a enviar a los clientes
     */
    public void escribir(String cadena){
        try {
            System.out.println("Enviar: "+cadena);
            out.writeUTF(cadena);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * Método que obtiene los canles por donde debe recibir y enviar los datos
     */
    private void ObtieneCanales(){
        try {
            out = new DataOutputStream(canal.getOutputStream());
            input = new DataInputStream(canal.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    /**
     * Método que cierra los canales cuando finaliza la conexión con el servidor
     */
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
