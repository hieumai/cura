create table Patient_Health (
	patient_id INT NOT NULL,
    condition_id INT,
    symptom_id INT,
    start_date date NOT NULL,
    end_date date,
    FOREIGN KEY (patient_id) REFERENCES patient (user_id),
    FOREIGN KEY (condition_id) REFERENCES conditions (id),
    FOREIGN KEY (symptom_id) REFERENCES symptoms (id)
);

insert into patient_health (patient_id, condition_id, start_date, end_date) values (2, 2, 19991212, 20031212);
insert into patient_health (patient_id, condition_id, start_date, end_date) values (2, 4, 20031212, 20061212);
insert into patient_health (patient_id, condition_id, start_date, end_date) values (2, 10, 20091212, 20121212);
insert into patient_health (patient_id, condition_id, start_date, end_date) values (2, 14, 20061212, 20091212);
insert into patient_health (patient_id, condition_id, start_date) value (2,6, 20160303);
insert into patient_health (patient_id, condition_id, start_date) value (2,20, 20160404);
insert into patient_health (patient_id, condition_id, start_date) value (2,8, 20160505);
insert into patient_health (patient_id, condition_id, start_date) value (2,18, 20160101);
insert into patient_health (patient_id, symptom_id, start_date, end_date) values (2, 2, 19991212, 20031212);
insert into patient_health (patient_id, symptom_id, start_date, end_date) values (2, 4, 20031212, 20061212);
insert into patient_health (patient_id, symptom_id, start_date, end_date) values (2, 10, 20091212, 20121212);
insert into patient_health (patient_id, symptom_id, start_date, end_date) values (2, 14, 20061212, 20091212);
insert into patient_health (patient_id, symptom_id, start_date) value (2,6, 20160303);
insert into patient_health (patient_id, symptom_id, start_date) value (2,20, 20160404);
insert into patient_health (patient_id, symptom_id, start_date) value (2,8, 20160202);
insert into patient_health (patient_id, symptom_id, start_date) value (2,18, 20160606);

ALTER TABLE patient_health
ADD note text;

