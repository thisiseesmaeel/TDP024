drop table if exists list cascade;
create table list(
    id serial primary key,
    name text not null
);
