package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * clase que inicaliza el funcionamiento del servidor, abre el puerto indicado
 * y esta a la escucha de nuevos clientes, una vez que se conecta se crea un hilo y 
 * se le pasa la conexión.
 * @author Tote
 */
public class core {
    //declaramos todos los objetos y variables que vamos a necesitar
    private int puerto =8889;
    private mensajeria mensaje;
    private ArrayList<cliente> hilos;
/**
 * constructor por defecto que inicializa el objeto mensaje y la lista de clientes.
 */
    public core() {
        mensaje = new mensajeria();
        hilos =  new ArrayList<>();
    }
    /**
     * método que arranca el servidor. Esta en un bucle infinito
     */
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
