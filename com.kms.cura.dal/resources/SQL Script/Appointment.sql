USE cura;

CREATE TABLE Appointments (
	id INT NOT NULL AUTO_INCREMENT,
	doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    facility_id INT NOT NULL,
    appt_day DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    current_status INT NOT NULL,
    patient_cmt text,
    doctor_cmt text,
    FOREIGN KEY (doctor_id) REFERENCES doctor_facilities (doctor_id),
    FOREIGN KEY (patient_id) REFERENCES patient (user_id),
    FOREIGN KEY (facility_id) REFERENCES doctor_facilities (facility_id),
    PRIMARY KEY (id)
);

INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (3, 2, 8, 20160830, '13:00:00', '14:00:00', 0);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (4, 2, 3, 20160825, '09:00:00', '10:00:00', 2);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (9, 2, 1, 20160830, '08:00:00', '10:00:00', 1);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (7, 2, 7, 20160830, '15:00:00', '16:30:00', 3);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (8, 2, 7, 20160830, '15:00:00', '16:30:00', 4);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (5, 2, 7, 20160830, '15:00:00', '16:30:00', 5);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (6, 2, 7, 20160830, '15:00:00', '16:30:00', 6);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (3, 2, 8, 20160830, '08:00:00', '09:00:00', 6);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (3, 2, 8, 20160830, '15:00:00', '16:30:00', 6);
INSERT INTO Appointments (doctor_id, patient_id, facility_id, appt_day, start_time, end_time, current_status) VALUES (3, 2, 8, 20160830, '13:00:00', '14:30:00', 6);
