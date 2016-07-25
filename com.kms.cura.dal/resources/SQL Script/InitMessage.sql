use cura;

create table Messages (
	patient_id int not null,
    doctor_id int not null,
    sent_by_doctor boolean not null,
    time_sent datetime not null,
    message text not null,
    patient_available boolean not null,
    doctor_available boolean not null,
    FOREIGN KEY (patient_id) REFERENCES patient (user_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor (user_id)
);

insert into messages values (2, 3, false, '2016-06-18 10:34:09', 'Hello from the other side', false, true);
insert into messages values (2, 3, true, '2016-06-18 10:35:09', 'Hello from the other side', true, false);
insert into messages values (2, 3, false, '2016-06-18 10:36:09', 'Hello from the other side', true, true);
insert into messages values (2, 3, true, '2016-06-18 10:37:09', 'Hello from the other side', true, true);
insert into messages values (2, 3, false, '2016-06-18 10:38:09', 'Hello from the other side', true, true);
insert into messages values (2, 3, true, '2016-06-18 10:39:09', 'Hello from the other side', true, true);
insert into messages values (2, 3, false, '2016-06-18 10:40:09', 'Hello from the other side', true, true);
insert into messages values (2, 4, true, '2016-06-19 11:37:09', 'Hello from the other side', true, true);
insert into messages values (2, 4, false, '2016-06-19 11:38:09', 'Hello from the other side', true, true);
insert into messages values (2, 4, true, '2016-06-19 11:39:09', 'Hello from the other side', true, true);
insert into messages values (2, 4, false, '2016-06-19 11:40:09', 'Hello from the other side', true, true);
insert into messages values (2, 5, true, '2016-06-19 12:37:09', 'Hello from the other side', true, true);
insert into messages values (2, 5, false, '2016-06-19 12:38:09', 'Hello from the other side', true, true);
insert into messages values (2, 5, true, '2016-06-20 12:39:09', 'Hello from the other side', true, true);
insert into messages values (2, 5, false, '2016-06-20 12:40:09', 'Hello from the other side', true, true);
