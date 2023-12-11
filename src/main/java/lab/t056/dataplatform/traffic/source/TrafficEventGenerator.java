package lab.t056.dataplatform.traffic.source;

import lab.t056.dataplatform.traffic.entity.enums.LicenseType;
import lab.t056.dataplatform.traffic.entity.enums.SeatPosition;
import lab.t056.dataplatform.traffic.entity.event.VehicleEvent;
import lab.t056.dataplatform.traffic.entity.thing.*;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

// TODO: Create a proper Source Generator.
public class TrafficEventGenerator implements SourceFunction<VehicleEvent> {
  private static final Instant streamStartTimestamp = Instant.parse("2023-08-15T08:00:00.000Z");

  private int batchSize;
  private int averageEventIntervalSeconds;
  private int offsetSeconds = 0;
  private int offsetEndSeconds;
  private boolean running;

  public TrafficEventGenerator(int batchSize, int averageEventIntervalSeconds) {
    this.batchSize = batchSize;
    this.averageEventIntervalSeconds = averageEventIntervalSeconds;
    this.offsetEndSeconds = offsetSeconds + batchSize * averageEventIntervalSeconds;
  }

  @Override
  public void run(SourceContext<VehicleEvent> ctx) throws Exception {
    int elapse;
    running = true;
    while (running) {
      offsetEndSeconds = offsetSeconds + batchSize * averageEventIntervalSeconds;
      generateRandomTrafficEvents().forEach(ctx::collect);


      elapse = batchSize * averageEventIntervalSeconds;
      offsetSeconds += elapse;

      Thread.sleep(elapse * 1000L);
    }


  }

  @Override
  public void cancel() {
    running = false;
  }


  private ArrayList<VehicleEvent> generateRandomTrafficEvents() throws InterruptedException {

    int skippedEventIdx = ThreadLocalRandom.current().nextInt(0, batchSize + 1);
    ArrayList<VehicleEvent> events = new ArrayList<>();
    for (int i = 0; i < batchSize; i++) {
      if (i != skippedEventIdx) {
        events.add(
            new VehicleEvent(
                UUID.randomUUID(),
                streamStartTimestamp.plusSeconds(
                    ThreadLocalRandom.current().nextInt(offsetSeconds, offsetEndSeconds + 1)
                ),
                (int) ThreadLocalRandom.current().nextGaussian() * 10 + 65,
                getRandomVehicle(),
                getRandomPassengers()
            )
        );
      }
    }

    return events;
  }

  private Vehicle getRandomVehicle() {
    ArrayList<Vehicle> potentialVehicle = new ArrayList<>(
        List.of(
            new Sedan(UUID.randomUUID(), LicenseType.PERSONAL),
            new Sedan(UUID.randomUUID(), LicenseType.COMMERCIAL),
            new Motorcycle(UUID.randomUUID(), LicenseType.PERSONAL),
            new Motorcycle(UUID.randomUUID(), LicenseType.COMMERCIAL),
            new TractorTrailer(UUID.randomUUID(), LicenseType.PERSONAL),
            new TractorTrailer(UUID.randomUUID(), LicenseType.COMMERCIAL)
        )
    );

    return potentialVehicle.get(new Random().nextInt(potentialVehicle.size()));
  }

  private ArrayList<Passenger> getRandomPassengers() {
    ArrayList<ArrayList<Passenger>> potentialPassengers = new ArrayList<>(
        List.of(
            new ArrayList<>(List.of(new Driver(true))),
            new ArrayList<>(List.of(new Driver(false))),
            new ArrayList<>(List.of(new Driver(true), new Passenger(SeatPosition.FP,true))),
            new ArrayList<>(List.of(new Driver(true), new Passenger(SeatPosition.FP,false)))
        )
    );

    return potentialPassengers.get(new Random().nextInt(potentialPassengers.size()));
  }


  public static List<VehicleEvent> generateTrafficEventList() {
    // TODO: Figure out why List (non-Serializable) is acceptable here?
    return List.of(
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(10),
            41,
            new Motorcycle(UUID.randomUUID(), LicenseType.PERSONAL),
            // TODO: Figure out why ArrayList (Serializable) is required here?
            new ArrayList<>(List.of(
                new Driver(true)))),
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(36),
            55,
            new Sedan(UUID.randomUUID(), LicenseType.PERSONAL),
            new ArrayList<>(List.of(
                new Driver(true),
                new Passenger(SeatPosition.RP, true)
            ))),
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(62),
            23,
            new TractorTrailer(UUID.randomUUID(), LicenseType.COMMERCIAL),
            new ArrayList<>(List.of(
                new Driver(true)
            ))),
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(73),
            39,
            new Sedan(UUID.randomUUID(), LicenseType.PERSONAL),
            new ArrayList<>(List.of(
                new Driver(true),
                new Passenger(SeatPosition.FP, true),
                new Passenger(SeatPosition.RD, false),
                new Passenger(SeatPosition.RP, false)
            ))),
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(97),
            88,
            new Motorcycle(UUID.randomUUID(), LicenseType.PERSONAL),
            new ArrayList<>(List.of(
                new Driver(true)
            ))),
        // Duplicate Event
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(97),
            88,
            new Motorcycle(UUID.randomUUID(), LicenseType.PERSONAL),
            new ArrayList<>(List.of(
                new Driver(true)
            ))),
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(145),
            42,
            new Sedan(UUID.randomUUID(), LicenseType.COMMERCIAL),
            new ArrayList<>(List.of(
                new Driver(true),
                new Passenger(SeatPosition.FP, true)
            ))),
        new VehicleEvent(
            UUID.randomUUID(),
            streamStartTimestamp.plusSeconds(156),
            49,
            new Sedan(UUID.randomUUID(), LicenseType.PERSONAL),
            new ArrayList<>(List.of(
                new Driver(true),
                new Passenger(SeatPosition.FP, true)
            )))
    );

  }

}
