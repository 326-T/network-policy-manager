# Network Policy Manager

## 概要

kubernetesクラスターにおいて、ネットワークポリシーを管理するためのツール。

## 機能

- Gitレポジトリの作成
- Helmチャートの作成
- values.ymlへの追加、削除

## インストール

### ArgoCDの構築

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
3. ArgoCDのパスワードを取得する。

    ```bash
    kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
    ```
4. ArgoCDへのログイン

    ```bash
    brew install argocd
    argocd login localhost:18080
      username: admin
      password: <password>
    ```
5. ArgoCDにユーザを追加する
   
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
6. 作成したユーザにロールを付与する。

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
   
7. 作成したユーザのアクセストークンを取得する。

    ```bash
    argocd account generate-token --account test-user
    ```
   
8. ArgoCD Projectを作成する。

    ```bash
    argocd proj create network-policy-manager --src '*' --dest '*','*' --namespace '*' --cluster-resource-whitelist '*/*'
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
```