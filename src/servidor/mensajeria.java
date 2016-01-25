package servidor;
import java.util.ArrayList;
/**
 *
 * @author Tote
 */
public class mensajeria {
    
    private String mensajes;

    public mensajeria() {
        mensajes="";
    }
    
    public void escribir(String cadena,ArrayList<cliente> hilos, Thread cliente){
        mensajes+=cadena+"\n";
        System.out.println("Recibido: "+cadena);
        //le enviamos el mensaje a los clientes que estan dentro del array,
        //realmnete estamos haciendo aqui el multicast
        for(int i=0;i<hilos.size();i++){
            //debo realizar la comprobación porque si no se repite el mensaje
            //al que lo envio
            if(!hilos.get(i).getName().equals(cliente.getName())){
                hilos.get(i).escribir(cadena);
            } 
        }
    }
}
