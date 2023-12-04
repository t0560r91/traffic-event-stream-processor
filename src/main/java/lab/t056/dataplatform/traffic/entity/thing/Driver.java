package lab.t056.dataplatform.traffic.entity.thing;

import lab.t056.dataplatform.traffic.entity.enums.SeatPosition;

public class Driver extends Passenger {

  public Driver(boolean isAdult) {
    super(SeatPosition.FD, isAdult);
  }
}
