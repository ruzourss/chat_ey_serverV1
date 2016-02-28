package servidor;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private final int puerto;
    private ServerSocket server;
    private Socket canalEntrada;
    private final ArrayList<String> clientes;
    private final ArrayList<multriControl> hilos;
    private final JTextArea area;
    private multriControl m;
    private boolean sesion =true;
    private Map<String,multriControl> mapa;
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
        hilos = new ArrayList<>();
        mapa = new HashMap<>();
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
                canalEntrada = server.accept();
                System.out.println("Conexión establecida por el puerto de control");
                m = new multriControl(canalEntrada, clientes, this, mapa);
                m.start();
            }
        } catch (IOException ex) {
            area.setBackground(Color.red);
            area.setText("ERROR: no se puede inicar el servidor del chat, revise el puerto");
        }
    }
    
    public void envia(){
        multriControl c;
        Iterator iterado = mapa.entrySet().iterator();
        while(iterado.hasNext()){
            Map.Entry map = (Map.Entry<String,multriControl>) iterado.next();
            c=(multriControl)map.getValue();
            c.enviar();
        }
    }
}
