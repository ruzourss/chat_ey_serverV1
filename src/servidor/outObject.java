
package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author Tautvydas
 */
public class outObject extends ObjectOutputStream {

    public outObject() throws IOException {
        super(null);
    }

    public outObject(OutputStream outputStream) throws IOException {
        super(outputStream);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        //no hacemos nada para que no me modifice la cabezera y pueda escribr las veces que quiera con el mismo objeto
    }
    
    
}
