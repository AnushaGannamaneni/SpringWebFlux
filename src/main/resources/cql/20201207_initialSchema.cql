CREATE TABLE IF NOT EXISTS division (division_id int, partner_id int, division_name text, account_number bigint,
unit_number bigint, state text, country text,
primary key(division_id, partner_id));