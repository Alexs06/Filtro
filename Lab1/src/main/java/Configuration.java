public class Configuration {

    private double spamProbability=3.0;
    private double spamThreshold=0.9;
    private int trainingSize;

    public double getSpamProbability() {
        return spamProbability;
    }

    public void setSpamProbability(double spamProbability) {
        this.spamProbability = spamProbability;
    }

    public double getspamThreshold() {
        return spamThreshold;
    }

    public void setspamThreshold(double spamThreshold) {
        this.spamThreshold = spamThreshold;
    }

    public int getTrainingSize() {
        return trainingSize;
    }

    public void setTrainingSize(int trainingSize) {
        this.trainingSize = trainingSize;
    }
}
