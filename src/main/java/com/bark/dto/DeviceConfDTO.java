package com.bark.dto;

import java.io.Serializable;

public class DeviceConfDTO implements Serializable {
    private String deviceToken;
    private String name;
    private String deviceKey;
    private String algorithm;
    private String model;
    private String padding;
    private String encodeKey;
    private String iv;
    private String status;

    private static final long serialVersionUID = 1L;

    // Getters and Setters
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getEncodeKey() {
        return encodeKey;
    }

    public void setEncodeKey(String encodeKey) {
        this.encodeKey = encodeKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", deviceToken=").append(deviceToken);
        sb.append(", name=").append(name);
        sb.append(", deviceKey=").append(deviceKey);
        sb.append(", algorithm=").append(algorithm);
        sb.append(", model=").append(model);
        sb.append(", padding=").append(padding);
        sb.append(", encodeKey=").append(encodeKey);
        sb.append(", iv=").append(iv);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}