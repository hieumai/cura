USE cura;

CREATE TABLE Insurances_Facilities (
	insurance_id INT NOT NULL,
    facility_id INT NOT NULL,
    FOREIGN KEY (insurance_id) REFERENCES insurance (id),
    FOREIGN KEY (facility_id) REFERENCES facilities (id)
    );
    
INSERT INTO Insurances_Facilities VALUE(1, 1);
INSERT INTO Insurances_Facilities VALUE(1, 2);
INSERT INTO Insurances_Facilities VALUE(2, 3);
INSERT INTO Insurances_Facilities VALUE(2, 4);
INSERT INTO Insurances_Facilities VALUE(2, 5);
INSERT INTO Insurances_Facilities VALUE(3, 6);
INSERT INTO Insurances_Facilities VALUE(4, 7);
INSERT INTO Insurances_Facilities VALUE(4, 1);
INSERT INTO Insurances_Facilities VALUE(5, 2);
INSERT INTO Insurances_Facilities VALUE(5, 3);
INSERT INTO Insurances_Facilities VALUE(5, 4);
INSERT INTO Insurances_Facilities VALUE(6, 5);
INSERT INTO Insurances_Facilities VALUE(6, 6);
INSERT INTO Insurances_Facilities VALUE(7, 7);
INSERT INTO Insurances_Facilities VALUE(8, 1);
INSERT INTO Insurances_Facilities VALUE(8, 2);
INSERT INTO Insurances_Facilities VALUE(9, 3);
INSERT INTO Insurances_Facilities VALUE(9, 4);
INSERT INTO Insurances_Facilities VALUE(10, 5);
INSERT INTO Insurances_Facilities VALUE(10, 6);
INSERT INTO Insurances_Facilities VALUE(11, 7);
INSERT INTO Insurances_Facilities VALUE(11, 1);