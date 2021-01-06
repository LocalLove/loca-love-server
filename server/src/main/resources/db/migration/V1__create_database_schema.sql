create table photos(
    id bigserial primary key,
    last_update_time timestamp not null,
    owner_id bigint not null
);

create table users(
    id bigserial primary key,
    email varchar(320) unique not null,
    login varchar(512) unique not null,
    password varchar(4096) not null,
    name varchar(512) not null,
    gender varchar(512) not null,
    birth_date date not null,
    status varchar(512) not null default '',
    bio varchar(4096) not null default '',
    avatar_id bigint references photos on update cascade on delete set null
);

alter table photos
    add constraint fk_users
    foreign key (owner_id)
    references users on update cascade on delete cascade;

create table users_likes(
    user_id bigint not null references users on update cascade on delete cascade,
    liked_user_id bigint not null references users on update cascade on delete cascade,
    primary key (user_id, liked_user_id),
    check (user_id is distinct from liked_user_id)
);

create table roles(
    id bigserial primary key,
    name varchar(256) not null
);

create table users_roles(
    user_id bigint not null references users on update cascade on delete cascade,
    role_id bigint not null references roles on update cascade on delete cascade,
    primary key (user_id, role_id)
);

create table email_tokens(
     id bigserial primary key,
     value uuid not null unique,
     user_id bigint not null references users on update cascade on delete cascade,
     creation_time timestamp not null
);