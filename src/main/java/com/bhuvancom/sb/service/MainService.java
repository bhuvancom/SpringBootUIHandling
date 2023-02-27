package com.bhuvancom.sb.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
public class MainService {
    public String checkService() {
        return "Running";
    }

    public String restartService() {
        return "";
    }

    public String stopService() {
        return "";
    }

    public String reUploadZip() {
        return "";
    }

    public boolean verifyUser(String name, String pass) {
        return isEqual(name, "sb@admin") && isEqual(pass, "sb@admin");
    }

    private boolean isEqual(String given, String target) {
        return given != null && given.contentEquals(target);
    }
}
