package org.example.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.example.persistence.entity.NetworkPolicyValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YamlRepositoryTest {

  @Autowired
  private YamlRepository yamlRepository;

  NetworkPolicyValue prepareData() {
    NetworkPolicyValue data = new NetworkPolicyValue();
    data.setName("network-policy");
    data.setNamespace("default");
    data.setSelector(Map.of("app", "backend"));
    // Ingress
    NetworkPolicyValue.Ingress ingress = new NetworkPolicyValue.Ingress();
    NetworkPolicyValue.PortType portType = new NetworkPolicyValue.PortType();
    portType.setProtocol("TCP");
    portType.setPort(80);
    NetworkPolicyValue.NamespaceSelector namespaceSelector = new NetworkPolicyValue.NamespaceSelector();
    namespaceSelector.setMatchLabels(Map.of("tier", "backend"));
    NetworkPolicyValue.PodSelector podSelector = new NetworkPolicyValue.PodSelector();
    podSelector.setMatchLabels(Map.of("app", "backend"));
    NetworkPolicyValue.IpBlock ipBlock = new NetworkPolicyValue.IpBlock();
    ipBlock.setCidr("172.17.0.0/16");
    ipBlock.setExcept(List.of("10.0.0.0/24"));
    NetworkPolicyValue.From from = new NetworkPolicyValue.From();
    from.setIpBlock(ipBlock);
    from.setNamespaceSelector(namespaceSelector);
    from.setPodSelector(podSelector);
    ingress.setFrom(List.of(from));
    ingress.setPorts(List.of(portType));
    data.setIngress(List.of(ingress));
    // Egress
    NetworkPolicyValue.Egress egress = new NetworkPolicyValue.Egress();
    NetworkPolicyValue.To to = new NetworkPolicyValue.To();
    to.setIpBlock(ipBlock);
    to.setNamespaceSelector(namespaceSelector);
    to.setPodSelector(podSelector);
    egress.setTo(List.of(to));
    egress.setPorts(List.of(portType));
    data.setEgress(List.of(egress));
    return data;
  }


  @AfterAll
  void tearDown() {
    FileSystemUtils.deleteRecursively(new File("cache/YamlRepositoryTest_regular_case1.yaml"));
  }

  @Order(1)
  @Nested
  class Save {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @Test
      @DisplayName("YAML ファイルを保存できること")
      void case1() throws IOException {
        // given
        String filename = "cache/YamlRepositoryTest_regular_case1.yaml";
        NetworkPolicyValue data = prepareData();
        // when
        yamlRepository.save(filename, data);
        // then
        File file = new File(filename);
        assertThat(file).exists();
      }
    }
  }

  @Order(2)
  @Nested
  class Load {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @Test
      @DisplayName("YAML ファイルを読み込めること")
      void case1() throws IOException {
        // given
        String filename = "cache/YamlRepositoryTest_regular_case1.yaml";
        // when
        NetworkPolicyValue actual = yamlRepository.load(filename, NetworkPolicyValue.class);
        NetworkPolicyValue expected = prepareData();
        // then
        assertThat(actual)
            .extracting(NetworkPolicyValue::getName,
                NetworkPolicyValue::getNamespace,
                NetworkPolicyValue::getSelector,
                NetworkPolicyValue::getIngress,
                NetworkPolicyValue::getEgress)
            .containsExactly("network-policy", "default",
                Map.of("app", "backend"),
                expected.getIngress(),
                expected.getEgress());
      }
    }
  }
}