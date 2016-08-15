USE cura;

CREATE TABLE Appt_Notification (
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
    ref_id INT NOT NULL,
    status BOOLEAN,
    appt_noti_type text,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (ref_id) REFERENCES appointments (id),
    primary key (id)
);

insert into appt_notification (user_id, ref_id, status, appt_noti_type) values (3, 10, false, 'update_appt');
insert into appt_notification (user_id, ref_id, status, appt_noti_type) values (2, 2, false, 'new_appt');