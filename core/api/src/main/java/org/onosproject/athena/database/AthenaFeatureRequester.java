package org.onosproject.athena.database;


import java.util.ArrayList;
import java.util.List;


/**
 * The AthenaFeatureRequester provides an interface to request data with online/ batch manner.
 * The inital prototpye of the AthenaFeatureRequester supports heierarical query statements.
 * AthenaFeatureRequesterType: Online events, batch data in a database.
 * FeatureConstraint: User-defined constraints.
 * AdvancedFeatureConstraint: Data-preprocessing parameters (Sorting, aggregation, and limiting)
 * Created by seunghyeon on 9/1/15.
 */
public class AthenaFeatureRequester {
    private AthenaFeatureRequestrType athenaFeatureRequestrType;

    private FeatureConstraint featureConstraint;

    private AdvancedFeatureConstraint dataRequestAdvancedOptions;


    public AthenaFeatureRequester(AthenaFeatureRequestrType athenaFeatureRequestrType,
                                  FeatureConstraint featureConstraint,
                                  AdvancedFeatureConstraint dataRequestAdvancedOptions) {
        this.athenaFeatureRequestrType = athenaFeatureRequestrType;
        this.featureConstraint = featureConstraint;
        this.dataRequestAdvancedOptions = dataRequestAdvancedOptions;
    }

    public AthenaFeatureRequester(AthenaFeatureRequestrType athenaFeatureRequestrType,
                                  FeatureConstraint featureConstraint,
                                  AdvancedFeatureConstraint dataRequestAdvancedOptions,
                                  List<String> listOfFeatures) {
        this.athenaFeatureRequestrType = athenaFeatureRequestrType;
        this.featureConstraint = featureConstraint;
        this.dataRequestAdvancedOptions = dataRequestAdvancedOptions;
        this.listOfFeatures = listOfFeatures;
    }


    public AdvancedFeatureConstraint getDataRequestAdvancedObject() {
        return dataRequestAdvancedOptions;
    }


    //List of extracted features.
    private List<String> listOfFeatures = new ArrayList<>();

    public AthenaFeatureRequester() {
    }

    public AthenaFeatureRequester(AthenaFeatureRequestrType athenaFeatureRequestrType,
                                  FeatureConstraint featureConstraint,
                                  List<String> listOfFeatures) {
        this.athenaFeatureRequestrType = athenaFeatureRequestrType;
        this.featureConstraint = featureConstraint;
        this.listOfFeatures = listOfFeatures;
    }

    public AthenaFeatureRequestrType getAthenaFeatureRequestrType() {
        return athenaFeatureRequestrType;
    }

    public FeatureConstraint getFeatureConstraint() {
        return featureConstraint;
    }

    public List<String> getListOfFeatures() {
        return listOfFeatures;
    }

    public void setAthenaFeatureRequestrType(AthenaFeatureRequestrType athenaFeatureRequestrType) {
        this.athenaFeatureRequestrType = athenaFeatureRequestrType;
    }

    public void setFeatureConstraint(FeatureConstraint featureConstraint) {
        this.featureConstraint = featureConstraint;
    }

    public void setListOfFeatures(List<String> listOfFeatures) {
        this.listOfFeatures = listOfFeatures;
    }
}
