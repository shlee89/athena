package org.onosproject.athena;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.onosproject.athena.database.AdvancedFeatureConstraintType;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaField;
import org.onosproject.athena.database.DatabaseType;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AdvancedFeatureConstraintValue;
import org.onosproject.athena.database.AthenaFeatureRequestrType;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.OnlineEvent;
import org.onosproject.athena.database.OnlineEventTable;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.RichFeatureCalculator;
import org.onosproject.athena.database.RichFeatureName;
import org.onosproject.athena.database.SortedUserDefinedFeatures;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.athena.feature.AggregateStatisticsFeature;
import org.onosproject.athena.feature.ErrorMessageFeature;
import org.onosproject.athena.feature.FeatureIndex;
import org.onosproject.athena.feature.FlowRemovedFeature;
import org.onosproject.athena.feature.FlowStatisticsFeature;
import org.onosproject.athena.feature.PacketInFeature;
import org.onosproject.athena.feature.PortStatisticsFeature;
import org.onosproject.athena.feature.PortStatusFeature;
import org.onosproject.athena.feature.QueueStatisticsFeature;
import org.onosproject.athena.feature.TableStatisticsFeature;
import org.onosproject.athena.feature.UnitAggregateStatistics;
import org.onosproject.athena.feature.UnitErrorMessageInformation;
import org.onosproject.athena.feature.UnitFeature;
import org.onosproject.athena.feature.UnitFlowRemovedInformation;
import org.onosproject.athena.feature.UnitFlowStatistics;
import org.onosproject.athena.feature.UnitPacketInInformation;
import org.onosproject.athena.feature.UnitPortStatistics;
import org.onosproject.athena.feature.UnitPortStatusInformation;
import org.onosproject.athena.feature.UnitQueueStatistics;
import org.onosproject.athena.feature.UnitTableStatistics;
import org.onosproject.athena.learning.Algorithm;
import org.onosproject.athena.learning.AthenaModel;
import org.onosproject.athena.learning.AthenaModelId;
import org.onosproject.athena.learning.MachineLearningEvent;
import org.onosproject.athena.learning.MachineLearningEventMessage;
import org.onosproject.athena.learning.MachineLearningRequester;
import org.onosproject.net.flow.FlowRule;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by seunghyeon on 1/21/16.
 */
public class SerializerHelper {
    public SerializerHelper() {
    }

    public Kryo initializeSerializeKryo() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(true);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(Date.class, 10);
        kryo.register(SimpleDateFormat.class, 12);
        kryo.register(QueryIdentifier.class, 13);
        kryo.register(AthenaFeatureRequester.class, 14);
        kryo.register(AthenaFeatureRequestrType.class, 15);
//        FieldSerializer<?> serializer = new FieldSerializer<FeatureConstraint>(kryo, FeatureConstraint.class);
//        kryo.register(FeatureConstraint.class, serializer, 16);



