alter table photos
    add column bytes bytea not null default E'\\000',
    add column type VARCHAR(50) not null default 'png';