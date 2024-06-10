package nbuttr.finance_bot.dto;

public class PredictionDto {
    private Object prediction;

    public Object getPrediction() {
        return prediction;
    }

    public void setPrediction(Object prediction) {
        this.prediction = prediction;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "prediction=" + prediction +
                '}';
    }
}
