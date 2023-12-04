package lab.t056.dataplatform.traffic.entity.thing;

import lab.t056.dataplatform.traffic.entity.enums.LicenseType;

import java.io.Serializable;
import java.util.UUID;

public class TractorTrailer implements Vehicle, Serializable {

  private UUID licensePlateNumber;
  private LicenseType licenseType;

  public TractorTrailer() {}

  public TractorTrailer(UUID licensePlateNumber, LicenseType licenseType) {
    this.licensePlateNumber = licensePlateNumber;
    this.licenseType = licenseType;
  }

  public UUID getLicensePlateNumber() {
    return licensePlateNumber;
  }

  public void setLicensePlateNumber(UUID licensePlateNumber) {
    this.licensePlateNumber = licensePlateNumber;
  }

  public LicenseType getLicenseType() {
    return licenseType;
  }

  public void setLicenseType(LicenseType licenseType) {
    this.licenseType = licenseType;
  }
}

