package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Tote
 */
public class core {
    
    private int puerto =8889;
    private mensajeria mensaje;
    private ArrayList<cliente> hilos = new ArrayList<>();

    public core() {
    mensaje = new mensajeria();
    }
    
    public void initServer(){
        try {
            ServerSocket s = new ServerSocket(puerto);
            
            while(true){
                
                Socket canal = s.accept();
                System.out.println("Conexión establecida");
                
                cliente cliente = new cliente(canal, mensaje,hilos);
                hilos.add(cliente);
                cliente.start();
            }
            
            
        } catch (IOException ex) {
            System.out.println("Error al abrir el puerto");
        }
    }
    
}
