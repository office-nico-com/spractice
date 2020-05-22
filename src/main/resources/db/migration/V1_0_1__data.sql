-- SQL statement to input initial data.
-- It's run every time you start the application,
-- So consider not running it if it already exists

INSERT INTO organizations(organization_name, organization_name_ja, description, is_invalided, is_deleted, created_at, updated_at, created_by, updated_by) VALUES ('nico', 'System Design Office NICO', '管理用組織', false, false, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, 1, 1);
INSERT INTO organizations(organization_name, organization_name_ja, description, is_invalided, is_deleted, created_at, updated_at, created_by, updated_by) VALUES ('securitier', 'セキュリティア', '管理用組織', false, false, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, 1, 1);


INSERT INTO users(account, passwd, family_name, given_name, family_name_kana, given_name_kana, role_code, is_invalided, is_deleted, created_at, updated_at, created_by, updated_by) VALUES ('administrator', 'dummy', '管理者', '管理者', 'カンリシャ', 'カンリシャ', 1, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);
