//package lab.t056.dataplatform.traffic.archive;
//
//import lab.t056.dataplatform.traffic.source.TrafficEventGenerator;
//import lab.t056.dataplatform.traffic.entity.enums.DriverLabel;
//import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
//import org.apache.flink.api.common.functions.AbstractRichFunction;
//import org.apache.flink.api.common.functions.ReduceFunction;
//import org.apache.flink.api.common.state.ValueState;
//import org.apache.flink.api.common.state.ValueStateDescriptor;
//import org.apache.flink.api.java.functions.KeySelector;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.KeyedStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
//import org.apache.flink.streaming.api.functions.ProcessFunction;
//import org.apache.flink.util.Collector;
//
//import java.time.Instant;
//
//public class TrafficEventStreamProcessor {
//  static class VehicleTypeKeySelector implements KeySelector<VehicleEvent, String> {
//    @Override
//    public String getKey(VehicleEvent trafficEvent) throws Exception {
//      return trafficEvent.getVehicleType().name();
//    }
//  }
//
//  static class ProcessKeyedTrafficStream extends ProcessFunction<VehicleEvent, LabeledVehicleEvent> {
//    @Override
//    public void processElement(VehicleEvent trafficEvent,
//                               ProcessFunction<VehicleEvent, LabeledVehicleEvent>.Context context,
//                               Collector<LabeledVehicleEvent> collector) throws Exception {
//      collector.collect(
//        new LabeledVehicleEvent(
//            trafficEvent.getVehicleType(),
//            trafficEvent.getSpeed(),
//            trafficEvent.getEventTimestamp(),
//            DriverLabel.NORMAL_DRIVER,
//            trafficEvent.getPassengers(),
//            context.toString(),
//            null,
//            null));
//    }
//  }
//
//  static class ProcessKeyedTrafficStreamByVehicleTypeKey
//      extends KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent> {
//    @Override
//    public void processElement(VehicleEvent trafficEvent,
//                               KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent>.Context context,
//                               Collector<LabeledVehicleEvent> collector) throws Exception {
//      collector.collect(
//          new LabeledVehicleEvent(
//              trafficEvent.getVehicleType(),
//              trafficEvent.getSpeed(),
//              trafficEvent.getEventTimestamp(),
//              DriverLabel.NORMAL_DRIVER,
//              trafficEvent.getPassengers(),
//              context.getCurrentKey(),
//              null,
//              null));
//    }
//  }
//
//  static class ProcessKeyedTrafficStreamByVehicleTypeWithState
//      extends KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent> {
//
//    ValueState<Integer> stateCount;
//    ValueState<Float> stateRunningAvgSpeed;
//
//    @Override
//    public void processElement(VehicleEvent trafficEvent,
//                               KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent>.Context context,
//                               Collector<LabeledVehicleEvent> collector) throws Exception {
//
//      // Initialize states
//      if (stateCount.value() == null) {
//        stateCount.update(0);
//      }
//      if (stateRunningAvgSpeed.value() == null) {
//        stateRunningAvgSpeed.update((float) 0);
//      }
//
//      // Get new states
//      int newStateCount = stateCount.value() + 1;
//      float newStateRunningAvgState = (float)(stateRunningAvgSpeed.value() + trafficEvent.getSpeed()) / newStateCount;
//
//      stateCount.update(newStateCount);
//      stateRunningAvgSpeed.update(newStateRunningAvgState);
//
//      DriverLabel label;
//      if (trafficEvent.getSpeed() > newStateRunningAvgState) {
//        label = DriverLabel.FASTER_DRIVER;
//      } else if (trafficEvent.getSpeed() == newStateRunningAvgState) {
//        label = DriverLabel.NORMAL_DRIVER;
//      } else {
//        label = DriverLabel.SLOWER_DRIVER;
//      }
//
//      collector.collect(
//          new LabeledVehicleEvent(
//            trafficEvent.getVehicleType(),
//            trafficEvent.getSpeed(),
//            trafficEvent.getEventTimestamp(),
//            label,
//            trafficEvent.getPassengers(),
//            context.getCurrentKey(),
//            null,
//            null
//        )
//      );
//    }
//
//    @Override
//    public void open(Configuration parameters) throws Exception {
//      stateCount
//          = getRuntimeContext().getState(new ValueStateDescriptor<>("count", Integer.class));
//      stateRunningAvgSpeed
//          = getRuntimeContext().getState(new ValueStateDescriptor<>("running-avg-speed", Float.class));
//    }
//
//    @Override
//    public void close() throws Exception {
//      if (stateCount != null) {
//        stateCount.clear();
//      }
//
//      if (stateRunningAvgSpeed != null) {
//        stateRunningAvgSpeed.clear();
//      }
//    }
//  }
//
//  AbstractRichFunction processorSelection = new ProcessKeyedTrafficStream();
////  AbstractRichFunction processorSelection = new ProcessKeyedTrafficStreamByVehicleTypeKey();
////  AbstractRichFunction processorSelection = new ProcessKeyedTrafficStreamByVehicleTypeWithState();
//
//  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//  public void runFlow() throws Exception {
//    // setup source
//    DataStream<VehicleEvent> sourceEvents = env.fromCollection(TrafficEventGenerator.generateTrafficEventList());
//    // Window?
//
//    // partition events by Key
//    KeyedStream<VehicleEvent, String> keyedEvents = sourceEvents.keyBy(new VehicleTypeKeySelector());
//
//
//    // process partitioned events
//    DataStream<LabeledVehicleEvent> processedEvents;
//    if (processorSelection instanceof ProcessFunction) {
//      // process partitioned events (w/ or w/o the Key context)
//      processedEvents = keyedEvents.process(
//          (ProcessFunction<VehicleEvent, LabeledVehicleEvent>) processorSelection);
//
//    } else if (processorSelection instanceof KeyedProcessFunction) {
//      // process partitioned events with the context of the Key
//      processedEvents = keyedEvents.process(
//          (KeyedProcessFunction<String, VehicleEvent, LabeledVehicleEvent>) processorSelection);
//
//    } else {
//      throw new RuntimeException("Unknown Processor type.");
//    }
//
//    // print and execute
////    processedEvents
////        .map(
////            new MapFunction<LabeledVehicleEvent, Tuple2<String, Integer>>() {
////              @Override
////              public Tuple2<String, Integer> map(LabeledVehicleEvent e) throws Exception {
////                return Tuple2.of(e.getVehicleType().name(), e.getSpeed());
////              }})
////        .keyBy(e -> e.f0)
////        .reduce(
////            new ReduceFunction<Tuple2<String, Integer>>() {
////              @Override
////              public Tuple2<String, Integer> reduce(Tuple2<String, Integer> e1,
////                                                    Tuple2<String, Integer> e2) throws Exception {
////                return Tuple2.of(e1.f0, e1.f1 + e2.f1);
////              }
////            })
//////        .map(e -> String.format("%s: %s: %s: %s: %s",
//////            e.getF1(),
//////            e.getEventUUID(),
//////            e.getVehicleType(),
//////            e.getLabel(),
//////            e.getEventTimestamp()))
////        .print();
////    env.execute();
//    processedEvents
//        .filter(e -> e.getEventTimestamp().isAfter(Instant.parse("2023-08-15T00:00:00")))
//        .keyBy(e -> e.getLabel().name())
//        .reduce(
//            new ReduceFunction<LabeledVehicleEvent>() {
//
//              @Override
//              public LabeledVehicleEvent reduce(LabeledVehicleEvent e0,
//                                                LabeledVehicleEvent e1) throws Exception {
//
//                return new LabeledVehicleEvent(
//                    e1.getVehicleType(),
//                    e1.getSpeed(),
//                    e1.getEventTimestamp(),
//                    e1.getLabel(),
//                    e1.getPassengers(),
//                    null,
//                    e0.getF2() == null ? 1 : e0.getF2() + 1,
//                    e0.getF3() == null
//                        ? (float) (e0.getSpeed() + e1.getSpeed()) / 2
//                        : (float) (e0.getF3() * e0.getF2() + e1.getSpeed()) / (e0.getF2() + 1)
//                );
//              }
//            })
//        .map(e -> String.format("%s: %s: %s :%s",
//            e.getLabel(),
//            e.getSpeed(),
//            e.getF2(),
//            e.getF3()))
//        .print();
//    env.execute();
//  }
//}
