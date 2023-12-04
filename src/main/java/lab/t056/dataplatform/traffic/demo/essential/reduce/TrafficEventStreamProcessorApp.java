package lab.t056.dataplatform.traffic.demo.essential.reduce;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TrafficEventStreamProcessorApp {
  public static void main(String[] args) throws Exception {
    // ingest config
    // integrate the actual app with config

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    TrafficEventStreamProcessor processor = new TrafficEventStreamProcessor();

    processor.runFlow(env);
  }
}
