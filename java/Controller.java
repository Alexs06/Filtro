import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import javax.mail.MessagingException;

/**
 * Clase controladora que administra todo el programa
 */
public class Controller {
    private GmailRetriever mails;
    private String bf;
    private BayesianFilter filter;
    private Data data;
    private boolean trained;


    /**
     * Constructor de la clase
     */
    public Controller(){
        trained = false;
        mails = new GmailRetriever();
        filter = new BayesianFilter();
        data = new Data();
        bf = "";
    }

    /**
     * Configura las opciones
     * @param spamProb
     * @param spamThresh
     * @param setSize
     */
    public void configuration(float spamProb, float spamThresh, int setSize){
        filter.setSpamProbab(spamProb);
        filter.setSpamThreshold(spamThresh);
        filter.setSizeOfTrainSet(setSize);
    }

    /**
     * Ejecuta un entrenamiento para el programa
     */
    public Hashtable<String,Float> train() throws IOException, MessagingException {
        trained = true;
        Hashtable<String, Float> prob = new Hashtable<>();
        mails.getMessagesFrom("in:Spam", filter.getSizeOfTrainSet());
        prob = filter.setWord(filter.setProbForEmail("in:Spam"), "in:Spam");
        return prob;
    }

    /**
     * Muestra las palabras y configraciones del usuario
     * @return txt
     */
    public String showData(){
        String data = "";
        return data;
    }

    /**
     * Obtiene la cantidad de mensajes en el buzón escogido
     * @return Array
     * @throws IOException
     */
    public void getMail() throws IOException, MessagingException {
        if(trained){
            List<Email> unread = mails.getMessagesFrom("in:unread", 100);
            Hashtable<String, Float> spamWords = this.train();
            Enumeration<String> e = spamWords.keys();
            boolean find = false;
            for (int i = 0; i < unread.size(); i++) {
                while (e.hasMoreElements() && !find){
                    if (unread.get(i).getBody().contains(e.nextElement())){
                        find = true;
                        float pW =spamWords.get(e.nextElement());
                        float spb = pW/(pW-(1-pW));
                        if (spb > filter.getSpamThreshold()){
                            System.out.println("Is spam bitch");
                        } else {
                            System.out.println("Isn´t spam bitch");
                        }
                    }
                }
            }
        }
    }

    /**
     * Cierra sesión y borra las credendiales del usuario
     */
    public static void logOut(){
        String path = System.getProperty("user.home")+ "//.credentials/gmail-java-Bayesian_Filter//StoredCredential";
        File data = new File(path);
        data.delete();
        System.out.println("Your cretentials has been deleted.");
    }

    /**
     * Inicia sesión con un nombre de usuario
     * @param usr
     * @throws IOException
     */

    public void logIn(String usr) throws IOException {
        bf = System.getProperty("user.home") + "//Documents/BayesianFilter//Users" + "//" + usr; //
        File bayesFilt = new File(bf);
        bayesFilt.mkdirs();
    }

}
