-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    login varchar(50) UNIQUE NOT NULL,
    password varchar(50) NOT NULL
);

insert into client (id, name, login, password)
values (
    nextval('client_SEQ'),
    'Admin',
    'admin',
    'admin'
);
