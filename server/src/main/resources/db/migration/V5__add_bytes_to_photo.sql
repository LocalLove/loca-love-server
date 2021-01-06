alter table photos
    add column bytes bytea not null default 0,
    add column type VARCHAR(50) not null default 'png';