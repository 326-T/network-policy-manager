package org.example.persistence.entity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NetworkPolicyValue {

  private String name;
  private String namespace;
  private Map<String, String> selector;
  private List<Ingress> ingress;
  private List<Egress> egress;

  @Data
  @NoArgsConstructor
  public static class Ingress {

    private List<From> from;
    private List<PortType> ports;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Ingress ingress = (Ingress) o;
      return Objects.equals(from, ingress.from)
          && Objects.equals(ports, ingress.ports);
    }

    @Override
    public int hashCode() {
      return Objects.hash(from, ports);
    }
  }

  @Data
  @NoArgsConstructor
  public static class Egress {

    private List<To> to;
    private List<PortType> ports;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Egress egress = (Egress) o;
      return Objects.equals(to, egress.to)
          && Objects.equals(ports, egress.ports);
    }

    @Override
    public int hashCode() {
      return Objects.hash(to, ports);
    }
  }

  @Data
  @NoArgsConstructor
  public static class From {

    private IpBlock ipBlock;
    private NamespaceSelector namespaceSelector;
    private PodSelector podSelector;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      From from = (From) o;
      return Objects.equals(ipBlock, from.ipBlock)
          && Objects.equals(namespaceSelector, from.namespaceSelector)
          && Objects.equals(podSelector, from.podSelector);
    }

    @Override
    public int hashCode() {
      return Objects.hash(ipBlock, namespaceSelector, podSelector);
    }
  }

  @Data
  @NoArgsConstructor
  public static class To {

    private IpBlock ipBlock;
    private NamespaceSelector namespaceSelector;
    private PodSelector podSelector;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      To to = (To) o;
      return Objects.equals(ipBlock, to.ipBlock)
          && Objects.equals(namespaceSelector, to.namespaceSelector)
          && Objects.equals(podSelector, to.podSelector);
    }

    @Override
    public int hashCode() {
      return Objects.hash(ipBlock, namespaceSelector, podSelector);
    }
  }

  @Data
  @NoArgsConstructor
  public static class IpBlock {

    private String cidr;
    private List<String> except;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      IpBlock ipBlock = (IpBlock) o;
      return Objects.equals(cidr, ipBlock.cidr)
          && Objects.equals(except, ipBlock.except);
    }

    @Override
    public int hashCode() {
      return Objects.hash(cidr, except);
    }
  }

  @Data
  @NoArgsConstructor
  public static class NamespaceSelector {

    private Map<String, String> matchLabels;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      NamespaceSelector that = (NamespaceSelector) o;
      return Objects.equals(matchLabels, that.matchLabels);
    }

    @Override
    public int hashCode() {
      return Objects.hash(matchLabels);
    }
  }

  @Data
  @NoArgsConstructor
  public static class PodSelector {

    private Map<String, String> matchLabels;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      PodSelector that = (PodSelector) o;
      return Objects.equals(matchLabels, that.matchLabels);
    }

    @Override
    public int hashCode() {
      return Objects.hash(matchLabels);
    }
  }

  @Data
  @NoArgsConstructor
  public static class PortType {

    private String protocol;
    private Integer port;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      PortType portType = (PortType) o;
      return Objects.equals(protocol, portType.protocol)
          && Objects.equals(port, portType.port);
    }

    @Override
    public int hashCode() {
      return Objects.hash(protocol, port);
    }
  }
}