        kryo.register(FeatureConstraint.class, new Serializer<FeatureConstraint>() {
            @Override
            public void write(Kryo kryo, Output output, FeatureConstraint object) {
                if (object.getFeatureConstraintType() != null) {
                    if (FeatureConstraintType.FEATURE == object.getFeatureConstraintType()) {
                        kryo.writeClassAndObject(output, object.getFeatureName());
                    } else {
                        kryo.writeClassAndObject(output, object.getFeatureName());
                    }
                }
                kryo.writeObjectOrNull(output, object.getFeatureName(), AthenaField.class);
                kryo.writeObjectOrNull(output, object.getFeatureConstraintOperator(), FeatureConstraintOperator.class);
                kryo.writeObjectOrNull(output, object.getFeatureConstraintOperatorType(),
                        FeatureConstraintOperatorType.class);
                kryo.writeObjectOrNull(output, object.getLocation(), String.class);
                kryo.writeObjectOrNull(output, object.getFeatureConstraintType(), FeatureConstraintType.class);
                kryo.writeObjectOrNull(output, object.getDataRequestObjectValueList(), ArrayList.class);

            }

            @Override
            public FeatureConstraint read(Kryo kryo, Input input, Class<FeatureConstraint> type) {
                AthenaFeatureField aff;
                AthenaIndexField aif;
                FeatureConstraint featureConstraint;
                FeatureConstraintOperator featureConstraintOperator =
                        kryo.readObjectOrNull(input, FeatureConstraintOperator.class);
                FeatureConstraintOperatorType featureConstraintOperatorType =
                        kryo.readObjectOrNull(input, FeatureConstraintOperatorType.class);
                String location = kryo.readObjectOrNull(input, String.class);
                FeatureConstraintType featureConstraintType =
                        (FeatureConstraintType) kryo.readObjectOrNull(input, FeatureConstraintType.class);
                List<TargetAthenaValue> dataRequestObjectValueList = kryo.readObjectOrNull(input, ArrayList.class);

                if (featureConstraintType != null) {
                    if (FeatureConstraintType.FEATURE == featureConstraintType) {
                        aff = (AthenaFeatureField) kryo.readClassAndObject(input);
                        featureConstraint = new FeatureConstraint(featureConstraintType,
                                featureConstraintOperatorType,
                                featureConstraintOperator,
                                aff);
                    } else {
                        aif = (AthenaIndexField) kryo.readClassAndObject(input);
                        featureConstraint = new FeatureConstraint(featureConstraintType,
                                featureConstraintOperatorType,
                                featureConstraintOperator,
                                aif);
                    }
                } else {
                    featureConstraint = new FeatureConstraint(featureConstraintOperatorType,
                            featureConstraintOperator);
                }
                for (TargetAthenaValue obj : dataRequestObjectValueList) {
                    featureConstraint.appenValue(obj);
                }

                return featureConstraint;
            }
        }, 16);

        kryo.register(FeatureConstraintOperator.class, 17);
        kryo.register(FeatureConstraintOperatorType.class, 18);
        kryo.register(FeatureConstraintType.class, 19);
        kryo.register(AthenaFeatureField.class, 20);
        kryo.register(AthenaIndexField.class, 21);
        kryo.register(List.class, new CollectionSerializer(), 22);
        kryo.register(ArrayList.class, new CollectionSerializer(), 23);
        kryo.register(AdvancedFeatureConstraint.class, 24);
        kryo.register(AdvancedFeatureConstraintType.class, 25);
        kryo.register(AdvancedFeatureConstraintValue.class, 26);
        kryo.register(ExternalDataType.class, 27);
        kryo.register(LinkedHashMap.class, new MapSerializer(), 28);
        kryo.register(HashMap.class, new MapSerializer(), 29);
        kryo.register(BigInteger.class, 30);
        kryo.register(Long.class, 31);
        kryo.register(String.class, 32);
        kryo.register(Integer.class, 33);
        kryo.register(FlowRule.class, 34);
        kryo.register(SerializerWrapper.class, 35);
        kryo.register(AthenaFeatures.class, 36);
        kryo.register(OnlineEvent.class, 37);
        kryo.register(short.class, 38);
        kryo.register(DatabaseType.class, 39);
        kryo.register(OnlineEventTable.class, 40);
        kryo.register(RichFeatureCalculator.class, 41);
        kryo.register(RichFeatureName.class, 42);
        kryo.register(SortedUserDefinedFeatures.class, 43);
        kryo.register(AggregateStatisticsFeature.class, 44);
        kryo.register(ErrorMessageFeature.class, 45);
        kryo.register(FeatureIndex.class, 46);
        kryo.register(FlowRemovedFeature.class, 47);
        kryo.register(FlowStatisticsFeature.class, 48);

