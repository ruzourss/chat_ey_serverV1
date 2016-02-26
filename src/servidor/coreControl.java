package servidor;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private ArrayList<String> niks;
    private JTextArea area;
    private outObject out;
    private ObjectInputStream in;
    private Map<String,Socket> canales;
    /**
     * Constructor que recibe el numero de puerto, lista de clientes y el area
     * de escribir mensajes
     * @param puerto puerto por el que va escuchar el servidor
     * @param clientes el listado de los clientes en un array
     * @param area area de texto donde se muestra los mensajes del sistema
     */
    public coreControl(int puerto,ArrayList<String> clientes,JTextArea area) {
        this.puerto=puerto;
        this.clientes=clientes;
        this.area=area;
        contenedorCanales = new ArrayList<>();
        niks = new ArrayList<>();
        canales = new HashMap<>();
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
                System.out.println("Conexión establecida por el puerto de control");
                //añadimos el canal en un array
                contenedorCanales.add(canal);
                //le enviamos en la primera conexión el litado total de los usuarios
                envioListadoUsuarios();
            }
        } catch (IOException ex) {
            area.setBackground(Color.red);
            area.setText("ERROR: no se puede inicar el servidor del chat, revise el puerto");
        }
    }
    /**
     * Método que será ejecutado por cada hilo cuando el usuario se ha conectado 
     * correctamente para avisar a los demás usuarios del usuario nuevo
     */
    public void envioListadoUsuarios(){
        for(int i=0;i<contenedorCanales.size();i++){
           try {
               out= new outObject(contenedorCanales.get(i).getOutputStream());
               out.writeObject(clientes);
               out.flush();
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
    
    private void obtenerDatosDelCliente(){
        String estado;
        String nick="";
        try {
            while(true){
                //se queda a la espera de recibir un estado
                estado = (String) in.readObject();
                switch(estado){
                    //si ha recibido de que el cliente le va enviar el nick
                    case estados.SEND_NICK:
                        //estamos a la espera de que el envie el nick
                        nick = (String) in.readObject();
                        //cuando lo recibimos le enviamos al cliente de que le vamos a enviar el listado de usuarios
                        out.writeObject(estados.SEND_USERS);
                        out.flush();
                        break;
                    //si el servidor recibe dame los usuarios conectados vamos enviarle los usuarios conectados actualmente
                    case estados.GET_USERS:
                        out.writeObject(clientes);
                        out.flush();
                        //además como todo es correcto vamos añadir en nuestra colección el socket con el nick asociado
                        canales.put(nick, canal);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(coreControl.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
