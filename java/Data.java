import java.io.*;
import java.util.Hashtable;

/**
 * Clase que se encarga de almacenar todos los datos del usuario
 */
public class Data {
    /**
     * Constructor de la clase
     */
    public Data(){
    }

    /**
     * Almacena los datos del usuario en://Documents/BayesianFilter//Users
     * @param usr
     * @param spamProb
     * @param spamThresh
     * @param setSize
     * @param prob
     * @throws IOException
     */
    public void store(String usr, float spamProb, float spamThresh, int setSize, Hashtable<String, Float> prob, Hashtable<String, Integer> freq) throws IOException {

        String path = System.getProperty("user.home") + "//Documents/BayesianFilter//Users" + "//" + usr + "/";
        File uData = new File(path + "/Probability Data.txt"); //Usaer data, hacemos un archivo
        FileWriter pTab = new FileWriter(uData, true); // no se que para poder escribir
        pTab.write("Probability Table: " + prob.toString() + '\n');// escribimos

        String path2 = System.getProperty("user.home") + "//Documents/BayesianFilter//Users" + "//" + usr + "/";
        File uData2 = new File(path2 + "/Frequency Data.txt"); //Usaer data, hacemos un archivo
        FileWriter fTab = new FileWriter(uData2, true); // no se que para poder escribir
        fTab.write("Frequency Table: " + prob.toString() + '\n');// escribimos

        pTab.append("SpamProb: " + spamProb + '\n'+ "SpamThresh: " + spamThresh + '\n' + "SetSize: " + setSize);

        fTab.close();
        pTab.close();
    }

    /**
     * Carga los datos guardados por el usario.
     * @param usr
     * @throws IOException
     */
    public void load(String usr) throws IOException {

        String path = System.getProperty("user.home") + "//Documents/BayesianFilter//Users" + "//" + usr + "/";
        //File rData = new File(path + "/hashData.txt");
        FileReader read = new FileReader(path + "/hashData.txt");
        BufferedReader content = new BufferedReader(read);
        try {
            content.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Borra todos los datos del usuario
     * @param usr
     */
    public void delete(String usr){
        String path = System.getProperty("user.home") + "//Documents/BayesianFilter/Users" + "/" + usr + "//hashData.txt";
        File toDelete = new File(path);
        toDelete.delete();
    }
}
