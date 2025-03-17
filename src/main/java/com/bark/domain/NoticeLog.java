package com.bark.domain;

import java.io.Serializable;
import java.util.Date;

public class NoticeLog implements Serializable {
    private Integer id;

    private String deviceKey;

    private String url;

    private String title;

    private String body;

    private String group;

    private String result;

    private String status;
    
    private Date recCreateTime;
    
    private Date recUpdateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey == null ? null : deviceKey.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group == null ? null : group.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getRecCreateTime() {
        return recCreateTime;
    }

    public void setRecCreateTime(Date recCreateTime) {
        this.recCreateTime = recCreateTime;
    }

    public Date getRecUpdateTime() {
        return recUpdateTime;
    }

    public void setRecUpdateTime(Date recUpdateTime) {
        this.recUpdateTime = recUpdateTime;
    }

    @Override
    public String toString() {
        return "NoticeLog{" +
                "id=" + id +
                ", deviceKey='" + deviceKey + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", group='" + group + '\'' +
                ", result='" + result + '\'' +
                ", status='" + status + '\'' +
                ", recCreateTime=" + recCreateTime +
                ", recUpdateTime=" + recUpdateTime +
                '}';
    }
}