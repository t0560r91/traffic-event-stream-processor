package lab.t056.dataplatform.traffic.entity.thing;

import lab.t056.dataplatform.traffic.entity.enums.LicenseType;

import java.util.UUID;

public interface Vehicle {
  void setLicensePlateNumber(UUID licensePlateNumber);
  UUID getLicensePlateNumber();

  void setLicenseType(LicenseType licenseType);
  LicenseType getLicenseType();
}
