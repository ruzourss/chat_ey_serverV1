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
    private final int puerto;
    private final mensajeria mensaje;
    private final ArrayList<cliente> hilos;
    private final ArrayList<String> nombreClientes;
    private final JTextField field;
    private final JTextArea area; 
    private final int numeroUsuarios;
/**
 * constructor por defecto que inicializa el objeto mensaje y la lista de clientes.
     * @param puerto
     * @param hilos
     * @param nombreClientes
     * @param mensaje
     * @param field
     * @param area
     * @param numeroUsuarios
 */
    public coreMensajeria(int puerto,ArrayList<cliente> hilos,ArrayList<String> nombreClientes,mensajeria mensaje,JTextField field,JTextArea area,int numeroUsuarios) {
        this.puerto=puerto;
        this.hilos=hilos;
        this.nombreClientes=nombreClientes;
        this.mensaje=mensaje;
        this.field=field;
        this.area=area;
        this.numeroUsuarios=numeroUsuarios;
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
                //Si el tamaño del array de clientes es menor e igual que el numero de usuarios establecidos
                //lanzamos el hilo.
                if(hilos.size()<numeroUsuarios){
                    cliente cliente = new cliente(canal, mensaje,hilos,field,area,nombreClientes);
                    hilos.add(cliente);
                    cliente.start();
                }else{
                    //en caso de que ya tenemos limite de conexiones
                    //cerramos el canal establecido
                    canal.close();
                }
            }//-->fin while
        } catch (IOException ex) {
            System.out.println("Error al abrir el puerto");
        }
    }

    @Override
    public void run() {
        initServer();
    }
}
