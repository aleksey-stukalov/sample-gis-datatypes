create extension postgis^

-- begin CRUISESAMPLE_PORT
create table CRUISESAMPLE_PORT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LOCATION geometry not null,
    --
    primary key (ID)
)^
-- end CRUISESAMPLE_PORT
-- begin CRUISESAMPLE_ROUTE
create table CRUISESAMPLE_ROUTE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    --
    primary key (ID)
)^
-- end CRUISESAMPLE_ROUTE
-- begin CRUISESAMPLE_WAYPOINT
create table CRUISESAMPLE_WAYPOINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    POINT geometry not null,
    ETA timestamp,
    ROUTE_ID uuid,
    --
    primary key (ID)
)^
-- end CRUISESAMPLE_WAYPOINT
-- begin CRUISESAMPLE_STOP
create table CRUISESAMPLE_STOP (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ORDER_ integer not null,
    PORT_ID uuid not null,
    --
    primary key (ID)
)^
-- end CRUISESAMPLE_STOP
-- begin CRUISESAMPLE_ROUTE_STOP_LINK
create table CRUISESAMPLE_ROUTE_STOP_LINK (
    ROUTE_ID uuid,
    STOP_ID uuid,
    primary key (ROUTE_ID, STOP_ID)
)^
-- end CRUISESAMPLE_ROUTE_STOP_LINK
