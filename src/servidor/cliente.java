package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    private ArrayList<String> nombreClientes;
    private ObjectOutputStream ObjectOutput;
    private ObjectInputStream ObjectInput;
    private boolean session=true;
    private JTextField field;
    private JTextArea area;
    private coreControl control;
    
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
     * @param field
     * @param area
     * @param nombreClientes
     * @param control
     */
    public cliente(Socket canal, mensajeria mensaje,ArrayList<cliente> hilos,JTextField field,JTextArea area,ArrayList<String> nombreClientes,coreControl control) {
        this.canal = canal;
        this.mensaje = mensaje;
        this.hilos=hilos;
        this.area=area;
        this.nombreClientes=nombreClientes;
        this.control=control;
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
                cadenaRecibida=ObjectInput.readUTF();
                switch(cadenaRecibida){
                    case estados.SEND_NICK:
                        ObjectOutput.writeUTF(estados.GET_NICK);
                        ObjectOutput.flush();
                        //a la espera de que reciba un nick
                        String nick = ObjectInput.readUTF();
                            if(compruebaNick(nick)){
                                //en caso de que el nick sea el correcto le enviamos la confirmación
                                ObjectOutput.writeUTF(estados.NICK_OK);
                                ObjectOutput.flush();
                                //modificamos el nombre del hilo si es el correcto
                                setName(nick);
                                area.setText(area.getText()+">Entra en el chat..."+nick+"\n");
                                //añadimos el nombre a la lista que la debemos enviar
                                nombreClientes.add(nick);
                                control.envioListadoUsuarios();
                                field.setText("NÚMERO DE CONEXIONES ACTUALES: "+hilos.size());
                            }else{
                                ObjectOutput.writeUTF(estados.NICK_ERROR);
                                ObjectOutput.flush();
                            }
                    break;
                    case estados.GET_RECORD:
                        //Aqui es donde le envia el historia de mensajes
                        ObjectOutput.writeObject(mensaje.getMensajes());
                        
                    break;
                    case estados.SEND_MESSAGES:
                        //el cliente le avisa al servidor que este preparado para recibir 
                        //mensajes del cliente, el servidor le avisa al cliente 
                        //que esta preparado para recibir los mensajes
                        ObjectOutput.writeUTF(estados.GET_MESSAGES);
                        ObjectOutput.flush();
                        String cadena;
                        while(session){
                            cadena = (String) ObjectInput.readObject();
                            if(cadena.equals(estados.EXIT)){
                                exit();
                            }
                            //escribimos el mensaje
                            mensaje.escribir(cadena, hilos, this);
                        }
                    break;
                        default:
                            //mensaje desconocido
                            area.setText(area.getText()+" Mensaje desconocido, recibe: "+cadenaRecibida);
                    break;  
                }
            }//-->fin while
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//-->fin leer()
    /**
     * Método que elimina del array el hilo que quiere cerrar la conexión
     * y pone la variable sesión a false para que se salga del bucle
     */
    private void exit() {
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
        /*
        Eliminamos de la lista el nombre del cliente que se ha desconectado
        */
        for(int i=0;i<nombreClientes.size();i++){
            if(nombreClientes.get(i).equals(this.getName())){
                nombreClientes.remove(i);
                break;
            }
        }
        //llamo al método que envia la lista de usuarios de nuevo a los clientes
        control.envioListadoUsuarios();
        // modificamos la variable session para que termine el bucle y su
        // ejecución
        session=false;
    }
    /**
     * Este método debe ser público porque es llamado desde mensaje para renviar el mensaje 
     * a los clientes.
     * @param cadena cadenaRecibida a enviar a los clientes
     */
    public void escribir(String cadena){
        try {
            System.out.println("Enviar: "+cadena);
            ObjectOutput.writeUTF(cadena);
            ObjectOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * Método que obtiene los canles por donde debe recibir y enviar los datos
     */
    private void ObtieneCanales(){
        try {
            ObjectOutput = new ObjectOutputStream(canal.getOutputStream());
            ObjectInput = new ObjectInputStream(canal.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    /**
     * Método que cierra los canales cuando finaliza la conexión con el servidor
     */
    private void cierraCanales(){
        if(ObjectInput!=null){
            try {
                ObjectInput.close();
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }if(ObjectOutput!=null){
            try {
                ObjectOutput.close();
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
