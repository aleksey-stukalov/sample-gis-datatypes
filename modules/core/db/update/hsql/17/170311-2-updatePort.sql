alter table CRUISESAMPLE_PORT add column LOCATION LONGVARCHAR ^
alter table CRUISESAMPLE_PORT alter column LOCATION set not null ;
alter table CRUISESAMPLE_PORT drop column POINT cascade ;
