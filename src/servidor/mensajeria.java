package servidor;
import java.util.ArrayList;
import javax.swing.JTextArea;
/**
 * clase encargada de guardar el historial de mensajes,
 * máximo 25 mensajes, cuando supera ese límite
 * elimina el último mensaje.
 * @author Tote
 */
public class mensajeria {
    
    //Declaramos los objetos que vamos a necesitar
    private ArrayList<String> mensajes;
    private JTextArea area;
    private ArrayList<cliente> clientes;
    /**
     * Constructor que recibe el area donde escribir el mensaje recibido
     * @param area area donde se escribe el mensaje
     */
    public mensajeria(JTextArea area, ArrayList<cliente> clientes) {
        mensajes = new ArrayList<>();
        this.area=area;
        this.clientes=clientes;
    }
    /**
     * Método que es llamado desde un hilo y le envia tres parámetros
     * @param cadena cadena a escribir e renviar a los demás usuarios
     * @param hilo  el hilo que envia el mensaje
     */
    public void escribir(String cadena, Thread hilo){
        area.append(hilo.getName()+">"+cadena+"\n");
        if(getMensajes().size()>25){
            getMensajes().remove(0);
        }
        //añadimos la cadena al array
        mensajes.add(cadena);
        //le enviamos el mensaje a los clientes que estan dentro del array,
        //realmnete estamos haciendo aqui el multicast
        for(int i=0;i<clientes.size();i++){
            clientes.get(i).escribir(cadena);
        }
    }
    /**
     * @return the mensajes
     */
    public ArrayList<String> getMensajes() {
        return mensajes;
    }
    
}
