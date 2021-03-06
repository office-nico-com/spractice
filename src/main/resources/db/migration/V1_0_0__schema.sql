CREATE TABLE records (
 id BIGSERIAL NOT NULL
);

ALTER TABLE records ADD CONSTRAINT PK_records PRIMARY KEY (id);


CREATE TABLE users (
 id BIGSERIAL NOT NULL,
 account VARCHAR(128) NOT NULL,
 passwd VARCHAR(128) NOT NULL,
 family_name VARCHAR(256) NOT NULL,
 given_name VARCHAR(256) NOT NULL,
 family_name_kana VARCHAR(256) NOT NULL,
 given_name_kana VARCHAR(256) NOT NULL,
 role_code SMALLINT DEFAULT 3 NOT NULL,
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE users ADD CONSTRAINT PK_users PRIMARY KEY (id);


CREATE TABLE organizations (
 id BIGSERIAL NOT NULL,
 organization_name VARCHAR(256) NOT NULL,
 organization_name_ja VARCHAR(256) NOT NULL,
 description VARCHAR(2048),
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE organizations ADD CONSTRAINT PK_organizations PRIMARY KEY (id);


CREATE TABLE organizations_users (
 id VARCHAR(10) NOT NULL,
 organization_id BIGSERIAL NOT NULL,
 user_id BIGSERIAL NOT NULL
);

ALTER TABLE organizations_users ADD CONSTRAINT PK_organizations_users PRIMARY KEY (id);


CREATE TABLE binary_file_categories (
 id BIGSERIAL NOT NULL,
 organization_id BIGSERIAL NOT NULL,
 category_name VARCHAR(256) NOT NULL,
 can_delete BOOLEAN NOT NULL,
 order_number INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE binary_file_categories ADD CONSTRAINT PK_binary_file_categories PRIMARY KEY (id);


CREATE TABLE courses (
 id BIGSERIAL NOT NULL,
 organization_id BIGSERIAL NOT NULL,
 course_name VARCHAR(256) NOT NULL,
 course_name_ja VARCHAR(256) NOT NULL,
 thumbnail_binary_file_id BIGINT,
 description TEXT,
 start_date DATE NOT NULL,
 end_date DATE NOT NULL,
 note TEXT,
 order_number INT NOT NULL,
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE courses ADD CONSTRAINT PK_courses PRIMARY KEY (id);


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


CREATE TABLE virtual_machines (
 id BIGSERIAL NOT NULL,
 course_id BIGSERIAL NOT NULL,
 virtual_machine_name VARCHAR(256) NOT NULL,
 virtual_machine_name_ja VARCHAR(256) NOT NULL,
 thumbnail_binary_file_id BIGINT,
 description TEXT,
 startup_script TEXT,
 note TEXT,
 order_number INT NOT NULL,
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE virtual_machines ADD CONSTRAINT PK_virtual_machines PRIMARY KEY (id);


CREATE TABLE virtual_machine_properties (
 id BIGSERIAL NOT NULL,
 virtual_machine_id BIGSERIAL NOT NULL,
 property_name VARCHAR(512) NOT NULL,
 property_value VARCHAR(512) NOT NULL,
 order_number INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE virtual_machine_properties ADD CONSTRAINT PK_virtual_machine_properties PRIMARY KEY (id);


CREATE TABLE completion_conditions (
 id BIGSERIAL NOT NULL,
 virtual_machine_id BIGSERIAL,
 completion_key VARCHAR(16) NOT NULL,
 description TEXT NOT NULL,
 order_number INT NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE completion_conditions ADD CONSTRAINT PK_completion_conditions PRIMARY KEY (id);


CREATE TABLE completions (
 id BIGSERIAL NOT NULL,
 user_id BIGSERIAL NOT NULL,
 completion_condition_id BIGSERIAL NOT NULL
);

ALTER TABLE completions ADD CONSTRAINT PK_completions PRIMARY KEY (id);


CREATE TABLE extensions (
 id BIGSERIAL NOT NULL,
 virtual_machine_id BIGINT NOT NULL,
 extension_type VARCHAR(64) NOT NULL,
 extension_name VARCHAR(256) NOT NULL,
 note TEXT,
 order_number INT NOT NULL,
 is_invalided BOOLEAN DEFAULT false NOT NULL,
 is_deleted BOOLEAN DEFAULT false NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE extensions ADD CONSTRAINT PK_extensions PRIMARY KEY (id);


CREATE TABLE extension_explorer_properties (
 id BIGSERIAL NOT NULL,
 extension_id BIGINT NOT NULL,
 parent_id BIGINT,
 target_type_code SMALLINT NOT NULL,
 file_name VARCHAR(256) NOT NULL,
 icon_binary_file_id BIGINT,
 list_kind_name VARCHAR(256) NOT NULL,
 propery_kind_name VARCHAR(256) NOT NULL,
 description VARCHAR(256) NOT NULL,
 location VARCHAR(1024) NOT NULL,
 file_size VARCHAR(512),
 disk_size VARCHAR(512),
 list_size VARCHAR(512),
 file_created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 file_updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 file_accessed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 click_event TEXT,
 dblclick_event TEXT,
 order_number INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE extension_explorer_properties ADD CONSTRAINT PK_extension_explorer_properties PRIMARY KEY (id);


CREATE TABLE extension_properties (
 id BIGSERIAL NOT NULL,
 extension_id BIGSERIAL NOT NULL,
 property_name VARCHAR(512),
 property_value VARCHAR(512),
 is_text BOOLEAN NOT NULL,
 method TEXT,
 order_number INT NOT NULL,
 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 updated_at TIMESTAMP WITHOUT TIME ZONE,
 created_by BIGINT NOT NULL,
 updated_by BIGINT
);

ALTER TABLE extension_properties ADD CONSTRAINT PK_extension_properties PRIMARY KEY (id);


COMMENT ON TABLE records IS 'セッション受講履歴';
COMMENT ON COLUMN records.id IS '';
COMMENT ON TABLE users IS 'ユーザ';
COMMENT ON COLUMN users.id IS 'ユーザID';
COMMENT ON COLUMN users.account IS 'アカウント名';
COMMENT ON COLUMN users.passwd IS 'パスワード';
COMMENT ON COLUMN users.family_name IS '名前-性';
COMMENT ON COLUMN users.given_name IS '名前-名';
COMMENT ON COLUMN users.family_name_kana IS 'フリガナ-性';
COMMENT ON COLUMN users.given_name_kana IS 'フリガナ-名';
COMMENT ON COLUMN users.role_code IS '権限
1:システムユーザ
2:組織管理者
3:利用者';
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
COMMENT ON TABLE organizations IS '組織';
COMMENT ON COLUMN organizations.id IS '組織ID';
COMMENT ON COLUMN organizations.organization_name IS '組織名';
COMMENT ON COLUMN organizations.organization_name_ja IS '日本語組織名';
COMMENT ON COLUMN organizations.description IS '説明';
COMMENT ON COLUMN organizations.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN organizations.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN organizations.created_at IS 'レコード作成日';
COMMENT ON COLUMN organizations.updated_at IS 'レコード更新日';
COMMENT ON COLUMN organizations.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN organizations.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE organizations_users IS '組織_ ユーザ';
COMMENT ON COLUMN organizations_users.id IS '組織_ユーザID';
COMMENT ON COLUMN organizations_users.organization_id IS '組織ID';
COMMENT ON COLUMN organizations_users.user_id IS 'ユーザID';
COMMENT ON TABLE binary_file_categories IS 'バイナリファイルカテゴリ';
COMMENT ON COLUMN binary_file_categories.id IS '';
COMMENT ON COLUMN binary_file_categories.organization_id IS '';
COMMENT ON COLUMN binary_file_categories.category_name IS 'カテゴリ名';
COMMENT ON COLUMN binary_file_categories.can_delete IS '削除可フラグ
true:削除可能
false:削除不可

初期の分類は削除できない
';
COMMENT ON COLUMN binary_file_categories.order_number IS '表示順';
COMMENT ON COLUMN binary_file_categories.created_at IS 'レコード作成日';
COMMENT ON COLUMN binary_file_categories.updated_at IS 'レコード更新日';
COMMENT ON COLUMN binary_file_categories.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN binary_file_categories.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE courses IS '受講コース';
COMMENT ON COLUMN courses.id IS 'セッションID';
COMMENT ON COLUMN courses.organization_id IS '組織ID';
COMMENT ON COLUMN courses.course_name IS 'コース名
同一組織内ではユニークである
半角英数記号（_ . @-）';
COMMENT ON COLUMN courses.course_name_ja IS '日本語セッション名';
COMMENT ON COLUMN courses.thumbnail_binary_file_id IS 'サムネイル画像バイナリファイルID';
COMMENT ON COLUMN courses.description IS '説明';
COMMENT ON COLUMN courses.start_date IS '公開開始日';
COMMENT ON COLUMN courses.end_date IS '公開終了日';
COMMENT ON COLUMN courses.note IS '備考';
COMMENT ON COLUMN courses.order_number IS '表示順';
COMMENT ON COLUMN courses.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN courses.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN courses.created_at IS 'レコード作成日';
COMMENT ON COLUMN courses.updated_at IS 'レコード更新日';
COMMENT ON COLUMN courses.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN courses.updated_by IS 'レコード更新ユーザID';
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
COMMENT ON TABLE virtual_machines IS '仮想マシン';
COMMENT ON COLUMN virtual_machines.id IS '仮想マシンID';
COMMENT ON COLUMN virtual_machines.course_id IS '受講コースID';
COMMENT ON COLUMN virtual_machines.virtual_machine_name IS '仮想マシン名
同一組織内ではユニークである
半角英数記号（_ . @-）';
COMMENT ON COLUMN virtual_machines.virtual_machine_name_ja IS '日本語仮想マシン名';
COMMENT ON COLUMN virtual_machines.thumbnail_binary_file_id IS 'サムネイル画像バイナリファイルID';
COMMENT ON COLUMN virtual_machines.description IS '説明';
COMMENT ON COLUMN virtual_machines.startup_script IS '起動スクリプト';
COMMENT ON COLUMN virtual_machines.note IS '備考';
COMMENT ON COLUMN virtual_machines.order_number IS '表示順';
COMMENT ON COLUMN virtual_machines.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN virtual_machines.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN virtual_machines.created_at IS 'レコード作成日';
COMMENT ON COLUMN virtual_machines.updated_at IS 'レコード更新日';
COMMENT ON COLUMN virtual_machines.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN virtual_machines.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE virtual_machine_properties IS '仮想マシンプロパティ';
COMMENT ON COLUMN virtual_machine_properties.id IS '仮想マシンプロパティID';
COMMENT ON COLUMN virtual_machine_properties.virtual_machine_id IS '仮想マシンID';
COMMENT ON COLUMN virtual_machine_properties.property_name IS '設定名';
COMMENT ON COLUMN virtual_machine_properties.property_value IS '設定値';
COMMENT ON COLUMN virtual_machine_properties.order_number IS '順番';
COMMENT ON COLUMN virtual_machine_properties.created_at IS 'レコード作成日';
COMMENT ON COLUMN virtual_machine_properties.updated_at IS 'レコード更新日';
COMMENT ON COLUMN virtual_machine_properties.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN virtual_machine_properties.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE completion_conditions IS '完了条件';
COMMENT ON COLUMN completion_conditions.id IS '完了条件ID';
COMMENT ON COLUMN completion_conditions.virtual_machine_id IS '仮想マシンID';
COMMENT ON COLUMN completion_conditions.completion_key IS '完了キーコード';
COMMENT ON COLUMN completion_conditions.description IS '完了条件説明文';
COMMENT ON COLUMN completion_conditions.order_number IS '表示順';
COMMENT ON COLUMN completion_conditions.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN completion_conditions.created_at IS 'レコード作成日';
COMMENT ON COLUMN completion_conditions.updated_at IS 'レコード更新日';
COMMENT ON COLUMN completion_conditions.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN completion_conditions.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE completions IS '完了結果';
COMMENT ON COLUMN completions.id IS '受講結果ID';
COMMENT ON COLUMN completions.user_id IS 'ユーザID';
COMMENT ON COLUMN completions.completion_condition_id IS '完了条件ID';
COMMENT ON TABLE extensions IS '拡張機能';
COMMENT ON COLUMN extensions.id IS 'モジュールID';
COMMENT ON COLUMN extensions.virtual_machine_id IS '';
COMMENT ON COLUMN extensions.extension_type IS '拡張機能タイプ
marker:マーカー
chrome:Chrome
desktopicon:デスクトップアイコン
explorer:エクスプローラー
gmail:gmail
imageview:ImageView
notepad:メモ帳
outlook:outlook
wannacry:WannaCry
word:Word

';
COMMENT ON COLUMN extensions.extension_name IS '拡張機能名';
COMMENT ON COLUMN extensions.note IS '備考';
COMMENT ON COLUMN extensions.order_number IS '表示順';
COMMENT ON COLUMN extensions.is_invalided IS '無効フラグ
true:無効
false:有効';
COMMENT ON COLUMN extensions.is_deleted IS '削除フラグ
true:削除
false:有効';
COMMENT ON COLUMN extensions.created_at IS 'レコード作成日';
COMMENT ON COLUMN extensions.updated_at IS 'レコード更新日';
COMMENT ON COLUMN extensions.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN extensions.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE extension_explorer_properties IS '拡張機能エクスプローラ固有プロパティ';
COMMENT ON COLUMN extension_explorer_properties.id IS '拡張機能エクスプローラ固有プロパティID';
COMMENT ON COLUMN extension_explorer_properties.extension_id IS '拡張機能ID';
COMMENT ON COLUMN extension_explorer_properties.parent_id IS '親拡張機能エクスプローラ固有プロパティID
ルート直下のファイル・フォルダの場合はnullを設定';
COMMENT ON COLUMN extension_explorer_properties.target_type_code IS '対象タイプコード
1:ファイル
2:フォルダ';
COMMENT ON COLUMN extension_explorer_properties.file_name IS 'ファイル名';
COMMENT ON COLUMN extension_explorer_properties.icon_binary_file_id IS 'アイコン画像バイナリファイルID';
COMMENT ON COLUMN extension_explorer_properties.list_kind_name IS 'リスト画面種別名称';
COMMENT ON COLUMN extension_explorer_properties.propery_kind_name IS 'プロパティ画面種別名称';
COMMENT ON COLUMN extension_explorer_properties.description IS '説明文';
COMMENT ON COLUMN extension_explorer_properties.location IS 'ロケーション';
COMMENT ON COLUMN extension_explorer_properties.file_size IS 'ファイルサイズ';
COMMENT ON COLUMN extension_explorer_properties.disk_size IS 'ディスクサイズ';
COMMENT ON COLUMN extension_explorer_properties.list_size IS '';
COMMENT ON COLUMN extension_explorer_properties.file_created_at IS '作成日時';
COMMENT ON COLUMN extension_explorer_properties.file_updated_at IS '更新日時';
COMMENT ON COLUMN extension_explorer_properties.file_accessed_at IS 'アクセス日時';
COMMENT ON COLUMN extension_explorer_properties.click_event IS 'クリックイベント';
COMMENT ON COLUMN extension_explorer_properties.dblclick_event IS 'ダブルクリックイベント';
COMMENT ON COLUMN extension_explorer_properties.order_number IS '順番';
COMMENT ON COLUMN extension_explorer_properties.created_at IS 'レコード作成日';
COMMENT ON COLUMN extension_explorer_properties.updated_at IS 'レコード更新日';
COMMENT ON COLUMN extension_explorer_properties.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN extension_explorer_properties.updated_by IS 'レコード更新ユーザID';
COMMENT ON TABLE extension_properties IS '拡張機能汎用プロパティ';
COMMENT ON COLUMN extension_properties.id IS 'モジュールプロパティID';
COMMENT ON COLUMN extension_properties.extension_id IS '拡張機能ID';
COMMENT ON COLUMN extension_properties.property_name IS '設定名';
COMMENT ON COLUMN extension_properties.property_value IS '設定値';
COMMENT ON COLUMN extension_properties.is_text IS 'テキストフラグ
true:テキスト
false:テキスト以外';
COMMENT ON COLUMN extension_properties.method IS 'メソッド';
COMMENT ON COLUMN extension_properties.order_number IS '順番';
COMMENT ON COLUMN extension_properties.created_at IS 'レコード作成日';
COMMENT ON COLUMN extension_properties.updated_at IS 'レコード更新日';
COMMENT ON COLUMN extension_properties.created_by IS 'レコード作成ユーザID';
COMMENT ON COLUMN extension_properties.updated_by IS 'レコード更新ユーザID';
