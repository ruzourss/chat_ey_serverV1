package servidor;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JTextArea;
/**
 * clase encargada de guardar el historial de mensajes,
 * máximo 25 mensajes, cuando supera ese límite
 * elimina el último mensaje.
 * @author Tote
 */
public class mensajeria {
    
    //Declaramos los objetos que vamos a necesitar
    private final ArrayList<String> mensajes;
    private final JTextArea area;
    private final ArrayList<cliente> clientes;
    private final int tamanioBuffer;
    private Calendar c;
    /**
     * Constructor que recibe el area donde escribir el mensaje recibido
     * @param area area donde se escribe el mensaje
     * @param clientes
     * @param tamanioBuffer
     */
    public mensajeria(JTextArea area, ArrayList<cliente> clientes, int tamanioBuffer) {
        mensajes = new ArrayList<>();
        this.area=area;
        this.clientes=clientes;
        this.tamanioBuffer=tamanioBuffer;
        c=Calendar.getInstance();
    }
    /**
     * Método que es llamado desde un hilo y le envia tres parámetros
     * @param cadena cadena a escribir e renviar a los demás usuarios
     * @param hilo  el hilo que envia el mensaje
     */
    public void escribir(String cadena, Thread hilo){
        int dia = c.get(Calendar.DATE);
        int mes = c.get(Calendar.MONTH);
        int anio = c.get(Calendar.YEAR);
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
        String mensaje = dia+"/"+mes+"/"+anio+" "+hora+":"+minutos+" "+hilo.getName()+">"+cadena;
        area.append(mensaje+"\n");
        if(getMensajes().size()>tamanioBuffer){
            getMensajes().remove(0);
        }
        //añadimos la cadena al array
        mensajes.add(mensaje);
        //le enviamos el mensaje a los clientes que estan dentro del array,
        //realmnete estamos haciendo aqui el multicast
        for(int i=0;i<clientes.size();i++){
            clientes.get(i).escribir(mensaje);
        }
    }
    /**
     * @return the mensajes
     */
    public ArrayList<String> getMensajes() {
        return mensajes;
    }
    
}
