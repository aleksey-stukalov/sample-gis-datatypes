-- begin CRUISESAMPLE_WAYPOINT
alter table CRUISESAMPLE_WAYPOINT add constraint FK_CRUISESAMPLE_WAYPOINT_ROUTE foreign key (ROUTE_ID) references CRUISESAMPLE_ROUTE(ID)^
create index IDX_CRUISESAMPLE_WAYPOINT_ROUTE on CRUISESAMPLE_WAYPOINT (ROUTE_ID)^
-- end CRUISESAMPLE_WAYPOINT
-- begin CRUISESAMPLE_STOP
alter table CRUISESAMPLE_STOP add constraint FK_CRUISESAMPLE_STOP_PORT foreign key (PORT_ID) references CRUISESAMPLE_PORT(ID)^
create index IDX_CRUISESAMPLE_STOP_PORT on CRUISESAMPLE_STOP (PORT_ID)^
-- end CRUISESAMPLE_STOP
-- begin CRUISESAMPLE_ROUTE_STOP_LINK
alter table CRUISESAMPLE_ROUTE_STOP_LINK add constraint FK_ROUSTO_ROUTE foreign key (ROUTE_ID) references CRUISESAMPLE_ROUTE(ID)^
alter table CRUISESAMPLE_ROUTE_STOP_LINK add constraint FK_ROUSTO_STOP foreign key (STOP_ID) references CRUISESAMPLE_STOP(ID)^
-- end CRUISESAMPLE_ROUTE_STOP_LINK
