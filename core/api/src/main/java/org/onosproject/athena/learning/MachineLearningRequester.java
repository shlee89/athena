package org.onosproject.athena.learning;

import org.onosproject.athena.database.AthenaFeatureRequester;

/**
 * Index could not be processed !(Filtering... !) Use "Remove".
 *
 * Created by seunghyeon on 9/2/15.
 */
public class MachineLearningRequester {

    //on-line or off-line
    private Category category;

    //supervised or unsupervised
    private Method method;

    private Algorithm algorithm;

    //could be used for building model
    private AthenaFeatureRequester baseAthenaFeatureRequester;

    //unlabeled data
    private AthenaFeatureRequester testAthenaFeatureRequester;

    private AthenaFeatureRequester onlineAthenaFeatureRequester;

    private ResultType resultType;

    public MachineLearningRequester(Category category,
                                    Method method,
                                    Algorithm algorithm,
                                    AthenaFeatureRequester baseAthenaFeatureRequester,
                                    AthenaFeatureRequester onlineAthenaFeatureRequester,
                                    ResultType resultType) {
        this.category = category;
        this.method = method;
        this.algorithm = algorithm;
        this.baseAthenaFeatureRequester = baseAthenaFeatureRequester;
        this.onlineAthenaFeatureRequester = onlineAthenaFeatureRequester;
        this.resultType = resultType;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setBaseAthenaFeatureRequester(AthenaFeatureRequester baseAthenaFeatureRequester) {
        this.baseAthenaFeatureRequester = baseAthenaFeatureRequester;
    }

    public void setTestAthenaFeatureRequester(AthenaFeatureRequester testAthenaFeatureRequester) {
        this.testAthenaFeatureRequester = testAthenaFeatureRequester;
    }

    public void setOnlineAthenaFeatureRequester(AthenaFeatureRequester onlineAthenaFeatureRequester) {
        this.onlineAthenaFeatureRequester = onlineAthenaFeatureRequester;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public Category getCategory() {
        return category;
    }

    public Method getMethod() {
        return method;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public AthenaFeatureRequester getBaseAthenaFeatureRequester() {
        return baseAthenaFeatureRequester;
    }

    public AthenaFeatureRequester getTestAthenaFeatureRequester() {
        return testAthenaFeatureRequester;
    }

    public AthenaFeatureRequester getOnlineAthenaFeatureRequester() {
        return onlineAthenaFeatureRequester;
    }

    public ResultType getResultType() {
        return resultType;
    }
}
