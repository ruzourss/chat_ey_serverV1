package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * clase que inicaliza el funcionamiento del servidor, abre el puerto indicado
 * y esta a la escucha de nuevos clientes, una vez que se conecta se crea un hilo y 
 * se le pasa la conexión.
 * @author Tote
 */
public class coreMensajeria extends Thread {
    //declaramos todos los objetos y variables que vamos a necesitar
    private int puerto =8889;
    private mensajeria mensaje;
    private ArrayList<cliente> hilos;
    private JTextField field;
    private JTextArea area;
/**
 * constructor por defecto que inicializa el objeto mensaje y la lista de clientes.
 */
    public coreMensajeria(JTextField field,JTextArea area) {
        mensaje = new mensajeria(area);
        hilos =  new ArrayList<>();
        this.field=field;
        this.area=area;
    }
    /**
     * método que arranca el servidor. Esta en un bucle infinito
     */
    public void initServer(){
        try {
            ServerSocket s = new ServerSocket(puerto);
            area.append("Iniciado servicio de mensajería puerto: "+puerto);
            while(true){
                
                Socket canal = s.accept();
                System.out.println("Conexión establecida");
                
                cliente cliente = new cliente(canal, mensaje,hilos,field,area);
                hilos.add(cliente);
                cliente.start();
            }
        } catch (IOException ex) {
            System.out.println("Error al abrir el puerto");
        }
    }

    @Override
    public void run() {
        initServer();
    }
    
    
    
}
