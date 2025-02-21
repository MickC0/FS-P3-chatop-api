package com.mick.chatopapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chatop.openapi")
public class OpenAPIProperties {
    private String devUrl;
    private String prodUrl;

    public String getDevUrl() {
        return devUrl;
    }

    public void setDevUrl(String devUrl) {
        this.devUrl = devUrl;
    }

    public String getProdUrl() {
        return prodUrl;
    }

    public void setProdUrl(String prodUrl) {
        this.prodUrl = prodUrl;
    }
}
