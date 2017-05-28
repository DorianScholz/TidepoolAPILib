package io.tidepool.api.data;

import java.util.Date;

public class DeviceDataCommon {
    public class DeviceTime extends Date {}

    // common device data fields for ingestion: http://developer.tidepool.io/data-model/device-data/common.html
    private long clockDriftOffset;
    private long conversionOffset;
    private String deviceId;
    private DeviceTime deviceTime; // can be empty, but cannot include a timezone offset
    private Date time;
    private int timezoneOffset; // in minutes
    private String uploadId; // same as in corresponding metadata upload

    // fields filled automatically on ingestion
    private String guid;
    private Date createdTime;
    private String id;

    DeviceDataCommon() {}

    DeviceDataCommon(DeviceDataCommon other) {
        clockDriftOffset = other.clockDriftOffset;
        conversionOffset = other.conversionOffset;
        deviceId = other.deviceId;
        deviceTime = other.deviceTime;
        time = other.time;
        timezoneOffset = other.timezoneOffset;
        uploadId = other.uploadId;
        // do not copy the fields filled on ingestion
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public DeviceTime getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(DeviceTime deviceTime) {
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
