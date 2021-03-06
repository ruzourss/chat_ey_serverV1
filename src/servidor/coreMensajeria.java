package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import servidor.control.conexionControl;

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
    private final conexionControl control;
/**
 * constructor por defecto que inicializa el objeto mensaje y la lista de clientes.
     * @param puerto puerto de esucha por donde va recibir los clientes e enviar los mensajes
     * @param hilos listado de todos los clientes conectados
     * @param nombreClientes listado de todos los clientes conectados
     * @param mensaje objeto que se encarga de todo el tema de mensajes
     * @param field campo donde muestra el número de conexiones totales
     * @param area area de texto donde se almacena todo lo enviado y recibido
     * @param numeroUsuarios número de usuarios 
     * @param control objeto control que se encarga de enviar la lista de los usuarios
 */
    public coreMensajeria(int puerto,ArrayList<cliente> hilos,ArrayList<String> nombreClientes,mensajeria mensaje,JTextField field,JTextArea area,int numeroUsuarios,conexionControl control) {
        this.puerto=puerto;
        this.hilos=hilos;
        this.nombreClientes=nombreClientes;
        this.mensaje=mensaje;
        this.field=field;
        this.area=area;
        this.numeroUsuarios=numeroUsuarios;
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
                //Si el tamaño del array de clientes es menor e igual que el numero de usuarios establecidos
                //lanzamos el hilo.
                if(hilos.size()<numeroUsuarios){
                    cliente cliente = new cliente(canal, mensaje,hilos,field,area,nombreClientes,control);
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
