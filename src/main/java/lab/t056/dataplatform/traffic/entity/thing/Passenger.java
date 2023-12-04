package lab.t056.dataplatform.traffic.entity.thing;

import lab.t056.dataplatform.traffic.entity.enums.SeatPosition;

import java.io.Serializable;

public class Passenger implements Serializable {
  private SeatPosition seatPosition;
  private boolean isAdult;

  public Passenger() {};
  public Passenger(SeatPosition seatPosition, boolean isAdult) {
    this.isAdult = isAdult;
    this.seatPosition = seatPosition;
  }

  public void setSeatPosition(SeatPosition seatPosition) {
    this.seatPosition = seatPosition;
  }

  public void setAdult(boolean adult) {
    isAdult = adult;
  }

  public SeatPosition getSeatPosition() {
    return seatPosition;
  }

  public boolean isAdult() {
    return isAdult;
  }

}
