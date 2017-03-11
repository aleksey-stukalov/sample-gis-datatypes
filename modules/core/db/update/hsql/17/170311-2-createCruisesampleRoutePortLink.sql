alter table CRUISESAMPLE_ROUTE_PORT_LINK add constraint FK_CRPL_ROUTE foreign key (ROUTE_ID) references CRUISESAMPLE_ROUTE(ID);
alter table CRUISESAMPLE_ROUTE_PORT_LINK add constraint FK_CRPL_PORT foreign key (PORT_ID) references CRUISESAMPLE_PORT(ID);
