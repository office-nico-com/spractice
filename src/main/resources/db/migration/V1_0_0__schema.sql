CREATE TABLE clients (
 id BIGSERIAL NOT NULL,
 client_keycode VARCHAR(256) NOT NULL,
 client_name_ja VARCHAR(256) NOT NULL,
 client_name_ja_kana VARCHAR(256),
 user_regist_type_code SMALLINT,
 description VARCHAR(2048),
 secret VARCHAR(32) NOT NULL,
 logout_url VARCHAR(1024),
 security_mangement_team VARCHAR(256),
 security_mangement_tel VARCHAR(256),
 security_mangement_email VARCHAR(256),
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE clients ADD CONSTRAINT PK_clients PRIMARY KEY (id);

CREATE INDEX IDX_CLIENTS_01 ON CLIENTS (client_keycode);
CREATE INDEX IDX_CLIENTS_02 ON CLIENTS (client_name_ja);
CREATE INDEX IDX_CLIENTS_03 ON CLIENTS (client_name_ja_kana);
CREATE INDEX IDX_CLIENTS_04 ON CLIENTS (user_regist_type_code);


CREATE TABLE scenarios (
 id BIGSERIAL NOT NULL,
 scenario_keycode VARCHAR(256) NOT NULL,
 scenario_name VARCHAR(256) NOT NULL,
 thumbnail_binary_file_id BIGINT,
 description TEXT,
 start_guidance_id BIGINT,
 note TEXT,
 content_body TEXT,
 content_script TEXT,
 content_css TEXT,
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE scenarios ADD CONSTRAINT PK_scenarios PRIMARY KEY (id);

CREATE INDEX IDX_SCENARIOS_01 ON SCENARIOS (scenario_keycode);
CREATE INDEX IDX_SCENARIOS_02 ON SCENARIOS (scenario_name);
CREATE INDEX IDX_SCENARIOS_03 ON SCENARIOS (description);


CREATE TABLE stocks (
 id BIGSERIAL NOT NULL,
 scenario_id BIGINT,
 binary_file_id BIGINT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE stocks ADD CONSTRAINT PK_stocks PRIMARY KEY (id);

CREATE INDEX IDX_STOCKS_01 ON STOCKS (scenario_id);


CREATE TABLE binary_file_categories (
 id BIGSERIAL NOT NULL,
 category_name VARCHAR(256) NOT NULL,
 can_delete BOOLEAN NOT NULL,
 sort_order INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE binary_file_categories ADD CONSTRAINT PK_binary_file_categories PRIMARY KEY (id);

CREATE INDEX IDX_BINARY_FILE_CATEGORIES_01 ON BINARY_FILE_CATEGORIES (sort_order);


CREATE TABLE users (
 id BIGSERIAL NOT NULL,
 account VARCHAR(256) NOT NULL,
 passwd VARCHAR(128) NOT NULL,
 email VARCHAR(256) NOT NULL,
 family_name VARCHAR(256) NOT NULL,
 given_name VARCHAR(256) NOT NULL,
 family_name_kana VARCHAR(256) NOT NULL,
 given_name_kana VARCHAR(256) NOT NULL,
 registered_from_code SMALLINT DEFAULT 1 NOT NULL,
 is_admin BOOLEAN NOT NULL,
 description TEXT,
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE users ADD CONSTRAINT PK_users PRIMARY KEY (id);

CREATE INDEX IDX_USERS_01 ON USERS (account);
CREATE INDEX IDX_USERS_02 ON USERS (email);
CREATE INDEX IDX_USERS_03 ON USERS (family_name);
CREATE INDEX IDX_USERS_04 ON USERS (given_name);
CREATE INDEX IDX_USERS_05 ON USERS (family_name_kana);
CREATE INDEX IDX_USERS_06 ON USERS (given_name_kana);
CREATE INDEX IDX_USERS_07 ON USERS (registered_from_code);


CREATE TABLE completion_points (
 id BIGSERIAL NOT NULL,
 completion_point_keycode VARCHAR(256) NOT NULL,
 description TEXT NOT NULL,
 description_en TEXT,
 is_invalided BOOLEAN NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE completion_points ADD CONSTRAINT PK_completion_points PRIMARY KEY (id);

CREATE INDEX IDX_COMPLETION_POINTS_01 ON COMPLETION_POINTS (completion_point_keycode);


CREATE TABLE completions (
 id BIGSERIAL NOT NULL,
 client_id BIGINT NOT NULL,
 user_id BIGINT NOT NULL,
 completion_point_id BIGINT NOT NULL,
 transaction VARCHAR(256) NOT NULL,
 started_at TIMESTAMP WITHOUT TIME ZONE,
 ended_at TIMESTAMP WITHOUT TIME ZONE,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE completions ADD CONSTRAINT PK_completions PRIMARY KEY (id);

CREATE INDEX IDX_COMPLETIONS_01 ON COMPLETIONS (client_id,user_id,completion_point_id);
CREATE INDEX IDX_COMPLETIONS_02 ON COMPLETIONS (started_at,ended_at);


CREATE TABLE informations (
 id BIGSERIAL NOT NULL,
 message TEXT NOT NULL,
 user_id BIGINT NOT NULL,
 posted_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 is_show_dashboard BOOLEAN NOT NULL,
 show_started_on DATE,
 show_ended_on DATE,
 is_deleted BOOLEAN NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE informations ADD CONSTRAINT PK_informations PRIMARY KEY (id);

CREATE INDEX IDX_INFORMATIONS_01 ON INFORMATIONS (show_started_on,show_ended_on);


CREATE TABLE admin_operations (
 id BIGSERIAL NOT NULL,
 user_id BIGINT NOT NULL,
 operated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 function_code SMALLINT NOT NULL,
 operation_code SMALLINT NOT NULL,
 target_id BIGINT,
 message TEXT,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE admin_operations ADD CONSTRAINT PK_admin_operations PRIMARY KEY (id);

CREATE INDEX IDX_ADMIN_OPERATIONS_01 ON ADMIN_OPERATIONS (operated_at);
CREATE INDEX IDX_ADMIN_OPERATIONS_02 ON ADMIN_OPERATIONS (user_id);


CREATE TABLE guidances (
 id BIGSERIAL NOT NULL,
 scenario_id BIGINT NOT NULL,
 guidance_keycode VARCHAR(256) NOT NULL,
 guidance_text TEXT NOT NULL,
 height INT,
 is_full_height BOOLEAN DEFAULT false NOT NULL,
 delay_ms INT,
 position_code SMALLINT,
 background_color VARCHAR(32),
 drawing_speed INT,
 pre_script_name VARCHAR(256),
 pre_script TEXT,
 pre_script_delay_ms INT,
 post_script_name VARCHAR(256),
 post_script TEXT,
 post_script_delay_ms INT,
 start_completion_point_id BIGINT,
 end_completion_point_id BIGINT,
 sort_order INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE guidances ADD CONSTRAINT PK_guidances PRIMARY KEY (id);

CREATE INDEX IDX_GUIDANCES_01 ON GUIDANCES (scenario_id,sort_order);


CREATE TABLE guidance_actions (
 id BIGSERIAL NOT NULL,
 guidance_id BIGSERIAL NOT NULL,
 label VARCHAR(256) NOT NULL,
 action_type_code SMALLINT NOT NULL,
 next_guidance_action_id BIGINT,
 title VARCHAR(256),
 body TEXT,
 open_window_flag BOOLEAN,
 sort_order INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE guidance_actions ADD CONSTRAINT PK_guidance_actions PRIMARY KEY (id);

CREATE INDEX IDX_GUIDANCE_ACTIONS_01 ON GUIDANCE_ACTIONS (guidance_id,sort_order);


CREATE TABLE clients_users (
 id BIGSERIAL NOT NULL,
 client_id BIGINT NOT NULL,
 user_id BIGSERIAL NOT NULL,
 role_code SMALLINT DEFAULT 3 NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE clients_users ADD CONSTRAINT PK_clients_users PRIMARY KEY (id);

CREATE UNIQUE INDEX IDX_CLIENTS_USERS_01 ON CLIENTS_USERS (client_id,user_id);


CREATE TABLE clients_scenarios (
 id BIGSERIAL NOT NULL,
 client_id BIGINT,
 scenario_id BIGINT,
 is_invalided BOOLEAN NOT NULL,
 sort_order INT,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE clients_scenarios ADD CONSTRAINT PK_clients_scenarios PRIMARY KEY (id);

CREATE UNIQUE INDEX IDX_CLIENTS_SCENARIOS_01 ON CLIENTS_SCENARIOS (client_id,scenario_id);

CREATE INDEX IDX_CLIENTS_SCENARIOS_02 ON CLIENTS_SCENARIOS (sort_order);


CREATE TABLE binary_files (
 id BIGSERIAL NOT NULL,
 binary_file_category_id BIGSERIAL NOT NULL,
 original_file_name VARCHAR(512) NOT NULL,
 save_file_name VARCHAR(512) NOT NULL,
 sub_path VARCHAR(8) NOT NULL,
 mime_type VARCHAR(512) NOT NULL,
 file_size BIGINT NOT NULL,
 last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE binary_files ADD CONSTRAINT PK_binary_files PRIMARY KEY (id);

CREATE INDEX IDX_BINARY_FILES_01 ON BINARY_FILES (binary_file_category_id);


COMMENT ON TABLE clients IS 'クライアント';
COMMENT ON COLUMN clients.id IS 'クライアントID';
COMMENT ON COLUMN clients.client_keycode IS 'クライアントキーコード';
COMMENT ON COLUMN clients.client_name_ja IS '日本語クライアント名';
COMMENT ON COLUMN clients.client_name_ja_kana IS '日本語クライアント名カナ';
COMMENT ON COLUMN clients.user_regist_type_code IS 'ユーザー登録方法
1：手動登録
2：自動登録';
COMMENT ON COLUMN clients.description IS '説明';
COMMENT ON COLUMN clients.secret IS 'シークレットキー
自動登録の際に一致するかチェックする。';
COMMENT ON COLUMN clients.logout_url IS '';
COMMENT ON COLUMN clients.security_mangement_team IS 'セキュリティ対策組織名';
COMMENT ON COLUMN clients.security_mangement_tel IS 'セキュリティ対策組織電話番号';
COMMENT ON COLUMN clients.security_mangement_email IS 'セキュリティ対策組織メールアドレス';
COMMENT ON COLUMN clients.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN clients.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN clients.created_at IS 'レコード作成日';
COMMENT ON COLUMN clients.updated_at IS 'レコード更新日';
COMMENT ON COLUMN clients.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN clients.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE scenarios IS 'シナリオ';
COMMENT ON COLUMN scenarios.id IS '仮想マシンID';
COMMENT ON COLUMN scenarios.scenario_keycode IS 'シナリオキーコード';
COMMENT ON COLUMN scenarios.scenario_name IS 'シナリオ名';
COMMENT ON COLUMN scenarios.thumbnail_binary_file_id IS 'サムネイル画像バイナリファイルID';
COMMENT ON COLUMN scenarios.description IS '説明';
COMMENT ON COLUMN scenarios.start_guidance_id IS '';
COMMENT ON COLUMN scenarios.note IS '備考';
COMMENT ON COLUMN scenarios.content_body IS 'コンテンツボディ';
COMMENT ON COLUMN scenarios.content_script IS 'コンテンツスクリプト';
COMMENT ON COLUMN scenarios.content_css IS 'コンテンツスタイル';
COMMENT ON COLUMN scenarios.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN scenarios.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN scenarios.created_at IS 'レコード作成日';
COMMENT ON COLUMN scenarios.updated_at IS 'レコード更新日';
COMMENT ON COLUMN scenarios.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN scenarios.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE stocks IS 'ストック素材';
COMMENT ON COLUMN stocks.id IS 'ストック素材ID';
COMMENT ON COLUMN stocks.scenario_id IS 'シナリオID';
COMMENT ON COLUMN stocks.binary_file_id IS 'バイナリファイルID';
COMMENT ON COLUMN stocks.created_at IS 'レコード作成日';
COMMENT ON COLUMN stocks.updated_at IS 'レコード更新日';
COMMENT ON COLUMN stocks.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN stocks.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE binary_file_categories IS 'バイナリファイルカテゴリ';
COMMENT ON COLUMN binary_file_categories.id IS '';
COMMENT ON COLUMN binary_file_categories.category_name IS 'カテゴリ名';
COMMENT ON COLUMN binary_file_categories.can_delete IS '削除可フラグ
true:削除可能
false:削除不可

初期の分類は削除できない
';
COMMENT ON COLUMN binary_file_categories.sort_order IS '表示順';
COMMENT ON COLUMN binary_file_categories.created_at IS 'レコード作成日';
COMMENT ON COLUMN binary_file_categories.updated_at IS 'レコード更新日';
COMMENT ON COLUMN binary_file_categories.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN binary_file_categories.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE users IS 'ユーザー';
COMMENT ON COLUMN users.id IS 'ユーザーID';
COMMENT ON COLUMN users.account IS 'アカウント';
COMMENT ON COLUMN users.passwd IS 'パスワード';
COMMENT ON COLUMN users.email IS 'メールアドレス（アカウント名）';
COMMENT ON COLUMN users.family_name IS '名前-性';
COMMENT ON COLUMN users.given_name IS '名前-名';
COMMENT ON COLUMN users.family_name_kana IS 'フリガナ-性';
COMMENT ON COLUMN users.given_name_kana IS 'フリガナ-名';
COMMENT ON COLUMN users.registered_from_code IS '登録方法
1：手動登録
2：自動登録';
COMMENT ON COLUMN users.is_admin IS 'レコード更新ユーザID
true:システム管理者
false:一般ユーザ';
COMMENT ON COLUMN users.description IS '説明';
COMMENT ON COLUMN users.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN users.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN users.created_at IS 'レコード作成日';
COMMENT ON COLUMN users.updated_at IS 'レコード更新日';
COMMENT ON COLUMN users.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN users.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE completion_points IS '履修ポイント';
COMMENT ON COLUMN completion_points.id IS '履修ポイントID';
COMMENT ON COLUMN completion_points.completion_point_keycode IS '履修コード';
COMMENT ON COLUMN completion_points.description IS '履修条件説明文';
COMMENT ON COLUMN completion_points.description_en IS '';
COMMENT ON COLUMN completion_points.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN completion_points.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN completion_points.created_at IS 'レコード作成日';
COMMENT ON COLUMN completion_points.updated_at IS 'レコード更新日';
COMMENT ON COLUMN completion_points.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN completion_points.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE completions IS '履修結果';
COMMENT ON COLUMN completions.id IS '受講結果ID';
COMMENT ON COLUMN completions.client_id IS '';
COMMENT ON COLUMN completions.user_id IS '';
COMMENT ON COLUMN completions.completion_point_id IS '完了条件ID';
COMMENT ON COLUMN completions.transaction IS 'トランザクション情報';
COMMENT ON COLUMN completions.started_at IS '開始日';
COMMENT ON COLUMN completions.ended_at IS '修了日時';
COMMENT ON COLUMN completions.created_at IS 'レコード作成日';
COMMENT ON COLUMN completions.updated_at IS 'レコード更新日';
COMMENT ON COLUMN completions.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN completions.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE informations IS '情報共有';
COMMENT ON COLUMN informations.id IS '情報共有ID';
COMMENT ON COLUMN informations.message IS 'メッセージ';
COMMENT ON COLUMN informations.user_id IS '投稿者ユーザーID';
COMMENT ON COLUMN informations.posted_at IS '投稿日時';
COMMENT ON COLUMN informations.is_show_dashboard IS 'ダッシュボードへの掲載フラフ
true:掲載する
false:掲載しない';
COMMENT ON COLUMN informations.show_started_on IS 'ダッシュボード掲載開始日';
COMMENT ON COLUMN informations.show_ended_on IS 'ダッシュボード掲載終了日';
COMMENT ON COLUMN informations.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN informations.created_at IS 'レコード作成日';
COMMENT ON COLUMN informations.updated_at IS 'レコード更新日';
COMMENT ON COLUMN informations.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN informations.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE admin_operations IS 'ADMIN操作履歴';
COMMENT ON COLUMN admin_operations.id IS '';
COMMENT ON COLUMN admin_operations.user_id IS 'ユーザーID';
COMMENT ON COLUMN admin_operations.operated_at IS '操作日時';
COMMENT ON COLUMN admin_operations.function_code IS '機能コード
1:ログイン
2:ユーザー管理
3:ユーザー一括登録
4:クライアント管理
5:シナリオ管理
6:コンテンツ
7:ガイダンス
8:ポータル管理
9:履修ポイント管理
10:受講履歴
11:パスワード変更
12:ユーザー情報変更
99:その他';
COMMENT ON COLUMN admin_operations.operation_code IS '操作コード
1:新規登録
2:編集
3:削除
4:複製
5:ダウンロード
99:その他
';
COMMENT ON COLUMN admin_operations.target_id IS '対象ID';
COMMENT ON COLUMN admin_operations.message IS 'メッセージ';
COMMENT ON COLUMN admin_operations.created_at IS 'レコード作成日';
COMMENT ON COLUMN admin_operations.updated_at IS 'レコード更新日';
COMMENT ON COLUMN admin_operations.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN admin_operations.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE guidances IS 'ガイダンス';
COMMENT ON COLUMN guidances.id IS 'ガイダンスID';
COMMENT ON COLUMN guidances.scenario_id IS ' シナリオID';
COMMENT ON COLUMN guidances.guidance_keycode IS 'ガイダンスキーコード';
COMMENT ON COLUMN guidances.guidance_text IS 'ガイダンステキスト';
COMMENT ON COLUMN guidances.height IS 'ガイダンスエリア高さ';
COMMENT ON COLUMN guidances.is_full_height IS '画面表示フラグ';
COMMENT ON COLUMN guidances.delay_ms IS '開始遅延時間（ms）';
COMMENT ON COLUMN guidances.position_code IS '位置コード
1:下部
2:上部';
COMMENT ON COLUMN guidances.background_color IS '背景色';
COMMENT ON COLUMN guidances.drawing_speed IS '描画速度';
COMMENT ON COLUMN guidances.pre_script_name IS '前処理名';
COMMENT ON COLUMN guidances.pre_script IS '前処理';
COMMENT ON COLUMN guidances.pre_script_delay_ms IS '前処理開始遅延時間（ms）';
COMMENT ON COLUMN guidances.post_script_name IS '後処理名';
COMMENT ON COLUMN guidances.post_script IS '後処理';
COMMENT ON COLUMN guidances.post_script_delay_ms IS '後処理開始遅延時間（ms）';
COMMENT ON COLUMN guidances.start_completion_point_id IS '履修開始ポイントID';
COMMENT ON COLUMN guidances.end_completion_point_id IS '履修終了ポイントID';
COMMENT ON COLUMN guidances.sort_order IS '表示順';
COMMENT ON COLUMN guidances.created_at IS 'レコード作成日';
COMMENT ON COLUMN guidances.updated_at IS 'レコード更新日';
COMMENT ON COLUMN guidances.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN guidances.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE guidance_actions IS 'ガイダンス行動';
COMMENT ON COLUMN guidance_actions.id IS 'ガイダンス遷移先ID';
COMMENT ON COLUMN guidance_actions.guidance_id IS 'ガイダンスID';
COMMENT ON COLUMN guidance_actions.label IS 'ラベル';
COMMENT ON COLUMN guidance_actions.action_type_code IS '遷移先種別コード
1:ガイダンス
2:スクリプト
3:リンク
4:最大化
5:受講終了';
COMMENT ON COLUMN guidance_actions.next_guidance_action_id IS '次ガイダンス遷移先ID';
COMMENT ON COLUMN guidance_actions.title IS 'スクリプト名・リンク名';
COMMENT ON COLUMN guidance_actions.body IS 'スクリプト・リンク先';
COMMENT ON COLUMN guidance_actions.open_window_flag IS '別画面表示フラグ
true:別画面で表示
false:同一画面で表示
';
COMMENT ON COLUMN guidance_actions.sort_order IS '表示順';
COMMENT ON COLUMN guidance_actions.created_at IS 'レコード作成日';
COMMENT ON COLUMN guidance_actions.updated_at IS 'レコード更新日';
COMMENT ON COLUMN guidance_actions.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN guidance_actions.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE clients_users IS 'クライアント_ ユーザー';
COMMENT ON COLUMN clients_users.id IS 'クライアント_ユーザーID';
COMMENT ON COLUMN clients_users.client_id IS 'クライアントID';
COMMENT ON COLUMN clients_users.user_id IS 'ユーザーID';
COMMENT ON COLUMN clients_users.role_code IS '権限コード
1:管理者
2:利用者';
COMMENT ON COLUMN clients_users.created_at IS 'レコード作成日';
COMMENT ON COLUMN clients_users.updated_at IS 'レコード更新日';
COMMENT ON COLUMN clients_users.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN clients_users.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE clients_scenarios IS 'クライアント_シナリオ';
COMMENT ON COLUMN clients_scenarios.id IS 'クライアント_シナリオID';
COMMENT ON COLUMN clients_scenarios.client_id IS 'クライアントID';
COMMENT ON COLUMN clients_scenarios.scenario_id IS 'シナリオID';
COMMENT ON COLUMN clients_scenarios.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN clients_scenarios.sort_order IS '表示順';
COMMENT ON COLUMN clients_scenarios.created_at IS 'レコード作成日';
COMMENT ON COLUMN clients_scenarios.updated_at IS 'レコード更新日';
COMMENT ON COLUMN clients_scenarios.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN clients_scenarios.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE binary_files IS 'バイナリファイル';
COMMENT ON COLUMN binary_files.id IS 'バイナリファイルID';
COMMENT ON COLUMN binary_files.binary_file_category_id IS 'バイナリファイルカテゴリID';
COMMENT ON COLUMN binary_files.original_file_name IS '元ファイル名';
COMMENT ON COLUMN binary_files.save_file_name IS '';
COMMENT ON COLUMN binary_files.sub_path IS '保存先サブパス名
"yyyyMM"形式';
COMMENT ON COLUMN binary_files.mime_type IS 'MIMEタイプ';
COMMENT ON COLUMN binary_files.file_size IS 'ファイルサイズ';
COMMENT ON COLUMN binary_files.last_modified_at IS 'ファイル最終更新日';
COMMENT ON COLUMN binary_files.created_at IS 'レコード作成日';
COMMENT ON COLUMN binary_files.updated_at IS 'レコード更新日';
COMMENT ON COLUMN binary_files.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN binary_files.updated_by IS 'レコード更新ユーザID';
