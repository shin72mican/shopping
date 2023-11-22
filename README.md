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
変数値 = (Oracle JDKをインストールしたディレクトリ、デフォルトでは C:\Program Files\Java\jdk-17 )
```
* 環境変数ウィンドウで `システム環境変数` 一覧から変数名 `Path` をダブルクリックする。
* 環境変数名の編集ウィンドウの `新規ボタン` をクリックし、 `$JAVA_HOME\bin` を入力する。
* 環境変数ウィンドウとシステムのプロパティウィンドウでそれぞれ `OKボタン` をクリックして閉じる。

#### Oracle JDKの動作確認
* 新しくPowerShellを管理者権限で起動し、以下のコマンドを入力、Enterキーを押下する。
```ps
java -version
```

* 以下のように標準出力に表示される。
```ps
java version "17.0.9" 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 17.0.9+11-LTS-201)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.9+11-LTS-201, mixed mode, sharing)
```

### Pleiades All in One Eclipse
Java等の統合開発環境
* [公式サイト](https://mergedoc.osdn.jp/)
`Windows x64`  >  `Full Edition`  >  `Java`のダウンロードボタンをクリックしてダウンロードする。
* ダウンロードしたファイルをダブルクリックする。
* Pleiades All in One (年-月)自己解凍書庫のウィンドウで `解凍ボタン` をクリックする。
  * 解凍先はデフォルトで `c:\pleiades\(年-月)` である。(年 - 月の例で2023年11月時点で2023-09が最新版である。)
  * 解凍完了まで1分弱を要する(PCのスペックに依る)。
* エクスプローラーから `c:\pleiades\(年-月)\eclipse` を開き、eclipse.exeを実行する。
  * 研修での開発時にこの手順で開発、テストする。

#### Mybatis Generator pluginの設定
Mybatis GeneratorとはDBへのCRUDに必要な以下のソースを自動生成するツールである。
* entity(DBテーブル1行分のデータ)
* mapper(CRUDするクラス及び定義)

Mybatis Generator pluginとはeclipseからMybatis Generatorを実行するplugin(ツール)である。

* eclipse起動後、 `メニュー` -> `ヘルプ` -> `Eclipse マーケットプレイス` をクリックする。
* Eclipseマーケットプレイスウィンドウで検索欄に `MyBatis Generator` と入力しEnterキーを押下する。
* 検索結果 `Mybatis Generator i.j.k`(i.j.kはバージョン番号)の `インストールボタン`
をクリックする。
* ライセンスのレビューウィンドウで `使用条件の条項に同意します`
を選択して、 `完了ボタン` をクリックする。
* 信頼ウィンドウでUnsignedの✓を付け、 `選択項目を信頼ボタン` をクリックする。
* ソフトウェア更新のダイアログが表示され、 `いいえボタン` をクリックした後、eclipseを終了する。

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
# <URL> は作成した自分のリポジトリの "HTTPS" を使用
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
# DBサーバーに接続
docker exec -it docker-dbserver-1 bash
# DBサーバーでsqlplusをsysdbaとして起動
sqlplus / as sysdba
# PDB$SEEDを基にデータベースTESTを作成
alter session set container=PDB$SEED;
alter session set "_oracle_script"=true;
alter pluggable database pdb$seed open read write force;
create pluggable database test admin user tuser identified by tpassword file_name_convert = ('/opt/oracle/oradata/XE/pdbseed/', '/opt/oracle/oradata/XE/test/');
# データベースTESTの状態を表示(MOUNTED)
show pdbs
# データベースTESTをオープンして、その状態を維持
alter pluggable database TEST open;
alter pluggable database TEST save state;
# sqlplusを終了
exit
# DBサーバーでの作業終了
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
