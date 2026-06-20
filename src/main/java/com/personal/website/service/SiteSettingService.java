package com.personal.website.service;

import com.personal.website.entity.SiteSetting;
import com.personal.website.repository.SiteSettingRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SiteSettingService {
    private final SiteSettingRepository repo;
    public SiteSettingService(SiteSettingRepository repo) { this.repo = repo; }

    public String get(String key, String defaultValue) {
        return repo.findBySettingKey(key).map(SiteSetting::getSettingValue).orElse(defaultValue);
    }

    public void set(String key, String value) {
        SiteSetting s = repo.findBySettingKey(key).orElse(new SiteSetting(key, value));
        s.setSettingValue(value);
        repo.save(s);
    }

    public Map<String, String> all() {
        Map<String, String> map = new HashMap<>();
        repo.findAll().forEach(s -> map.put(s.getSettingKey(), s.getSettingValue()));
        return map;
    }
}