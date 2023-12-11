package lab.t056.dataplatform.traffic;

import lab.t056.dataplatform.traffic.component.timestampassigner.VehicleEventTimestampAssigner;
import lab.t056.dataplatform.traffic.component.transformationfunction.TimeBasedKeyedWindowedAverageFunction;
import lab.t056.dataplatform.traffic.component.watermarkgenerator.CustomWatermarkGenerator;
import lab.t056.dataplatform.traffic.component.watermarkgenerator.CustomWatermarkGeneratorSupplier;
import lab.t056.dataplatform.traffic.entity.event.PerVehicleTypeTrafficMeterEvent;
import lab.t056.dataplatform.traffic.component.keyselector.VehicleTypeKeySelector;
import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.Serializable;


// TODO: Think about the fact that all dependencies of this class needs to be Serializable,
//  for distributed processing...
//  fields, references, etc...
//  need to use no-arg constructor, getter and setter...
public class TrafficEventStreamProcessor {
  // TODO: Figure out why we need the stream processor to be Serializable?
  private final long maxWatermarkDelayMilli = 5000L;
  private final Time windowSize = Time.minutes(1);  // TODO: Figure out why

  public void runFlow(StreamExecutionEnvironment env) throws Exception {
    // Get source data
    DataStream<VehicleEvent> vehicleEvents = env
        .addSource(new TrafficEventGenerator(3, 1))
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                // TODO: Figure out why CustomWatermarkGenerator is NOT serializable and thus causing the processor class to be NOT serializable as a consequence.
//                .<VehicleEvent>forGenerator(ctx -> new CustomWatermarkGenerator(maxWatermarkDelayMilli))
                .<VehicleEvent>forGenerator(new CustomWatermarkGeneratorSupplier())  // Using the supplier, the generator is made Serializable.
                // Watermark generator needs to be Serializable for when it is shipped to workers in distributed setup.
                // TODO: Learn about Serialization in Flink.
                .withTimestampAssigner(new VehicleEventTimestampAssigner())
        );


    // Transform events
    vehicleEvents
        .keyBy(new VehicleTypeKeySelector())
        .window(
            TumblingEventTimeWindows.of(Time.minutes(1))
        )
        .apply(new TimeBasedKeyedWindowedAverageFunction())
        .map(PerVehicleTypeTrafficMeterEvent::toString)
        .print();

    env.execute();
  }
}
