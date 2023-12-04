package lab.t056.dataplatform.traffic.entity.enums;

public enum DriverLabel {
  FASTER_DRIVER ("FASTER_DRIVER"),
  NORMAL_DRIVER ("NORMAL_DRIVER"),
  SLOWER_DRIVER ("SLOWER_DRIVER");

  private final String value;

  DriverLabel(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
