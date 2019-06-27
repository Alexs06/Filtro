import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Jimmy Acuña, Luis Carvajal
 *
 */

/**
 * Clase que ejecuta el programa completo
 */
public class Main {
    private  static  Controller control;
    private static Interface interf;
    private static GmailRetriever gm;

    /**
     * Constructor de la clase
     */
    public Main(){
        control = new Controller();
        interf = new Interface(control);
        gm = new GmailRetriever();
    }

    /**
     * Llama a la interfaz
     * @throws IOException
     */
    public void start() throws IOException, MessagingException {
        interf.menu();
    }

    /**
     * Ejecuta el programa
     * @param args
     * @throws IOException
     * @throws MessagingException
     */
    public static void main(String[] args) throws IOException, MessagingException {
        Main aMain = new Main();
        aMain.start();

    }
}
