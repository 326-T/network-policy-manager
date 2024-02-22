package org.example.util;

public class FileNameDefinition {

  public static final String README_LOCATION = "classpath:templates/README.md";
  public static final String NETWORK_POLICY_TEMPLATE_LOCATION = "classpath:templates/helm/network-policy/templates/*";
  private static final String INDIVIDUAL_README_LOCATION = "cache/%s/README.md";
  private static final String INDIVIDUAL_NETWORK_POLICY_TEMPLATE_LOCATION = "cache/%s/%s/templates";
  private static final String INDIVIDUAL_NETWORK_POLICY_VALUES_LOCATION = "cache/%s/%s/values.yaml";
  private FileNameDefinition() {
  }

  public static String getReadmeLocation(String systemCode) {
    return INDIVIDUAL_README_LOCATION.formatted(systemCode);
  }

  public static String getIndividualTemplateLocation(String systemCode, String namespace) {
    return INDIVIDUAL_NETWORK_POLICY_TEMPLATE_LOCATION.formatted(systemCode, namespace);
  }

  public static String getIndividualValuesLocation(String systemCode, String namespace) {
    return INDIVIDUAL_NETWORK_POLICY_VALUES_LOCATION.formatted(systemCode, namespace);
  }
}
