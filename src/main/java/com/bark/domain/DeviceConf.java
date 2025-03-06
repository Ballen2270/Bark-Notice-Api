package com.bark.domain;

import com.bark.dto.DeviceConfDTO;

import java.io.Serializable;

public class DeviceConf implements Serializable {
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken == null ? null : deviceToken.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey == null ? null : deviceKey.trim();
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm == null ? null : algorithm.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding == null ? null : padding.trim();
    }

    public String getEncodeKey() {
        return encodeKey;
    }

    public void setEncodeKey(String encodeKey) {
        this.encodeKey = encodeKey == null ? null : encodeKey.trim();
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv == null ? null : iv.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public DeviceConfDTO toDTO() {
        DeviceConfDTO dto = new DeviceConfDTO();
        dto.setName(this.name);
        dto.setAlgorithm(this.algorithm);
        dto.setModel(this.model);
        dto.setPadding(this.padding);
        dto.setStatus(this.status);
        dto.setDeviceToken(mask(this.deviceToken));
        dto.setDeviceKey(mask(this.deviceKey));
        dto.setIv(mask(this.iv));
        dto.setEncodeKey(mask(this.encodeKey));
        return dto;
    }

private String mask(String value) {
    if (value == null || value.isEmpty()) {
        return value;
    }
    int length = value.length();
    int maskLength = Math.max(length - 4, 0);
    StringBuilder maskedValue = new StringBuilder(value.substring(0, 4)); // 保留前四位
    for (int i = 0; i < maskLength; i++) {
        maskedValue.append("*");
    }
    return maskedValue.toString();
}

}