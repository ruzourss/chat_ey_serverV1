package Log;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;
/**
 *
 * @author Lyderys
 */
public class EscribirXml {
    public static void Escribir(){
        Element config = new Element("config");
        Document doc = new Document(config);
        doc.setRootElement(config);
        Element configuracion = new Element("configuracion");
        configuracion.addContent(new Element("puertom").setText("PUERTO_DE_MENSAJERIA"));
        configuracion.addContent(new Element("puertop").setText("PUERTO_DE_CONTROL"));
        configuracion.addContent(new Element("numuser").setText("NUMERO_DE_USUARIOS"));
        configuracion.addContent(new Element("tambuffer").setText("TAMAÑO_DE_BUFFER"));
        configuracion.addContent(new Element("log").setText("FICHERO_PARA_EL_LOG"));
        configuracion.addContent(new Element("infocon").setText("INFORMACION_DE_CONEXION"));
        doc.getRootElement().addContent(configuracion);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter("config.xml"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
