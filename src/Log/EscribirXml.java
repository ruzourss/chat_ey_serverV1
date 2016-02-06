package Log;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;
/**
 *
 * @author Lyderys
 */
public class EscribirXml {
    String sFichero = "config.xml";
    File fichero = new File(sFichero);
    public void Escribir(){
        Element config = new Element("config");
        Document doc = new Document(config);
        //doc.setRootElement(config);
        Element configuracion = new Element("configuracion");
        Element campos = new Element("campos");
        configuracion.addContent(campos);
        campos.addContent(new Element("puertom").setText("PUERTO_DE_MENSAJERIA"));
        campos.addContent(new Element("puertoc").setText("PUERTO_DE_CONTROL"));
        campos.addContent(new Element("numuser").setText("NUMERO_DE_USUARIOS"));
        campos.addContent(new Element("tambuffer").setText("TAMAÑO_DE_BUFFER"));
        campos.addContent(new Element("log").setText("FICHERO_PARA_EL_LOG"));
        campos.addContent(new Element("infocon").setText("INFORMACION_DE_CONEXION"));
        doc.getRootElement().addContent(configuracion);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        if(fichero.exists()){
            
            //codigo para arrancar la carga de datos
        }else{
            
           try {
            xmlOutput.output(doc, new FileWriter("config.xml"));
            } catch (IOException ex) {
            System.out.println(ex.getMessage());
            } 
        }
        
    }
}
