package athena.api.regression;

import athena.api.ValidationSummary;

import java.io.Serializable;

/**
 * Created by seunghyeon on 5/4/16.
 */
public class RegressionValidationSummary implements Serializable,ValidationSummary {
    double MSE;
double validationTime;
    int entry;

    public int getEntry() {
        return entry;
    }

    public void addEntry(){
        entry++;
    }

    public double getMSE() {
        return MSE;
    }

    public void setMSE(double MSE) {
        this.MSE = MSE;
    }

    public double getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(double validationTime) {
        this.validationTime = validationTime;
    }

    @Override
    public void printResults() {
        System.out.println("training Mean Squared Error = " + MSE);
        System.out.println("Total validation time  = " + validationTime);
    }
}
