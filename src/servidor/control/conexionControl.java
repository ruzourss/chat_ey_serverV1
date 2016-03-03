package servidor.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.outObject;

/**
 * clase que esta siempre a la escucha de que un cliente se 
 * conecte, y que le envia un listado de todos los usuarios conectados
 * @author Tautvydas
 */
public class conexionControl extends Thread{
    
    private ServerSocket control;
    private Socket canal;
    private int puerto;
    private ArrayList<Socket> clientesControl;
    private ArrayList<String> listaUsuarios;
    private outObject out;
    /**
     * constructor por defecto
     */
    public conexionControl() {
    }
    /**
     * constructor que recibe 
     * @param puerto puerto de esucha
     * @param clientesControl listado de canales
     * @param listaUsuarios lista de usuarios conectados
     */
    public conexionControl(int puerto, ArrayList<Socket> clientesControl,ArrayList<String> listaUsuarios) {
        this.puerto = puerto;
        this.clientesControl = clientesControl;
        this.listaUsuarios=listaUsuarios;
    }
    /**
     * Método que arranca el servidor
     */
    private void initServer(){
        try {
            control = new ServerSocket(puerto);
            while(true){
                System.out.println("A la espera de usuarios");
                canal=control.accept();
                System.out.println("Usuario conectado por el puerto de control");
                clientesControl.add(canal);
                //lanzamos un hilo que se encargue de actualizar el listado
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        enviar();
                    }
                }).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(conexionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método que es llamado cada vez que se desconecta u conecta un cliente.
     * Realizamos el control de que si un cliente ya esta desconectado , lo removemos
     * de la lista y volvemos enviarla a los usuarios
     */
    public synchronized void enviar(){
        Iterator iterador = clientesControl.iterator();
        
        while(iterador.hasNext()){
            Socket c = (Socket) iterador.next();
            try {
                out = new outObject(c.getOutputStream());
                out.writeObject(listaUsuarios);
                out.flush();
            } catch (IOException ex) {
                iterador.remove();
                enviar();
            }
        }
    }
    
    
    @Override
    public void run() {
        initServer();
    }
}
