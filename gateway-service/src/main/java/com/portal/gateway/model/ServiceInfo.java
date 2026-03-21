package com.portal.gateway.model;

public class ServiceInfo {

    private String name;
    private String description;
    private String url;
    private String healthUrl;
    private String status;
    private String icon;
    private String color;

    public ServiceInfo() {}

    public ServiceInfo(String name, String description, String url, String healthUrl, String status, String icon, String color) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.healthUrl = healthUrl;
        this.status = status;
        this.icon = icon;
        this.color = color;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getHealthUrl() { return healthUrl; }
    public void setHealthUrl(String healthUrl) { this.healthUrl = healthUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}

