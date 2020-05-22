INSERT INTO binary_file_categories (
organization_id,
category_name,
order_number,
can_delete,
created_at,
updated_at,
created_by,
updated_by)
VALUES
(1,
'未分類',
1,
false,
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1
);

INSERT INTO binary_file_categories (
organization_id,
category_name,
order_number,
can_delete,
created_at,
updated_at,
created_by,
updated_by)
VALUES
(1,
'イメージ',
2,
true,
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1
);

INSERT INTO binary_file_categories (
organization_id,
category_name,
order_number,
can_delete,
created_at,
updated_at,
created_by,
updated_by)
VALUES
(1,
'書庫',
3,
true,
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1
);
