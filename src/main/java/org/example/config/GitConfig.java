package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "git")
public class GitConfig {

  private String remoteUrl;
  private String remoteName;
  private String user;
  private String token;
  private static final String REPO_NAME_TEMPLATE = "%s/network-policy-%s.git";

  public String getRepoUrl(String systemCode) {
    return REPO_NAME_TEMPLATE.formatted(remoteUrl, systemCode);
  }
}
