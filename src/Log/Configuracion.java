package Log;

/**
 *
 * @author Lyderys
 */
public class Configuracion {
    public String Config[] = new String[5];
    public String puertom;
    public String puertoc;
    public String numuser;
    public String tambuffer;
    public String log;
    
    public Configuracion(){
        EscribirXml escribirXml = new EscribirXml();
        escribirXml.Escribir();
        LeerXml leer = new LeerXml();
        leer.cargarXml();
        Config=leer.getConfiguracion();
        puertom=this.Config[0];
        puertoc=this.Config[1];
        numuser=this.Config[2];
        tambuffer=this.Config[3];
        log=this.Config[4];
    }
    
}
