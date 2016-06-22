USE cura;

ALTER TABLE doctor_facilities ADD week_day INT NOT NULL;

ALTER TABLE doctor_facilities ADD time_open TIME NOT NULL;

ALTER TABLE doctor_facilities ADD time_close TIME NOT NULL;