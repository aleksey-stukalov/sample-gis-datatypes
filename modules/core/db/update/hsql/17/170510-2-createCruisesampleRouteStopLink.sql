alter table CRUISESAMPLE_ROUTE_STOP_LINK add constraint FK_CRSL_ROUTE foreign key (ROUTE_ID) references CRUISESAMPLE_ROUTE(ID);
alter table CRUISESAMPLE_ROUTE_STOP_LINK add constraint FK_CRSL_STOP foreign key (STOP_ID) references CRUISESAMPLE_STOP(ID);
