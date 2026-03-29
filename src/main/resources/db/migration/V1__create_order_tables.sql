create table if not exists orders (
    id bigserial primary key,
    customer_id varchar(100) not null,
    status varchar(30) not null
);

create table if not exists order_items (
    order_id bigint not null,
    product_code varchar(100) not null,
    quantity integer not null,
    unit_price numeric(19, 2) not null,
    constraint fk_order_items_order
        foreign key (order_id)
        references orders (id)
        on delete cascade
);
