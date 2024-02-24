# Helm Chart For Network Policy

このHelm Chartは、ネットワークポリシーを管理しています。
<br><br>
values.ymlにネットワークポリシーを追加、削除することができます。

```yaml
---
name: "network-policy"
namespace: "default"
selector:
  app: "backend"
ingress:
  from:
  - ipBlock:
      cidr: "172.17.0.0/16"
      except:
      - "10.0.0.0/24"
    namespaceSelector:
      matchLabels:
        tier: "backend"
    podSelector:
      matchLabels:
        app: "backend"
  ports:
  - protocol: "TCP"
    port: 80
egress:
  to:
  - ipBlock:
      cidr: "172.17.0.0/16"
      except:
      - "10.0.0.0/24"
    namespaceSelector:
      matchLabels:
        tier: "backend"
    podSelector:
      matchLabels:
        app: "backend"
  ports:
  - protocol: "TCP"
    port: 80
```