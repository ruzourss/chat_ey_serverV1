
package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

/**
 * clase que extiende de objectInputStream 
 * modificamos la clase para que nos sobreescriba la cabecera cada vez
 * que leemos los datos, asi podemos utilizar siempre el mismo objeto para recibir los datos
 * @author Tautvydas
 */
public class inObject extends ObjectInputStream {
    /**
     * Constructor por defecto
     * @throws IOException 
     */
    public inObject() throws IOException {
        super(null);
    }
    /**
     * Constructor que recibe por parámetro 
     * @param inputStream flujo de entrada
     * @throws IOException 
     */
    public inObject(InputStream inputStream) throws IOException {
        super(inputStream);
    }
    /**
     * método que lee la cabezera sin ningún cambio
     * @throws IOException
     * @throws StreamCorruptedException 
     */
    @Override
    protected void readStreamHeader() throws IOException, StreamCorruptedException {
       
    }
    
    
    
}
