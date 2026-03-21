package com.portal.gateway.model;

/**
 * ServiceInfo - Model class representing metadata about a registered microservice.
 * <p>
 * Contains details such as the service name, description, base URL, health endpoint URL,
 * current health status (UP/DOWN), display icon, and theme color.
 * Used by the Gateway dashboard to render service cards.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
public class ServiceInfo {

    /** Display name of the microservice (e.g., "User Service"). */
    private String name;

    /** Short description of the microservice's purpose. */
    private String description;

    /** Base URL where the microservice is running. */
    private String url;

    /** Full URL to the service's health actuator endpoint. */
    private String healthUrl;

    /** Current health status of the service: {@code "UP"} or {@code "DOWN"}. */
    private String status;

    /** Emoji icon used for dashboard display. */
    private String icon;

    /** Hex color code used for theming the service card on the dashboard. */
    private String color;

    /** Default no-arg constructor. */
    public ServiceInfo() {}

    /**
     * Constructs a fully initialized ServiceInfo instance.
     *
     * @param name        the display name of the service
     * @param description a short description of the service
     * @param url         the base URL of the service
     * @param healthUrl   the health check endpoint URL
     * @param status      the current health status ({@code "UP"} or {@code "DOWN"})
     * @param icon        the emoji icon for the service
     * @param color       the hex color code for dashboard theming
     */
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

