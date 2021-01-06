create table email_change_tokens(
    id bigserial primary key,
    email varchar(250) not null,
    value uuid not null unique,
    user_id bigint not null references users on update cascade on delete cascade,
    creation_time timestamp not null
);