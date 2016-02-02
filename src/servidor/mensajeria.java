package servidor;
import java.util.ArrayList;
/**
 *
 * @author Tote
 */
public class mensajeria {
    
    //private String mensajes;
    private ArrayList<String> mensajes;
    
    
    public mensajeria() {
        mensajes = new ArrayList<>();
    }
    
    public void escribir(String cadena,ArrayList<cliente> hilos){
        mensajes.add(cadena);
        System.out.println("Recibido: "+cadena);
        //le enviamos el mensaje a los clientes que estan dentro del array,
        //realmnete estamos haciendo aqui el multicast
        for(int i=0;i<hilos.size();i++){
            hilos.get(i).escribir(cadena);
        }
    }
}
