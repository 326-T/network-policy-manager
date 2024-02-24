package org.example.client.request;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArgoCdAppRequest {
  private Metadata metadata;
  private Spec spec;

  @Data
  @NoArgsConstructor
  public static class Metadata {
    private String name;
    private Map<String, String> labels;
  }

  @Data
  @NoArgsConstructor
  public static class Spec {
    private String project;
    private Source source;
    private Destination destination;
    private SyncPolicy syncPolicy;
  }

  @Data
  @NoArgsConstructor
  public static class Source {
    private String repoURL;
    private String path;
    private String targetRevision;
    private Helm helm;
  }

  @Data
  @NoArgsConstructor
  public static class Helm {
    private List<String> valueFiles;
  }

  @Data
  @NoArgsConstructor
  public static class Destination {
    private String server;
    private String namespace;
  }

  @Data
  @NoArgsConstructor
  public static class SyncPolicy {
    private Automated automated;
  }

  @Data
  @NoArgsConstructor
  public static class Automated {
    private Boolean prune;
    private Boolean selfHeal;
  }
}
