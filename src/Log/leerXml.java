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
public class LeerXml {
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
            //Se obtiene la lista de hijos del tag 'config'
            List lista_campos = config.getChildren();
            System.out.println( "\t\tPuerto de mensajeria\t\tPuerto de control"
                    + "\t\tNumero de usuarios\t\tTamaño de Buffer\t\tLog\t\tInfo. Conexion" );
            //Se recorre la lista de campos
            for (Object lista_campo : lista_campos) {
                //Se obtiene el elemento 'campo'
                Element campo = (Element) lista_campo;
                //Se obtiene el valor que esta entre los tags '<puertom></puertom>'
                String puertom = campo.getChildTextTrim("puertom");
                //Se obtiene el valor que esta entre los tags '<puertoc></puertoc>'
                String puertoc = campo.getChildTextTrim("puertoc");
                //Se obtiene el valor que esta entre los tags '<numuser></numuser>'
                String numuser = campo.getChildTextTrim("numuser");
                //Se obtiene el valor que esta entre los tags '<tambuffer></tambuffer>'
                String tambuffer = campo.getChildTextTrim("tambuffer");
                //Se obtiene el valor que esta entre los tags '<log></log>'
                String log = campo.getChildTextTrim("log");
                //Se obtiene el valor que esta entre los tags '<infocon></infocon>'
                String infocon = campo.getChildTextTrim("infocon");
                System.out.println("\t"+puertom+"\t\t"+puertoc+"\t\t"+numuser+"\t\t"+tambuffer+"\t\t"+log+"\t\t"+infocon);
            }
        }
    }catch ( IOException | JDOMException io ) {
        System.out.println( io.getMessage() );
    }
}
}
