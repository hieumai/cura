USE cura;

CREATE TABLE Msg_Notification (
	user_id INT NOT NULL,
    ref_id INT NOT NULL,
    status BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (ref_id) REFERENCES messages (id)
);

insert into msg_notification values (2, 3, false);
insert into msg_notification values (3, 1, true);