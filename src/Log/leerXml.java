package Log;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;         // |
import org.jdom2.Element;          // |\ Librerías
import org.jdom2.JDOMException;    // |/ JDOM
import org.jdom2.input.SAXBuilder; // |
/**
 *
 * @author Lyderys
 */
public class LeerXml {
    public static final String PROP_PUERTOM = "PROP_PUERTOM";
    public static final String PROP_PUERTOC = "PROP_PUERTOC";
    public static final String PROP_NUMUSER = "PROP_NUMUSER";
    public static final String PROP_TAMBUFFER = "PROP_TAMBUFFER";
    public static final String PROP_LOG = "PROP_LOG";
    public static final String PROP_INFOCON = "PROP_INFOCON";
    private String puertom;
    private String puertoc;
    private String numuser;
    private String tambuffer;
    private String log;
    private String infocon;
    private final String Configuracion[] = new String[5];
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    private final transient VetoableChangeSupport vetoableChangeSupport = new java.beans.VetoableChangeSupport(this);

    public void cargarXml()
{
    //Se crea un SAXBuilder para poder parsear el archivo
    SAXBuilder builder = new SAXBuilder();
    File xmlFile = new File( "config.xml" );
    try
    {   /**
        * El archivo se creara en el directorio raíz 
        */
        //Se crea el documento a traves del archivo
        Document document = (Document) builder.build( xmlFile );
 
        //Se obtiene la raiz 'config'
        Element rootNode = document.getRootElement();
 
        //Se obtiene la lista de hijos de la raiz 'config'
        List list = rootNode.getChildren("configuracion");
        //Se recorre la lista de hijos de 'config'
        for (Object list1 : list) {
            //Se obtiene el elemento 'config'
            Element config = (Element) list1;
            //Se obtiene la lista de hijos del tag 'config'
            List lista_campos = config.getChildren();
//            System.out.println( "\t\tPuerto de mensajeria\t\tPuerto de control"
//                    + "\t\tNumero de usuarios\t\tTamaño de Buffer\t\tLog\t\tInfo. Conexion" );
            //Se recorre la lista de campos
            for (Object lista_campo : lista_campos) {
                //Se obtiene el elemento 'campo'
                Element campo = (Element) lista_campo;
                //Se obtiene el valor que esta entre los tags '<puertom></puertom>'
                this.puertom = campo.getChildTextTrim("puertom");
                //Se obtiene el valor que esta entre los tags '<puertoc></puertoc>'
                this.puertoc = campo.getChildTextTrim("puertoc");
                //Se obtiene el valor que esta entre los tags '<numuser></numuser>'
                this.numuser = campo.getChildTextTrim("numuser");
                //Se obtiene el valor que esta entre los tags '<tambuffer></tambuffer>'
                this.tambuffer = campo.getChildTextTrim("tambuffer");
                //Se obtiene el valor que esta entre los tags '<log></log>'
                this.log = campo.getChildTextTrim("log");
                //Se obtiene el valor que esta entre los tags '<infocon></infocon>'
//                this.infocon = campo.getChildTextTrim("infocon");
                System.out.println("\t"+puertom+"\t\t"+puertoc+"\t\t"+numuser+"\t\t"+tambuffer+"\t\t"+log);
            }
        }
    }catch ( IOException | JDOMException io ) {
        System.out.println( io.getMessage() );
    }
}

    /**
     * @return the puertom
     */
    public String getPuertom() {
        return puertom;
    }

    /**
     * @return the puertoc
     */
    public String getPuertoc() {
        return puertoc;
    }

    /**
     * @return the numuser
     */
    public String getNumuser() {
        return numuser;
    }

    /**
     * @return the tambuffer
     */
    public String getTambuffer() {
        return tambuffer;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @return the infocon
     */
    public String getInfocon() {
        return infocon;
    }

    /**
     * @return the Configuracion
     */
    public String[] getConfiguracion() {
        this.Configuracion[0]=puertom;
        this.Configuracion[1]=puertoc;
        this.Configuracion[2]=numuser;
        this.Configuracion[3]=tambuffer;
        this.Configuracion[4]=log;
        return Configuracion;
    }
}
