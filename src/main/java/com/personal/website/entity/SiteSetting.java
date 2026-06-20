package com.personal.website.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "site_settings")
public class SiteSetting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String settingKey;
    @Column(columnDefinition = "TEXT")
    private String settingValue;

    public SiteSetting() {}
    public SiteSetting(String key, String value) { this.settingKey = key; this.settingValue = value; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String k) { this.settingKey = k; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String v) { this.settingValue = v; }
}