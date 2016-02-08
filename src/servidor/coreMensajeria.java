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
    private final int puerto =9000;
    private final mensajeria mensaje;
    private final ArrayList<cliente> hilos;
    private final ArrayList<String> nombreClientes;
    private final JTextField field;
    private final JTextArea area;
    private coreControl control;
/**
 * constructor por defecto que inicializa el objeto mensaje y la lista de clientes.
     * @param hilos
     * @param nombreClientes
     * @param mensaje
     * @param field
     * @param area
     * @param control
 */
    public coreMensajeria(ArrayList<cliente> hilos,ArrayList<String> nombreClientes,mensajeria mensaje,JTextField field,JTextArea area,coreControl control) {
        this.hilos=hilos;
        this.nombreClientes=nombreClientes;
        this.mensaje=mensaje;
        this.field=field;
        this.area=area;
        this.control=control;
    }
    /**
     * método que arranca el servidor. Esta en un bucle infinito
     */
    public void initServer(){
        try {
            ServerSocket s = new ServerSocket(puerto);
            area.append("Iniciado servicio de mensajería puerto: "+puerto+"\n");
            while(true){
                
                Socket canal = s.accept();
                System.out.println("Conexión establecida");
                
                cliente cliente = new cliente(canal, mensaje,hilos,field,area,nombreClientes,control);
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
