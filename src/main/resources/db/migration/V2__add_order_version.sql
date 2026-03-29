alter table orders
    add column if not exists version bigint not null default 0;
