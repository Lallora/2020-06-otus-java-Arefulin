-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
--create table users(id long auto_increment, name varchar(50));

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create table users (id bigint not null, name varchar(255), primary key (id));
create sequence hibernate_sequence start with 1 increment by 1;