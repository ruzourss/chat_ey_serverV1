/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Tautvydas
 */
public class conexionControl extends Thread{
    
    private ServerSocket control;
    private Socket canal;
    private int puerto;
    private ArrayList<Socket> clientesControl;
    private ArrayList<String> listaUsuarios;
    private outObject out;

    public conexionControl() {
    }

    public conexionControl(int puerto, ArrayList<Socket> clientesControl,ArrayList<String> listaUsuarios) {
        this.puerto = puerto;
        this.clientesControl = clientesControl;
        this.listaUsuarios=listaUsuarios;
    }
    
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
//                Logger.getLogger(conexionControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
//        for (Socket clientesControl1 : clientesControl) {
//            try {
//                out = new outObject(clientesControl1.getOutputStream());
//                out.writeObject(listaUsuarios);
//                out.flush();
//            } catch (IOException ex) {
//                //en caso si se ha desconectado el cliente lo eliminamos de la lista
//                clientesControl.remove(clientesControl1);
//                enviar();
////                Logger.getLogger(conexionControl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//        }
    }
    
    
    @Override
    public void run() {
        initServer();
    }
    
    
    
    
}
