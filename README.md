# セットアップ(Windows端末用)
以下、管理者権限ユーザーで実施する。

## 1.開発環境構築

### Oracle JDK(Oracle Java Development Kit)
Javaの開発・動作に必要な開発キット
Javaには様々なディストリビューションがあり、Oracle JDKはいわばJavaの本家という位置付けである。

#### インストール手順
* [Oracle JDK公式サイト](https://www.oracle.com/jp/java/technologies/downloads/#jdk17-windows)から
`x64 installer` のリンクをクリックし、インストーラーをダウンロードする。
* ダウンロードしたファイル `jdk-17_windows-x64_bin.exe` をダブルクリックする。
* ユーザーアカウント制限のダイアログが表示される。 `はいボタン` をクリックする。
* インストール手順は基本的に `次へボタン` をクリックしていくのみだが、ボタンクリック2回目で表示されるインストール先フォルダを確認しておく。
* インストール完了のダイアログで `閉じるボタン` をクリックする。

#### インストールしたOracle JDKの設定
* Windowsのスタートボタンを右クリックして、 `システム` をクリックする。
* 設定の検索欄に `システム環境変数` を入力して、表示される候補をクリックする。
* システムのプロパティウィンドウで `環境変数ボタン` をクリックする。
* 環境変数ウィンドウで `システム環境変数` で `新規ボタン` をクリックする。
* 新しいシステム変数ウィンドウで変数名、変数値を設定して `OKボタン` をクリックする。
```
変数名 = JAVA_HOME
変数値 = (Oracle JDKをインストールしたディレクトリ、
        デフォルトでは C:\Program Files\Java\jdk-17 )
```
* 環境変数ウィンドウで `システム環境変数` 一覧から変数名 `Path` をダブルクリックする。
* 環境変数名の編集ウィンドウの `新規ボタン` をクリックし、 `$JAVA_HOME\bin` を入力する。
* 環境変数ウィンドウとシステムのプロパティウィンドウでそれぞれ `OKボタン` をクリックして閉じる。

#### Oracle JDKのセットアップ確認
* 新たにPowerShellを管理者権限で起動し、以下のコマンドを入力、Enterキーを押下する。
```ps
java -version
```

* 以下のように標準出力に表示される。(javaのバージョンは2023/11/22時点の最新版である。)
```ps
java version "17.0.9" 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 17.0.9+11-LTS-201)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.9+11-LTS-201, mixed mode, sharing)
```

### Git for Windows
[公式サイト](https://gitforwindows.org/)からインストーラーをダウンロード、実行する。
* 以下の設定は変更してから`Nextボタン` をクリックする。
  * Choosing the default editor used by git -> 研修者が利用するテキストエディタを選択(プルダウンに候補が無い場合は `Select other editor as Git's default editor` を選択して Location of editorを指定する。)
  * Adjusting the name of the initial branch in new repository -> `Override the default branch name for new repository` を選択する。
  * Checkout Windows-style, commit Unix-style line endings -> `Override the default branch name for new repository` を選択する。
* 他の設定はデフォルトから変えずに `Nextボタン` をクリックする。
dockerに関するコマンド実行後、同梱されているgit bashが起動する場合がある。

### Pleiades All in One Eclipse
Java等の統合開発環境
* [公式サイト](https://mergedoc.osdn.jp/)
`Windows x64`  >  `Full Edition`  >  `Java`のダウンロードボタンをクリックしてダウンロードする。
* ダウンロードしたファイルをダブルクリックする。
* Pleiades All in One (年-月)自己解凍書庫のウィンドウで `解凍ボタン` をクリックする。
  * 解凍先はデフォルトで `c:\pleiades\(年-月)` である。(年 - 月の例で2023年11月時点で2023-09が最新版である。)
  * 解凍完了まで数十秒程度を要する。

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
git clone https://github.com/hara-y-illmatics/shopping-template-java-windows shopping
以下、c:\gitディレクトリにshoppingをクローンしたものとして説明する。
# shoppingへ移動
cd shopping
# ローカルリポジトリを安全なディレクトリとして設定
git config --global --add safe.directory c:/git/shopping
# originの再設定
# <URL> には作成した自分のリポジトリの "HTTPS" を使用
git remote set-url origin <URL>
# Githubにローカルリポジトリをプッシュ
git push origin main
```

## 3. mainブランチのプロテクションルール設定
Githubでmainへのマージをレビュー必須とする[設定](https://drive.google.com/drive/folders/1jwtMsaLBwvPpkmjvfqIdrkwqHWQXjq7k?usp=sharing)を行う。
`.github/CODEOWNERS`に指定したGithubアカウントのレビュー承認を受けなければマージできなくなる。

## 4. ダウンロード済みのOracle XEの移動
Oracle XEのインストーラー(拡張子rpmのファイル)を `C:\Users\(ユーザー名)\Downloads` フォルダにダウンロードした場合
```
mv C:\Users\(ユーザー名)\Downloads\oracle-database-xe-21c-1.0-1.ol7.x86_64.rpm c:\git\shopping\docker\oracle\21.3.0
```

## 5. dockerイメージのビルド
```
cd c:\git\shopping\docker\oracle
.\buildContainerImage.sh -v 21.3.0 -x -i
(別のコマンドプロンプトが開きビルドを実行、所要時間はPCの性能に依るが数分程度後、ビルド完了後に別プロンプトが閉じる)
```

## 6. Dockerコンテナ(DB)の起動
```bash
cd c:\git\shopping\bin
./up-d.sh
```

## 7. Oracleのセットアップ
```bash
# DBサーバーに接続
docker exec -it docker-dbserver-1 bash
# .bashrcにNLS_LANGを設定(DBサーバー初回起動時のみ)
(echo 'NLS_LANG="Japanese_Japan.AL32UTF8"'; echo 'export NLS_LANG') > .bashrc
# DBサーバーでsqlplusをsysdbaとして起動
sqlplus / as sysdba
# pdb$seedをopenするため下記の設定が必要
alter session set "_oracle_script"=true;
# pdb$seedをopen(エラーが発生した場合、7. Oracleのセットアップを最初から実行、alter sessionをせずに手順を実行)
alter pluggable database pdb$seed open read write force;
# pdb$seedを基にデータベースtestを作成
create pluggable database TEST admin user tuser identified by tpassword file_name_convert = ('/opt/oracle/oradata/XE/pdbseed/', '/opt/oracle/oradata/XE/test/');
# 接続先DBをTESTに変更
alter session set container=TEST;
# データベースTESTの状態がMOUNTEDであることを確認
show pdbs
# データベースTESTをオープンして、その状態を維持
alter pluggable database TEST open;
alter pluggable database TEST save state;
# ユーザーtuserに権限付与
GRANT ALL PRIVILEGES to TUSER;
# 初期データの投入 -> テーブル作成SQLを全てコピー、sqlplusのコンソールにペースト(SQLの内容は割愛)
# テーブル作成SQLのパス -> c:\git\shopping\shopping-app\src\main\resources\sql\1-create-tables.sql

# 初期データの投入 -> データ追加SQLを全てコピー、sqlplusのコンソールにペースト(SQLの内容は割愛)
# データ追加SQLのパス -> c:\git\shopping\shopping-app\src\main\resources\sql\2-insert-user.sql

# sqlplusを終了
exit
# DBサーバーでの作業終了
exit
```
<!--
メンター用備忘録
create pluggable database TEST admin user tuser
で作成するTUSERには権限が付与されていないため、
grantコマンドで権限付与する。
-->

## 8. eclipseのセットアップ
* エクスプローラーから `c:\pleiades\(年-月)\eclipse` を開き、eclipse.exeを実行する。
* eclipseのパッケージエクスプローラーから `プロジェクトのインポート` をクリックする。
* インポートウィンドウで `Gradle` -> `既存のGradleプロジェクト` をクリックする。
* Gradleプロジェクトのインポートウィンドウで `プロジェクト・ルート・ディレクトリー` の `参照ボタン` をクリックする。
* プロジェクト・ルート・ディレクトリーウィンドウで `c:\git\shopping-template-java-windows\shopping-app` を選択する。
* Gradleプロジェクトのインポートウィンドウで `完了ボタン` をクリックする。
* コマンド `ipconfig` でローカルIPアドレスを確認する。
* shopping-app\src\main\resources/application.yml
の7行目 `your-local-ip-address` をipconfigで確認したIPアドレスに書き換える。

## 9. Dockerコンテナ(アプリ)の起動
### コンテナの起動
Docker Desktopを起動して、以下コマンドを実行する。
```bash
./up.sh
```

## 10. 動作確認
[http://localhost:8080/sample](http://localhost:8080/sample) にアクセスして画面が表示されれば動作確認は完了である。

表示するjsonデータは[http://localhost:8080/api/sample](http://localhost:8080/api/sample)で確認可能である。

## サービス起動/停止＋
```bash
# DB停止
Docker Desktopから停止する、あるいは
docker stop (docker psで確認したDBサーバーのCONTAINER ID)
# DBサーバーに接続
docker exec -it docker-dbserver-1 bash
# DBサーバーに接続
sqlplus tuser/tpassword@localhost:1521/test
# CRUDの例:usersテーブルの検索
SELECT id, name FROM users;
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
|フロントサイド|user1@a.com|pass|
|フロントサイド|user2@a.com|pass|
|フロントサイド|user3@a.com|pass|
|管理サイド|admin@a.com|pass|

上記の機能を含めて [設計書](https://drive.google.com/drive/folders/1VRGeN6YdkE5EmyPEBiIkk0y2TneF3RH2?usp=sharing) を参考に実装を行う。
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
