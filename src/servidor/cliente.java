package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo que simula el funcionamiento de un nuevo cliente
 * @author Tote
 */
public class cliente extends Thread {
    //declaramos todos los objetos y variables que vamos a necesitar
    private Socket canal;
    private mensajeria mensaje;
    private String cadenaRecibida;
    private ArrayList<cliente> hilos; 
    private DataOutputStream out;
    private DataInputStream input;
    private boolean session=true;
    
    /**
     * Constructor por defecto
     */
    public cliente() {
    }
    /**
     * Constructor que recibe por parámetro
     * @param canal cliente que se ha conectado
     * @param mensaje objeto mensaje donde irán guardandose todos los mensajes
     * @param hilos el array de hilos donde estarán todos los clientes , esto es
     * necesario para realizar la silumación de un mensaje multicast, ya que cuando 
     * un cliente escriba debe llegar a todos los usuarios conectados.
     */
    public cliente(Socket canal, mensajeria mensaje,ArrayList<cliente> hilos ) {
        this.canal = canal;
        this.mensaje = mensaje;
        this.hilos=hilos;
    }
    
    @Override
    public void run() {
        ObtieneCanales();
        leer();
        cierraCanales();
    }
    /**
     * Método leer que simplemente esta a la espera de recibir una cadenaRecibida.
     * Después la envia al objeto mensaje que ahi es donde se "contabiliza"
     * el mensaje y se le renvia a todos los clientes conectados
     */
    private void leer(){
        try {
            while(session){
                cadenaRecibida=input.readUTF();
                switch(cadenaRecibida){
                    case estados.SEND_NICK:
                        out.writeUTF(estados.GET_NICK);
                        out.flush();
                        //a la espera de que reciba un nick
                        String nick = input.readUTF();
                            if(compruebaNick(nick)){
                                out.writeUTF(estados.GET_MESSAGES);
                                out.flush();
                                //modificamos el nombre del hilo si es el correcto
                                setName(nick);
                            }else{
                                out.writeUTF(estados.NICK_ERROR);
                                out.flush();
                            }
                    break;
                    case estados.EXIT:
                        /*
                        En caso de que el cliente le envia un EXIT, primero 
                        recorremos el array en busca del hilo y lo eliminamos.
                        */
                        for(int i=0;i<hilos.size();i++){
                            if(hilos.get(i).getName().equals(this.getName())){
                                hilos.remove(i);
                                break;
                            }
                        }
                        // modificamos la variable session para que termine el bucle y su
                        // ejecución
                        session=false;
                    break;
                        default:
                            mensaje.escribir(cadenaRecibida, hilos, this);
                    break;  
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * Este método debe ser público porque es llamado desde mensaje para renviar el mensaje 
     * a los clientes.
     * @param cadena cadenaRecibida a enviar a los clientes
     */
    public void escribir(String cadena){
        try {
            System.out.println("Enviar: "+cadena);
            out.writeUTF(cadena);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * Método que obtiene los canles por donde debe recibir y enviar los datos
     */
    private void ObtieneCanales(){
        try {
            out = new DataOutputStream(canal.getOutputStream());
            input = new DataInputStream(canal.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    /**
     * Método que cierra los canales cuando finaliza la conexión con el servidor
     */
    private void cierraCanales(){
        if(input!=null){
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }if(out!=null){
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }if(canal!=null){
            try {
                canal.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * comprobamos el nick, si el nick coincide con alguno que esta ya devuelve false
     * @param nick nombre del cliente que se ha conectado
     * @return false si el nick existe o true en caso de que es correcto
     */
    private boolean compruebaNick(String nick){
        boolean bandera=false;
        for(int i=0;i<hilos.size();i++){
            if(hilos.get(i).getName().equals(nick)){
                bandera=false;
                break;
            }else{
                bandera=true;
            }
        }
        return bandera;
    }
    
}
