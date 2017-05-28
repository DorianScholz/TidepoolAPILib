package io.tidepool.api.data;

public class DeviceDataCBG extends DeviceDataCommon {
    // CBG specific data
    @SuppressWarnings("FieldCanBeLocal")
    private String type = "cbg";
    private String units;
    private double value;

    public DeviceDataCBG(DeviceDataCommon other) {
        super(other);
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}
