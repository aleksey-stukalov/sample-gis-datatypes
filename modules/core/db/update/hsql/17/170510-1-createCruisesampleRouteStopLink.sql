create table CRUISESAMPLE_ROUTE_STOP_LINK (
    ROUTE_ID varchar(36) not null,
    STOP_ID varchar(36) not null,
    primary key (ROUTE_ID, STOP_ID)
);
