package lab.t056.dataplatform.traffic.demo.window.tumbling_eventtime_keyed;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TrafficEventStreamProcessorApp {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    TrafficEventStreamProcessor processor = new TrafficEventStreamProcessor();

    processor.runFlow(env);
  }
}
