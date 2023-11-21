# セットアップ(Windows端末用)
以下、管理者権限ユーザーで実施する。

## 1.開発環境構築
### Chocolatey
パッケージ管理アプリ

#### インストール手順
デスクトップのスタートボタンをクリックし、
``` powershell ```
と入力する。

最も一致する検索結果にWindows PowerShellが表示される。
「管理者として実行する」をクリックする。

以下のコマンドを実行して、Chocolateyをインストールする。
```ps
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

cf. 上記コマンドは下記のURLに記載されている。
> https://chocolatey.org/install#installing-chocolatey
> **run the following command**と記載された直下にあるコマンドをPowerShellで実行する。

Chocolateyのインストール確認のため、
以下のコマンドを実行する。
```ps
choco list -localonly
```

実行結果でChocolateyが表示されていることを確認する。
```ps
Chocolatey v1.1.0
chocolatey 1.1.0
1 packages installed.
```

引き続き、Chocolatey GUIをインストールする。
```choco install ChocolateyGUI -y```

インストールが正常終了していれば、
処理結果が標準出力(PowerShellの画面)に以下のように表示される。
```
The install of chocolateygui was successful.
```

### JDK(Java Development Kit)
Javaの開発・動作に必要な開発キット
#### インストール手順
デスクトップからスタートボタンをクリックし、Chocolatey GUIを起動する。
左のメニューからchocolateyをクリックして画面を切り替える。
次のとおりに操作して検索する。

* SearchにJava SEと入力し、Enterキーを押下する。
* 検索結果にjavaの各バージョンが表示される。その中からJava SE 8.0.211ををダブルクリックして、画面右下のinstallボタンをクリックする。
インストールが完了すると、
```C:\Program Files```
フォルダ配下にJavaフォルダが作成されていて、
Chocolatey GUIからjdk 1.8.0_211がインストールされている。

インストール確認のため、新しくPowerShellを管理者権限で起動し、
```java -version```
と入力、Enterキー押下する。jdk 1.8.0_211をインストールした場合、
以下のように標準出力に表示される。
```ps
java version "1.8.0_211"
Java(TM) SE Runtime Environment (build 1.8.0_211-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.211-b12, mixed mode)
```

<!--
メンター用備忘録
Chocolatey GUI経由でJDK各種をインストールした場合、
環境変数JAVA_HOMEの追加及びpathへの追記も併せて実施される。
-->

### Pleiades All in One Eclipse
Java等の統合開発環境
* [公式サイト](https://mergedoc.osdn.jp/)
`Windows x64`  >  `Full Edition`  >  `Java`のダウンロードボタンをクリックしてダウンロードしてください。
<!--
メンター用備忘録
Windows x64 Full Edition Javaの場合、LombokやSTSプラグインも導入されているため、
上記EclipseのセットアップだけでSpring Bootアプリケーションを開発開始可能です。
-->

### Docker
非常に軽量なコンテナ型のアプリケーション実行環境  
* [Docker Desktop ダウンロードサイト](https://www.docker.com/products/docker-desktop/)
* Download Docker Desktop Windowsのボタンをクリックしてダウンロードする。

* 起動時にエラーが発生する場合、下記のサイトの通りにコマンド実行など実施する。
[セットアップ手順記載先](https://learn.microsoft.com/ja-jp/windows/wsl/install-manual#step-4---download-the-linux-kernel-update-package)

### Github
ソースコード管理等のWebサービス
[github.com](https://github.com/)
* アカウントを持っていなければ場合は登録する。(以下、例としてアカウントをyamada-t、メールアドレスをyamada-t@company.co.jpとする。)
* 新規リポジトリを作成する。
* 作成したリポジトリの `Settings` > `Manage access` `Invite a collaborator` から研修担当者のGithubアカウントを追加する。
* Slack研修用チャンネルに作成したリポジトリの通知がくるように連携を行う。
    `/github subscribe yamada-t/shopping reviews`
    認証を要求してくるので手順に従い設定する。
* Gitのユーザー設定
```base
git config --global user.name "yamada-t"
git config --global user.email "yamada-t@company.co.jp"
```

### Oracle 
データベース管理システム
 * [Oracle Database 21cダウンロードサイト](https://www.oracle.com/jp/database/technologies/xe-downloads.html)
 * Oracle Database 21c Express Edition for Linux x64 ( OL7 )をダウンロードする。


## 2. 研修アプリの取得と作業用リポジトリへプッシュ
研修アプリを取得した後、自分の作業用リポジトリへプッシュを行う。
```bash
# 研修用リポジトリをローカルにshoppingというディレクトリ名でクローン
git clone https://github.com/hara-y-illmatics/java-shopping-template shopping
以下、c:\gitディレクトリにshoppingをクローンしたものとして説明する。
# shoppingへ移動
cd shopping
# originの再設定
# <URL> は作成した自分のリポジトリの "HTTPS" を使用する
git remote set-url origin <URL>
# Githubにローカルリポジトリをプッシュ
git push origin main
# ローカルリポジトリを安全なディレクトリとして設定
git config --global --add safe.directory c:/git/shopping
```


## 3. mainブランチのプロテクションルール設定
Githubでmainへのマージをレビュー必須とする[設定](https://drive.google.com/drive/folders/1jwtMsaLBwvPpkmjvfqIdrkwqHWQXjq7k?usp=sharing)を行う。
`.github/CODEOWNERS`に指定したGithubアカウントのレビュー承認を受けなければマージできなくなる。

## 4. ダウンロード済みのOracle XEの移動
Oracle XEのインストーラー(拡張子rpmのファイル)を `C:\Users\(ユーザー名)\Downloads` フォルダにダウンロードした場合
```
mv C:\Users\(ユーザー名)\Downloads\oracle-database-xe-21c-1.0-1.ol7.x86_64.rpm c:\git\shopping\docker\oracle\21.3.0\
```


## 5. dockerイメージのビルド
```
cd c:\git\shopping\docker\oracle
.\buildContainerImage.sh -v 21.3.0 -x -i
(別のコマンドプロンプトが開きビルドを実行、所要時間はPCの性能に依るが数分程度後、ビルド完了後に別プロンプトが閉じる)
```


## 6. Dockerコンテナ(DB)の起動
```bash
cd bin
./up-d.sh
```


## 7. Oracleのセットアップ
```bash
docker exec -it docker exec -it docker-dbserver-1 bash
sqlplus / as sysdba
alter session set container=PDB$SEED;
create pluggable database test admin user tuser identified by tpassword file_name_convert = ('/opt/oracle/oradata/XE/pdbseed/', '/opt/oracle/oradata/XE/test/');
show pdbs
alter pluggable database TEST open;
exit
```

## 8.DB初期データの投入
TBD

## 9. Dockerコンテナ(アプリ)の起動
### アプリのビルド
```bash
./clean-build.sh
(実行完了に数分ほどかかる場合がある。)
```

### コンテナの起動
```bash
./up.sh
```

## 10. 動作確認
[http://localhost:8080](http://localhost:8080) にアクセスして画面が表示されれば完了。

## サービス起動/停止＋
<!--
# ビルド
~/bin/clean-build.sh
# 起動
~/bin/up.sh
-->
```bash
# DB停止
Docker Desktopから停止する
# DBアクセス
~/bin/sqlplus.sh
```
# 課題
* フロントサイド
  * 認証
    * ログイン
    * ログアウト
    * パスワードリセット
    * 新規登録(イメージ以外)
* 管理サイド
  * ログイン
  * ログアウト

認証用テストユーザ
|用いる画面|ユーザ名|パスワード|
|---|---|---|
|フロントサイド|user@a.com|pass|
|管理サイド|admin@a.com|pass|

上記の実装済み機能以外について [設計書](https://drive.google.com/drive/folders/1VRGeN6YdkE5EmyPEBiIkk0y2TneF3RH2?usp=sharing) を参考に実装を行う。
管理サイドから実装するのがおすすめ。
ブランチモデルは [GitHub Flow](https://tracpath.com/bootcamp/learning_git_github_flow.html) を利用する。
ブランチ名は `topic/product_management_20200101` のようにする。
PullRequestは機能単位(商品管理、商品カテゴリ管理...)とする。
masterへのマージはGitHubでPullRequestを利用し、有識者のコードレビュー承認後にマージすること。
# リファレンスなど
* [Spring](https://spring.pleiades.io)
* [Java](https://kazurof.github.io/GoogleJavaStyle-ja/)
* [Bootstrap4.4](https://getbootstrap.com/docs/4.4/getting-started/introduction/)
* [FontAwesome](https://fontawesome.com/)
* [Git](https://git-scm.com/book/ja/v2)
* [設計書の例](https://pm-rasinban.com/bd-write)
* [テスト設計書](https://docs.google.com/spreadsheets/d/1eAcfaLHgvd0X8Bomp7Be1qAVlkXLonyHEdmTw5qP91s/edit?usp=sharing)
