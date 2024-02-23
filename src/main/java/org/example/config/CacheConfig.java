package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "cache")
public class CacheConfig {

  public static final String README_LOCATION = "classpath:templates/README.md";
  public static final String NETWORK_POLICY_TEMPLATE_LOCATION = "classpath:templates/helm/network-policy/templates/*";
  private static final String INDIVIDUAL_REPO_LOCATION = "%s/network-policy-%s";
  private static final String INDIVIDUAL_README_LOCATION = "%s/network-policy-%s/README.md";
  private static final String INDIVIDUAL_NETWORK_POLICY_TEMPLATE_LOCATION = "%s/network-policy-%s/%s/templates";
  private static final String INDIVIDUAL_NETWORK_POLICY_VALUES_LOCATION = "%s/network-policy-%s/%s/values.yaml";
  private String dir;

  public String getRepoLocation(String systemCode) {
    return INDIVIDUAL_REPO_LOCATION.formatted(dir, systemCode);
  }

  public String getReadmeLocation(String systemCode) {
    return INDIVIDUAL_README_LOCATION.formatted(dir, systemCode);
  }

  public String getIndividualTemplateLocation(String systemCode, String namespace) {
    return INDIVIDUAL_NETWORK_POLICY_TEMPLATE_LOCATION.formatted(dir, systemCode, namespace);
  }

  public String getIndividualValuesLocation(String systemCode, String namespace) {
    return INDIVIDUAL_NETWORK_POLICY_VALUES_LOCATION.formatted(dir, systemCode, namespace);
  }
}
