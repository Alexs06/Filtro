public class Term {
    private String word;
    private int frecuencySpam;
    private int frecuencyNoSpam;
    private double probabilitySpam;
    private double probabilityNoSpam;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrecuencySpam() {
        return frecuencySpam;
    }

    public void setFrecuencySpam(int frecuencySpam) {
        this.frecuencySpam = frecuencySpam;
    }

    public int getFrecuencyNoSpam() {
        return frecuencyNoSpam;
    }

    public void setFrecuencyNoSpam(int frecuencyNoSpam) {
        this.frecuencyNoSpam = frecuencyNoSpam;
    }

    public double getProbabilitySpam() {
        return probabilitySpam;
    }

    public void setProbabilitySpam(double probabilitySpam) {
        this.probabilitySpam = probabilitySpam;
    }

    public double getProbabilityNoSpam() {
        return probabilityNoSpam;
    }

    public void setProbabilityNoSpam(double probabilityNoSpam) {
        this.probabilityNoSpam = probabilityNoSpam;
    }
}
