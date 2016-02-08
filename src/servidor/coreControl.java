package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 * clase que inicia el puerto de control para el envio de 
 * de la lista de los clientes
 * @author Tautvydas Bagocius
 */
public class coreControl extends Thread{ 
    //Declaramos todos los objetos y variables que vamos a necesitar
    private int puerto;
    private ServerSocket server;
    private Socket canal;
    private ArrayList<Socket> contenedorCanales;
    private ArrayList<String> clientes;
    private JTextArea area;
    /**
     * Constructor que recibe el numero de puerto, lista de clientes y el area
     * de escribir mensajes
     * @param puerto puerto por el que va escuchar el servidor
     * @param clientes el listado de los clientes en un array
     * @param area area de texto donde se muestra los mensajes del sistema
     */
    public coreControl(int puerto,ArrayList<String> clientes,JTextArea area) {
        this.puerto=puerto;
        contenedorCanales = new ArrayList<>();
        this.clientes=clientes;
        this.area=area;
    }
    
    @Override
    public void run() {
       initServer();
    }
    /**
     * Método que arranca el servidor , se pone a la escucha por el puerto
     * indicado
     */
    private void initServer(){
        try {
            //instanciamos el server por el puerto indicado
            server = new ServerSocket(puerto);
            area.append("Iniciado servicio de control puerto: "+puerto+"\n");
            //bucle infinito para la escucha de los clientes
            while(true){
                //a la espera de que se conecte alguien
                canal = server.accept();
                //añadimos el canal en un array
                contenedorCanales.add(canal);
                //le enviamos en la primera conexión el litado total de los usuarios
                new ObjectOutputStream(canal.getOutputStream()).writeObject(clientes);             
            }
        } catch (IOException ex) {
            Logger.getLogger(coreControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Método que será ejecutado por cada hilo cuando el usuario se ha conectado 
     * correctamente para avisar a los demás usuarios del usuario nuevo
     */
    public void envioListadoUsuarios(){ 
        for(int i=0;i<contenedorCanales.size();i++){
           try {
                new ObjectOutputStream(contenedorCanales.get(i).getOutputStream()).writeObject(clientes);
            } catch (IOException ex) {
                Logger.getLogger(coreControl.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }  
    }
    /**
     * Método que elimina el canal del contenedor por que se ha desconectado del servidor
     * esto es muy importante controlarlo ya que si no se encuentra el canal disponible
     * nos puede lanzar un error de sistema
     * @param direccionIP direccion IP del canal que se ha desconectado (cliente)
     */
    public void eliminaCanal(String direccionIP){
        for(int i=0;i<contenedorCanales.size();i++){
            if(contenedorCanales.get(i).getInetAddress().getHostAddress().equals(direccionIP)){
                contenedorCanales.remove(i);
                break;
            }
        }
    }
    
}