alter table CRUISESAMPLE_ROUTE add column NAME varchar(255) ^
update CRUISESAMPLE_ROUTE set NAME = '' where NAME is null ;
alter table CRUISESAMPLE_ROUTE alter column NAME set not null ;