        kryo.register(PacketInFeature.class, 49);
        kryo.register(PortStatisticsFeature.class, 50);
        kryo.register(PortStatusFeature.class, 51);
        kryo.register(QueueStatisticsFeature.class, 52);
        kryo.register(TableStatisticsFeature.class, 53);
        kryo.register(UnitAggregateStatistics.class, 54);
        kryo.register(UnitErrorMessageInformation.class, 55);
        kryo.register(UnitFeature.class, 56);
        kryo.register(UnitFlowRemovedInformation.class, 57);
        kryo.register(UnitFlowStatistics.class, 58);
        kryo.register(UnitPacketInInformation.class, 59);
        kryo.register(UnitPortStatistics.class, 60);
        kryo.register(UnitPortStatusInformation.class, 61);
        kryo.register(UnitQueueStatistics.class, 62);
        kryo.register(UnitTableStatistics.class, 63);
        kryo.register(Algorithm.class, 64);
        kryo.register(AthenaModel.class, 65);
        kryo.register(AthenaModelId.class, 66);
        kryo.register(MachineLearningEvent.class, 67);
        kryo.register(MachineLearningEventMessage.class, 68);
        kryo.register(MachineLearningRequester.class, 69);
        kryo.register(Object.class, 70);
        kryo.register(TargetAthenaValue.class, 71);
        kryo.register(AthenaField.class, 72);
        return kryo;
    }
        /*
    public Kryo initializeSerializeKryo() {
        MapSerializer serializer = new MapSerializer();
        CollectionSerializer listSerializer = new CollectionSerializer();
        Kryo kry = new Kryo();

        kry.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kry.register(QueryIdentifier.class);
        kry.register(HashMap.class, serializer);
        kry.register(LinkedHashMap.class, serializer);
        kry.register(Long.class);
        kry.register(Integer.class);
        kry.register(Date.class);
        kry.register(List.class);
        kry.register(SimpleDateFormat.class);
        kry.register(ArrayList.class, listSerializer);
        kry.register(LinkedList.class, listSerializer);
        kry.register(ExternalDataType.class);
        kry.register(short.class);
        kry.register(Short.class);


        kry.register(AthenaFeatureRequester.class);
        kry.register(AthenaFeatureRequestrType.class);
        kry.register(DatabaseType.class);
        kry.register(AdvancedFeatureConstraint.class);
        kry.register(AdvancedFeatureConstraintValue.class);
        kry.register(FeatureConstraint.class);
        kry.register(DatabaseType.class);
        kry.register(AthenaFeatures.class);
        kry.register(AthenaFeatureField.class);
        kry.register(AthenaIndexField.class);
        kry.register(OnlineEvent.class);
        kry.register(OnlineEventTable.class);
        kry.register(QueryIdentifier.class);
        kry.register(FeatureConstraintOperator.class);
        kry.register(RichFeatureCalculator.class);
        kry.register(RichFeatureName.class);
        kry.register(SortedUserDefinedFeatures.class);
        kry.register(ExternalDataType.class);
        kry.register(SerializerWrapper.class);
        kry.register(AggregateStatisticsFeature.class);
        kry.register(ErrorMessageFeature.class);
        kry.register(FeatureIndex.class);
        kry.register(FlowRemovedFeature.class);
        kry.register(FlowStatisticsFeature.class);
        kry.register(PacketInFeature.class);
        kry.register(PortStatisticsFeature.class);
        kry.register(PortStatusFeature.class);
        kry.register(QueueStatisticsFeature.class);
        kry.register(TableStatisticsFeature.class);
        kry.register(UnitAggregateStatistics.class);
        kry.register(UnitErrorMessageInformation.class);
        kry.register(UnitFeature.class);
        kry.register(UnitFlowRemovedInformation.class);
        kry.register(UnitFlowStatistics.class);
        kry.register(UnitPacketInInformation.class);
        kry.register(UnitPortStatistics.class);
        kry.register(UnitPortStatusInformation.class);
        kry.register(UnitQueueStatistics.class);
        kry.register(UnitTableStatistics.class);
        kry.register(Algorithm.class);
        kry.register(AthenaModel.class);
        kry.register(AthenaModelId.class);
        kry.register(MachineLearningEvent.class);
        kry.register(MachineLearningEventMessage.class);
        kry.register(MachineLearningRequester.class);
        return kry;
    }*/
}
