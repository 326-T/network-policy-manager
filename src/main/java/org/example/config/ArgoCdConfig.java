package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "argocd")
public class ArgoCdConfig {

  private String origin;
  private String token;
  private String project;
}
