# Network Policy Manager

## 概要

kubernetesクラスターにおいて、ネットワークポリシーを管理するためのツール。

## 機能

- Gitレポジトリの作成
- Helmチャートの作成
- values.ymlへの追加、削除


---
## インストール
### 環境変数の設定

以下の環境変数を設定する。

|変数名|デフォルト|説明|
|---|---|---|
|GIT_REMOTE_URL| |GitのリモートURL|
|GIT_REMOTE_NAME| origin |Gitのリモート名|
|GIT_USER| |Gitのユーザ名|
|GIT_TOKEN| |Gitのアクセストークン|
|ARGOCD_ORIGIN| localhost:18080 |ArgoCDのURL|
|ARGOCD_TOKEN| |ArgoCDのアクセストークン|
|ARGOCD_PROJECT| network-policy-manager |ArgoCDのプロジェクト名|

### docker-composeを用いた起動

```bash
docker-compose up -d
```

### mvnwを用いた起動
1. ビルド

    ```bash
    ./mvnw clean install -DskipTests=true
    ```
2. 起動

    ```bash
    java -jar target/network-policy-manager-0.0.1-SNAPSHOT.jar
    ```

3. テスト

   ```bash
   ./mvnw test
   ```


---
## 動作確認
### レポジトリの作成
事前にレポジトリをGUIで作成しておく。<br>
レポジトリ名は`network-policy-${systemCode}`とする。

```bash
curl -X POST http://localhost:8080/api/v1/repos \
 -H "Content-Type: application/json" \
 -d '{"systemCode": "system1"}'
```

### チャートの作成
```bash
curl -X POST http://localhost:8080/api/v1/charts \
 -H "Content-Type: application/json" \
 -d '{"systemCode": "system1", "namespace": "namespace1"}'
```

### values.ymlへの追加

追加はIngressとEgressの設定値に含まれていない要素を追加する。<br>
含まれているかの判定は完全一致。
```bash
curl -X POST http://localhost:8080/api/v1/values \
 -H "Content-Type: application/json" \
 -d '{
    "systemCode": "system1",
    "namespace": "namespace1",
    "body": {
        "selector": {
            "app": "backend"
        },
        "ingress": [
            {
                "from": [
                    {
                        "namespaceSelector": {
                            "matchLabels": {
                                "tier": "backend"
                            }
                        },
                        "podSelector": {
                            "matchLabels": {
                                "app": "backend"
                            }
                        }
                    }
                ],
                "ports": [
                    {
                        "protocol": "TCP",
                        "port": 80
                    }
                ]
            }
        ],
        "egress": [
            {
                "to": [
                    {
                        "namespaceSelector": {
                            "matchLabels": {
                                "tier": "backend"
                            }
                        },
                        "podSelector": {
                            "matchLabels": {
                                "app": "backend"
                            }
                        }
                    }
                ],
                "ports": [
                    {
                        "protocol": "TCP",
                        "port": 80
                    }
                ]
            }
        ]
    }
}'
```

### values.ymlからの削除

削除はIngressとEgressの設定値に含まれている要素を削除する。<br>
含まれているかの判定は完全一致。
```bash
curl -X DELETE http://localhost:8080/api/v1/values \
 -H "Content-Type: application/json" \
 -d '{
    "systemCode": "system1",
    "namespace": "namespace1",
    "body": {
        "selector": {
            "app": "backend"
        },
        "ingress": [
            {
                "from": [
                    {
                        "namespaceSelector": {
                            "matchLabels": {
                                "tier": "backend"
                            }
                        },
                        "podSelector": {
                            "matchLabels": {
                                "app": "backend"
                            }
                        }
                    }
                ],
                "ports": [
                    {
                        "protocol": "TCP",
                        "port": 80
                    }
                ]
            }
        ],
        "egress": [
            {
                "to": [
                    {
                        "namespaceSelector": {
                            "matchLabels": {
                                "tier": "backend"
                            }
                        },
                        "podSelector": {
                            "matchLabels": {
                                "app": "backend"
                            }
                        }
                    }
                ],
                "ports": [
                    {
                        "protocol": "TCP",
                        "port": 80
                    }
                ]
            }
        ]
    }
}'
```

---
## 補足: ArgoCDの構築
本アプリケーションの動作にはArgoCDが必要なため、以下にArgoCDの構築手順を記載します。

1. インストール
   1. ArgoCD CLIをインストールする。

       ```bash
       brew install argocd
       ```
   2. ArgoCDをインストールする。

       ```bash
       kubectl create namespace argocd
       kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
       kubectl port-forward svc/argocd-server -n argocd 18080:443
       ```
2. Adminログイン
   1. ArgoCDのパスワードを取得する。

       ```bash
       kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
       ```
   2. ArgoCDへのログイン

       ```bash
       brew install argocd
       argocd login localhost:18080
         username: admin
         password: <password>
       ```
3. ArgoCD Projectを作成する。

   以下のyamlを適用する
   ```bash
   kubectl apply -f argocd-project.yml
   ```
   ```yaml
   apiVersion: argoproj.io/v1alpha1
   kind: AppProject
   metadata:
      name: network-policy-manager
      namespace: argocd
   spec:
      clusterResourceWhitelist:
         - group: '*'
           kind: '*'
      destinations:
         - namespace: '*'
           server: '*'
      sourceRepos:
         - '*'
      roles:
         - name: admin
           policies:
              - p, proj:network-policy-manager:admin, applications, create, */*, allow
              - p, proj:network-policy-manager:admin, applications, get, */*, allow
              - p, proj:network-policy-manager:admin, applications, update, */*, allow
              - p, proj:network-policy-manager:admin, applications, delete, */*, allow
              - p, proj:network-policy-manager:admin, applications, sync, */*, allow
   ```
4. ArgoCDにユーザを追加する
   
   adminユーザだと、ユーザの作成ができないため、ユーザを追加する。

   1. ユーザを管理しているConfigMapを取得する。
   
      ```bash
      kubectl get configmap argocd-cm -n argocd -o yaml > argocd-cm.yml
      ```
   2. argocd-cm.ymlを編集し、以下を追記する。
   
      ```yaml
      data:
        accounts.test-user: apikey, login
      ```
   3. argocd-cm.ymlを適用する。
   
      ```bash
      kubectl apply -f argocd-cm.yml -n argocd
      ```
   4. ユーザを確認する。
   
      ```bash
      argocd account list
      ```
   5. ユーザのパスワードを設定する。
   
      ```bash
      argocd account update-password --account test-user
      ```
5. 作成したユーザにロールを付与する。

   1. RBACを取得する
   
      ```bash
      kubectl get configmap argocd-rbac-cm -n argocd -o yaml > argocd-rbac-cm.yml
      ```
   2. argocd-rbac-cm.ymlを編集し、以下を追記する。
   
      ```yaml
      data:
        policy.csv: |
          g, test-user, role:admin
          g, test-user, proj:network-policy-manager:admin
      ```
   3. argocd-rbac-cm.ymlを適用する。
    
      ```bash
      kubectl apply -f argocd-rbac-cm.yml -n argocd
      ```
   
6. 作成したユーザのアクセストークンを取得する。

    ```bash
    argocd account generate-token --account test-user
    ```



