USE cura;

CREATE TABLE Appt_Notification (
	user_id INT NOT NULL,
    ref_id INT NOT NULL,
    status BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (ref_id) REFERENCES appointments (id)
);

insert into appt_notification values (3, 10, false);
insert into appt_notification values (2, 2, false);