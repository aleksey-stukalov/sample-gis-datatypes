create table CRUISESAMPLE_ROUTE_PORT_LINK (
    ROUTE_ID varchar(36) not null,
    PORT_ID varchar(36) not null,
    primary key (ROUTE_ID, PORT_ID)
);
