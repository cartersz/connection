package com.orvibo.cloud.connection.common.cache;

/**
 * Created by sunlin on 2017/9/20.
 */
public class DeviceParameters {

    private String software;

    private String hardware;

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }


    @Override
    public String toString() {
        return "DeviceParameters{" +
                "software='" + software + '\'' +
                ", hardware='" + hardware + '\'' +
                '}';
    }
}
