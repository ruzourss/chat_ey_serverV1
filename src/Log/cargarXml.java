package Log;
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
public class cargarXml {
    public void cargarXml()
{
    //Se crea un SAXBuilder para poder parsear el archivo
    SAXBuilder builder = new SAXBuilder();
    File xmlFile = new File( "config.xml" );
    try
    {
        //Se crea el documento a traves del archivo
        Document document = (Document) builder.build( xmlFile );
 
        //Se obtiene la raiz 'tables'
        Element rootNode = document.getRootElement();
 
        //Se obtiene la lista de hijos de la raiz 'tables'
        List list = rootNode.getChildren( "configuracion" );
 
        //Se recorre la lista de hijos de 'tables'
        for (Object list1 : list) {
            //Se obtiene el elemento 'config'
            Element config = (Element) list1;
            //Se obtiene el atributo 'direccion' que esta en el tag 'config'
            String direccionIP = config.getAttributeValue("direccion");
            System.out.println("Direccion del servidor: " + direccionIP );
            //Se obtiene la lista de hijos del tag 'config'
            List lista_campos = config.getChildren();
            System.out.println( "\tDireccion IP\t\tPuerto\t\tValor" );
            //Se recorre la lista de campos
            for (Object lista_campo : lista_campos) {
                //Se obtiene el elemento 'campo'
                Element campo = (Element) lista_campo;
                //Se obtienen los valores que estan entre los tags '<campo></campo>'
                //Se obtiene el valor que esta entre los tags '<nombre></nombre>'
                String direccion = campo.getChildTextTrim("direccion");
                //Se obtiene el valor que esta entre los tags '<tipo></tipo>'
                String puerto = campo.getChildTextTrim("puerto");
                //Se obtiene el valor que esta entre los tags '<valor></valor>'
                String valor = campo.getChildTextTrim("valor");
                System.out.println("\t"+direccion+"\t\t"+puerto+"\t\t"+valor);
            }
        }
    }catch ( IOException | JDOMException io ) {
        System.out.println( io.getMessage() );
    }
}
}
