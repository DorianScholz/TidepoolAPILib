package io.tidepool.api.data;

import java.util.Date;

public class DeviceDataCBG {
    // CBG specific data
    private String type = "cbg";
    private String units;
    private double value;

    // common device data fields for ingestion: http://developer.tidepool.io/data-model/device-data/common.html
    private long clockDriftOffset;
    private long conversionOffset;
    private String deviceId;
    private Date deviceTime; // can be empty, but cannot include a timezone offset
    private Date time;
    private int timezoneOffset; // in minutes
    private String uploadId = ""; // same as in corresponding metadata upload

    // fields filled after ingestion
    private String guid;
    private Date createdTime;
    private String id;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getClockDriftOffset() {
        return clockDriftOffset;
    }

    public void setClockDriftOffset(long clockDriftOffset) {
        this.clockDriftOffset = clockDriftOffset;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdtime) {
        this.createdTime = createdtime;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getConversionOffset() {
        return conversionOffset;
    }

    public void setConversionOffset(long conversionOffset) {
        this.conversionOffset = conversionOffset;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(Date deviceTime) {
        this.deviceTime = deviceTime;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

}
