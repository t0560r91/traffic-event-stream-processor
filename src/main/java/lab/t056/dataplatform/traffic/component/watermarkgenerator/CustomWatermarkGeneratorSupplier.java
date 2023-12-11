package lab.t056.dataplatform.traffic.component.watermarkgenerator;

import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;

public class CustomWatermarkGeneratorSupplier implements WatermarkGeneratorSupplier<VehicleEvent> {
  @Override
  public WatermarkGenerator<VehicleEvent> createWatermarkGenerator(Context context) {
    return new CustomWatermarkGenerator(5000);
  }
}
