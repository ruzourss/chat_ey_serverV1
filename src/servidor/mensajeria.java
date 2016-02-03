package servidor;
import java.util.ArrayList;
import javax.swing.JTextArea;
/**
 *
 * @author Tote
 */
public class mensajeria {
    
    //private String mensajes;
    private ArrayList<String> mensajes;
    private JTextArea area;
    
    public mensajeria(JTextArea area) {
        mensajes = new ArrayList<>();
        this.area=area;
    }
    
    public void escribir(String cadena,ArrayList<cliente> hilos, Thread hilo){
        area.append(hilo.getName()+">"+cadena);
        if(getMensajes().size()>25){
            getMensajes().remove(0);
        }
        getMensajes().add(cadena);
        System.out.println("Recibido: "+cadena);
        //le enviamos el mensaje a los clientes que estan dentro del array,
        //realmnete estamos haciendo aqui el multicast
        for(int i=0;i<hilos.size();i++){
            hilos.get(i).escribir(cadena);
        }
    }

    /**
     * @return the mensajes
     */
    public ArrayList<String> getMensajes() {
        return mensajes;
    }
    
}
