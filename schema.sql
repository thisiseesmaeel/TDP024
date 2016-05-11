drop table if exists task cascade;
create table task(
    id serial primary key,
    name text not null,
    done boolean default false,
    list int references list(id)
);

drop table if exists list cascade;
create table list(
    id serial primary key,
    name text not null
);
