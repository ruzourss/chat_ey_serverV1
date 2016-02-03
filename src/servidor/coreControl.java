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
 * 
 * @author Tautvydas Bagocius
 */
public class coreControl extends Thread{ 
    private int puerto;
    private ServerSocket server;
    private Socket canal;
    private ArrayList<Socket> contenedorCanales;
    private ArrayList<String> clientes;
    private JTextArea area;

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
    
    private void initServer(){
        try {
            server = new ServerSocket(puerto);
            area.append("Iniciado servicio de control puerto: "+puerto+"\n");
            while(true){
                canal = server.accept();
                contenedorCanales.add(canal);
                new ObjectOutputStream(canal.getOutputStream()).writeObject(clientes);
            }
        } catch (IOException ex) {
            Logger.getLogger(coreControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void envioListadoUsuarios(){ 
        for(int i=0;i<contenedorCanales.size();i++){
           try {
                new ObjectOutputStream(contenedorCanales.get(i).getOutputStream()).writeObject(clientes);
            } catch (IOException ex) {
                Logger.getLogger(coreControl.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }  
    }
    
}
