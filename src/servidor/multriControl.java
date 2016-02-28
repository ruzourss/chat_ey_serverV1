package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tautvydas
 */
public class multriControl extends Thread {
    
    private Socket canal;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private ArrayList<String> clientes;
    private coreControl control;
    private Map<String,multriControl> mapa;
    

    public multriControl() {
    }

    public multriControl(Socket canal,ArrayList<String> clientes,coreControl control,Map<String,multriControl> mapa) {
        this.canal = canal;
        this.clientes = clientes;
        this.control=control;
        this.mapa=mapa;
    }

    @Override
    public void run() {
        try {
            obtenerCanales();
        } catch (IOException ex) {
            Logger.getLogger(multriControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviar(){
        try {
            salida = new ObjectOutputStream(canal.getOutputStream());
            salida.writeObject(clientes);
        } catch (IOException ex) {
            Logger.getLogger(multriControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void obtenerCanales() throws IOException{
            String nick="";
            entrada = new ObjectInputStream(canal.getInputStream());
            salida = new ObjectOutputStream(canal.getOutputStream());
            
            salida.writeUTF(estados.GET_NICK);
            salida.flush();
            System.out.println("a la espera de recibir orden");
            
            while(true){
                switch(entrada.readUTF()){
                case estados.SEND_NICK:
                    System.out.println("recibo orden de recibir nick");
                    System.out.println("envio peticion nick");
                    //esperamos a recibir el nick
                    nick = entrada.readUTF();
                    System.out.println("recibo nick");
                    //cuando lo recibimos guardamos el nick con el canal asociado con ese nick
                    mapa.put(nick,this);
                    //avisamos al cliente que este a la escucha de recibir la lista de usuarios
                    salida.writeUTF(estados.LISTEN);
                    salida.flush();
                    System.out.println("le digo que este a la escucha");
                    break;
                case estados.GET_USER:
                    System.out.println("recibo orden de enviar la lista");
                    control.envia();
                    break;
                }
            }
            
        
    }
}
