

# 🎓 受講生管理システム (Student Management System)
## サービス概要
このプロジェクトは、IT技術を教えるスクールが受講生の情報を保持・分析するための管理システムです。
スクール運営者が使用することを想定しており、CRUD操作中心のシンプルで使いやすい設計を目指しています。
Spring Boot と AWS を組み合わせた、実務志向の受講生管理Webアプリケーションです。
<img width="1440" height="876" alt="image" src="https://github.com/user-attachments/assets/94b042d6-69d7-4f92-bcdc-bcf0618cb733" />


## 🔗 デモサイト
**[👉 システムにアクセスして動作を確認する](http://StudentManagementALB-1209990620.ap-northeast-1.elb.amazonaws.com/)**
http://studentmanagementalb-1209990620.ap-northeast-1.elb.amazonaws.com/

> **ログイン情報** > どなたでも自由にご覧いただけます。検索フォームより「Mark」や「Kim」などの名前で検索をお試しください。

---

## 🛠 使用技術・インフラ構成

### 1. バックエンド / フロントエンド
- **Java 21 / Spring Boot 3.x**
- **MyBatis**: 動的SQLを利用した柔軟な検索機能を実装
- **MySQL 8.0**: 受講生データおよびコース情報の管理
- **HTML5 / CSS3 / JavaScript**: Vanilla JSによるレスポンシブなUIデザイン

### 2. インフラ（AWS）
- **EC2 / Amazon Linux 2023**: アプリケーションサーバー
- **ALB (Application Load Balancer)**: 負荷分散および公開窓口の固定
- **RDS (MySQL)**: データの永続化
- **systemd**: OS再起動時もアプリが自動起動する「24時間稼働」の設定を適用

### 3. CI/CD (自動化)
- **GitHub Actions**: 
  `main` ブランチへの Push を検知し、ビルドから AWS EC2 へのデプロイまでを完全に自動化しています。

---

## 💡 本プロジェクトで注力した点

### ✅ 現場視点のデプロイ・運用設計
単に「作って終わり」ではなく、採用担当者がいつでも動作確認できるよう **AWS 上で 24時間稼働** させています。また、`systemctl` を利用した常駐化設定を行い、信頼性の高いシステム構成を目指しました。

### ✅ 実務的な検索ロジック
MyBatis の動的SQLを活用し、未入力項目があってもエラーにならず、入力された条件のみで最適にフィルタリングされる検索機能を実装しました。

### ✅ CI/CD パイプラインの構築
手動デプロイによるミスを排除するため、GitHub Actions を導入しました。これにより、コードの修正から本番環境への反映までをシームレスに行える環境を整えています。
