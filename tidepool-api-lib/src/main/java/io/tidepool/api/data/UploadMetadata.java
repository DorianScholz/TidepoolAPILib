package io.tidepool.api.data;

import java.util.List;
import java.util.Date;
import java.util.TimeZone;

public class UploadMetadata extends DeviceDataCommon {

    // Upload specific data
    @SuppressWarnings("FieldCanBeLocal")
    private String type = "upload";
    private String byUser;
    private DeviceTime computerTime; // cannot include a timezone offset
    private List<String> deviceManufacturers;
    private String deviceModel;
    private String deviceSerialNumber;
    private List<String> deviceTags;
    private String timeProcessing;
    private String timezone;
    private String version;

    public UploadMetadata(DeviceDataCommon other) {
        super(other);
    }

    public UploadMetadata() {
        super();
        setTime(new Date());
        setComputerTime(new DeviceTime());
        setTimezone(TimeZone.getDefault().getID());
        setTimezoneOffset(TimeZone.getDefault().getOffset(getTime().getTime()) / 1000 / 60);
    }

    public String getType() {
        return type;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public DeviceTime getComputerTime() {
        return computerTime;
    }

    public void setComputerTime(DeviceTime computerTime) {
        this.computerTime = computerTime;
    }

    public List<String> getDeviceManufacturers() {
        return deviceManufacturers;
    }

    public void setDeviceManufacturers(List<String> deviceManufacturers) {
        this.deviceManufacturers = deviceManufacturers;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public List<String> getDeviceTags() {
        return deviceTags;
    }

    public void setDeviceTags(List<String> deviceTags) {
        this.deviceTags = deviceTags;
    }

    public String getTimeProcessing() {
        return timeProcessing;
    }

    public void setTimeProcessing(String timeProcessing) {
        this.timeProcessing = timeProcessing;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
