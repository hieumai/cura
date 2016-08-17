use cura;

create table Password_Code (
	user_id int not null,
    code varchar(5) not null,
    request_time timestamp not null
);

insert into Password_Code values (2, 'QWERT', '20160808154200'); 