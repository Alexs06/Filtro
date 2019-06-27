/**
 * Clase que administra las palabras
 */
public class Word {
    private double probability;
    private int frequencyInSpam;
    private int frequencyInNotSpam;
    private String word;

    /**
     * Constructor de la clase
     */
    public Word() {
        probability = 0;
        frequencyInSpam = 0;
        frequencyInNotSpam = 0;
        word = "";
    }

    /**
     * Palabra
     * @param w tipo palabra
     */
    public Word(String w){
        probability = 0;
        frequencyInSpam = 0;
        frequencyInNotSpam = 0;
        word = w;
    }

    /**
     * Setea la proba de una palabra
     * @param probab proba de la palabra
     */
    public void setProbability(double probab){

        probability = probab;
    }

    /**
     * Setea la frecuencia de una palabra
     * @param frequency frecuencia por mensjae
     */
    public void setFrequencyInSpam(int frequency) {

        frequencyInSpam = frequency;
    }

    /**
     * Frecuencia de una palbra no spam
     * @param frequency Frecuencia
     */
    public void setFrequencyInNotSpam(int frequency) {

        frequencyInNotSpam = frequency;
    }

    /**
     * Setea la palabra
     * @param word palabra
     */
    public void setWord(String word) {

        this.word = word;
    }

    /**
     * Obtiene la proba de la palabra
     * @return La proba :v
     */
    public double getProbability() {

        return probability;
    }

    /**
     * Obtiene la frecuencia
     * @return Frecuencia
     */
    public int getFrequencyInSpam() {

        return frequencyInSpam;
    }

    /**
     * Frecuencia en no spam
     * @return frecuencia
     */
    public int getFrequencyInNotSpam() {

        return frequencyInNotSpam;
    }

    /**
     * Obtiene la palabra
     * @return palabra
     */
    public String getWord() {

        return word;
    }

}