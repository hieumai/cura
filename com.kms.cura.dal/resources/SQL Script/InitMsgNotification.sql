USE cura;

CREATE TABLE Msg_Notification (
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
    ref_id INT NOT NULL,
    status BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (ref_id) REFERENCES messages (id),
    primary key (id)
);

insert into msg_notification (user_id, ref_id, status) values (2, 3, false);
insert into msg_notification (user_id, ref_id, status) values (3, 1, true);