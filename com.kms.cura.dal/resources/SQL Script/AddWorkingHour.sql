USE cura;

ALTER TABLE doctor_facilities ADD working_day INT NOT NULL;

ALTER TABLE doctor_facilities ADD start_working_time TIME NOT NULL;

ALTER TABLE doctor_facilities ADD end_working_time TIME NOT NULL;