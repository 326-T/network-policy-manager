package org.example.web.request;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.persistence.entity.NetworkPolicyValue;

@Data
@NoArgsConstructor
public class NetworkPolicyValueRequest {

  private Map<String, String> selector;
  private Ingress ingress;
  private Egress egress;

  public NetworkPolicyValue exportEntity() {
    NetworkPolicyValue networkPolicyValue = new NetworkPolicyValue();
    networkPolicyValue.setSelector(selector);
    networkPolicyValue.setIngress(ingress.exportEntity());
    networkPolicyValue.setEgress(egress.exportEntity());
    return networkPolicyValue;
  }

  @Data
  @NoArgsConstructor
  public static class Ingress {

    private List<From> from;
    private List<PortType> ports;

    public NetworkPolicyValue.Ingress exportEntity() {
      NetworkPolicyValue.Ingress ingress = new NetworkPolicyValue.Ingress();
      ingress.setFrom(from.stream().map(From::exportEntity).toList());
      ingress.setPorts(ports.stream().map(PortType::exportEntity).toList());
      return ingress;
    }
  }

  @Data
  @NoArgsConstructor
  public static class Egress {

    private List<To> to;
    private List<PortType> ports;

    public NetworkPolicyValue.Egress exportEntity() {
      NetworkPolicyValue.Egress egress = new NetworkPolicyValue.Egress();
      egress.setTo(to.stream().map(To::exportEntity).toList());
      egress.setPorts(ports.stream().map(PortType::exportEntity).toList());
      return egress;
    }
  }

  @Data
  @NoArgsConstructor
  public static class From {

    private IpBlock ipBlock;
    private NamespaceSelector namespaceSelector;
    private PodSelector podSelector;

    public NetworkPolicyValue.From exportEntity() {
      NetworkPolicyValue.From from = new NetworkPolicyValue.From();
      from.setIpBlock(ipBlock.exportEntity());
      from.setNamespaceSelector(namespaceSelector.exportEntity());
      from.setPodSelector(podSelector.exportEntity());
      return from;
    }
  }

  @Data
  @NoArgsConstructor
  public static class To {

    private IpBlock ipBlock;
    private NamespaceSelector namespaceSelector;
    private PodSelector podSelector;

    public NetworkPolicyValue.To exportEntity() {
      NetworkPolicyValue.To to = new NetworkPolicyValue.To();
      to.setIpBlock(ipBlock.exportEntity());
      to.setNamespaceSelector(namespaceSelector.exportEntity());
      to.setPodSelector(podSelector.exportEntity());
      return to;
    }
  }

  @Data
  @NoArgsConstructor
  public static class IpBlock {

    private String cidr;
    private List<String> except;

    public NetworkPolicyValue.IpBlock exportEntity() {
      NetworkPolicyValue.IpBlock ipBlock = new NetworkPolicyValue.IpBlock();
      ipBlock.setCidr(cidr);
      ipBlock.setExcept(except);
      return ipBlock;
    }
  }

  @Data
  @NoArgsConstructor
  public static class NamespaceSelector {

    private Map<String, String> matchLabels;

    public NetworkPolicyValue.NamespaceSelector exportEntity() {
      NetworkPolicyValue.NamespaceSelector namespaceSelector = new NetworkPolicyValue.NamespaceSelector();
      namespaceSelector.setMatchLabels(matchLabels);
      return namespaceSelector;
    }
  }

  @Data
  @NoArgsConstructor
  public static class PodSelector {

    private Map<String, String> matchLabels;

    public NetworkPolicyValue.PodSelector exportEntity() {
      NetworkPolicyValue.PodSelector podSelector = new NetworkPolicyValue.PodSelector();
      podSelector.setMatchLabels(matchLabels);
      return podSelector;
    }
  }

  @Data
  @NoArgsConstructor
  public static class PortType {

    private String protocol;
    private Integer port;

    public NetworkPolicyValue.PortType exportEntity() {
      NetworkPolicyValue.PortType portType = new NetworkPolicyValue.PortType();
      portType.setProtocol(protocol);
      portType.setPort(port);
      return portType;
    }
  }
}
