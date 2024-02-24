package org.example.web.request;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NetworkPolicyValueRequest {

  private Map<String, String> selector;
  private List<Ingress> ingress;
  private List<Egress> egress;

  @Data
  @NoArgsConstructor
  public static class Ingress {

    private List<From> from;
    private List<PortType> ports;
  }

  @Data
  @NoArgsConstructor
  public static class Egress {

    private List<To> to;
    private List<PortType> ports;
  }

  @Data
  @NoArgsConstructor
  public static class From {

    private IpBlock ipBlock;
    private NamespaceSelector namespaceSelector;
    private PodSelector podSelector;
  }

  @Data
  @NoArgsConstructor
  public static class To {

    private IpBlock ipBlock;
    private NamespaceSelector namespaceSelector;
    private PodSelector podSelector;
  }

  @Data
  @NoArgsConstructor
  public static class IpBlock {

    private String cidr;
    private List<String> except;
  }

  @Data
  @NoArgsConstructor
  public static class NamespaceSelector {

    private Map<String, String> matchLabels;
  }

  @Data
  @NoArgsConstructor
  public static class PodSelector {

    private Map<String, String> matchLabels;
  }

  @Data
  @NoArgsConstructor
  public static class PortType {

    private String protocol;
    private Integer port;
  }
}
