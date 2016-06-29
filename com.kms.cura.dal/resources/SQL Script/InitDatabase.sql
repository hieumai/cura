CREATE DATABASE cura
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;
USE cura;

CREATE TABLE Facilities(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    address VARCHAR(200) NOT NULL,
	city VARCHAR(50) NOT NULL,
	city_keyword VARCHAR(50) NOT NULL,
    phone VARCHAR(50),
    PRIMARY KEY (id)
    );

CREATE TABLE Opening_hours(
	facility_id INT NOT NULL,
    week_day INT NOT NULL, #0 = monday, 6 = sunday
    time_open TIME NOT NULL,
    time_close TIME NOT NULL,
    FOREIGN KEY (facility_id) REFERENCES Facilities (id)
);

#Hue hospital data     
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Hue Central Hospital', 'Lê Lợi, Vĩnh Ninh, tp. Huế, Thừa Thiên Huế, Vietnam','Huế','hue', '+84 54 3822 325');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),0,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),1,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),2,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),3,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),4,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),5,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Hue Central Hospital'),6,'00:00:00','23:59:59');
 
# Cho Ray hospital
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Cho Ray Hospital', '201B Nguyễn Chí Thanh, phường 12, Hồ Chí Minh, Vietnam','Hồ Chí Minh','ho chi minh', '+84 8 3955 9856');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),0,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),1,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),2,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),3,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),4,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),5,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Cho Ray Hospital'),6,'00:00:00','23:59:59');

# Starlight Dental Clinic
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Starlight Dental Clinic', '2 Bis, Cong Truong Quoc Te Street, District 3, Hồ Chí Minh, Vietnam','Hồ Chí Minh','ho chi minh', '+84 8 3822 6222');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),0,'08:00:00','20:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),1,'08:00:00','20:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),2,'08:00:00','20:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),3,'08:00:00','20:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),4,'08:00:00','20:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),5,'08:00:00','20:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Starlight Dental Clinic'),6,'08:00:00','20:30:00');

# Family Medical Practice
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Family Medical Practice', '50 Nguyễn Văn Linh, Dương Nam, Đà Nẵng, Vietnam','Đà Nẵng','da nang', '+84 511 3582 699');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),0,'08:00:00','17:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),1,'08:00:00','17:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),2,'08:00:00','17:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),3,'08:00:00','17:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),4,'08:00:00','17:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),5,'08:00:00','17:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Family Medical Practice'),6,'08:00:00','12:00:00');

# Vinaoptic
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Vinaoptic', '245 Nam Kỳ Khởi Nghĩa, Ho Chi Minh City, Vietnam', 'Hồ Chí Minh','ho chi minh','+84 8 3822 7228');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),0,'08:00:00','21:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),1,'08:00:00','21:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),2,'08:00:00','21:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),3,'08:00:00','21:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),4,'08:00:00','21:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),5,'08:00:00','21:30:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vinaoptic'),6,'08:00:00','21:00:00');

# Maple Healthcare Center (this one is close on Sunday)
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Maple Healthcare Center', 'Lô MD6,, Nguyễn Lương Bằng, Tân Phú, Hồ Chí Minh, Tp. Ho Chi Minh 700000, Vietnam','Hồ Chí Minh','ho chi minh', '+84 8 5410 0100');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Maple Healthcare Center'),0,'08:30:00','19:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Maple Healthcare Center'),1,'08:30:00','19:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Maple Healthcare Center'),2,'08:30:00','19:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Maple Healthcare Center'),3,'08:30:00','19:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Maple Healthcare Center'),4,'08:30:00','19:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Maple Healthcare Center'),5,'08:30:00','18:00:00');

# Vietnam National Hospital of Pediatrics
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('Vietnam National Hospital of Pediatrics', '18/879 La Thành, Đống Đa, Hà Nội, Vietnam','Hà Nội','ha noi', '+84 4 6273 8573');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),0,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),1,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),2,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),3,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),4,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),5,'00:00:00','23:59:59');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'Vietnam National Hospital of Pediatrics'),6,'00:00:00','23:59:59');

# QuickCare Clinic
INSERT INTO Facilities(name,address,city,city_keyword,phone) VALUES ('QuickCare Clinic', '77 Nguyễn Hữu Cầu, Tân Định, Quận 1, Hồ Chí Minh, Vietnam','Hồ Chí Minh','ho chi minh', '+84 8 3910 4545');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),0,'08:00:00','20:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),1,'08:00:00','20:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),2,'08:00:00','20:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),3,'08:00:00','20:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),4,'08:00:00','20:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),5,'08:00:00','20:00:00');
INSERT INTO Opening_hours (facility_id,week_day,time_open,time_close) VALUES ((select id from Facilities where name = 'QuickCare Clinic'),6,'08:00:00','12:00:00');

CREATE TABLE Specialties(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
    );
    
INSERT INTO Specialties(name) VALUES ('Acupuncturist');
INSERT INTO Specialties(name) VALUES ('Allergist');
INSERT INTO Specialties(name) VALUES ('Audiologist');
INSERT INTO Specialties(name) VALUES ('Cardiologist');
INSERT INTO Specialties(name) VALUES ('Cardiothoracic Surgeon');
INSERT INTO Specialties(name) VALUES ('Chiropractor');
INSERT INTO Specialties(name) VALUES ('Colorectal Surgeon');
INSERT INTO Specialties(name) VALUES ('Dentist');
INSERT INTO Specialties(name) VALUES ('Dermatologist');
INSERT INTO Specialties(name) VALUES ('Dietitian/Nutritionist');
INSERT INTO Specialties(name) VALUES ('Ear, Nose, & Throat Doctor');
INSERT INTO Specialties(name) VALUES ('Endocrinologist');
INSERT INTO Specialties(name) VALUES ('Gastroenterologist');
INSERT INTO Specialties(name) VALUES ('Geriatrician');
INSERT INTO Specialties(name) VALUES ('Hematologist');
INSERT INTO Specialties(name) VALUES ('Infectious Disease Specialist');
INSERT INTO Specialties(name) VALUES ('Infertility Specialist');
INSERT INTO Specialties(name) VALUES ('Midwife');
INSERT INTO Specialties(name) VALUES ('Naturopathic');
INSERT INTO Specialties(name) VALUES ('Nephrologist');
INSERT INTO Specialties(name) VALUES ('Neurologist');
INSERT INTO Specialties(name) VALUES ('OB-GYN');
INSERT INTO Specialties(name) VALUES ('Oncologist');
INSERT INTO Specialties(name) VALUES ('Ophthalmologist');
INSERT INTO Specialties(name) VALUES ('Optometrist');
INSERT INTO Specialties(name) VALUES ('Oral surgeon');
INSERT INTO Specialties(name) VALUES ('Orthodontist');
INSERT INTO Specialties(name) VALUES ('Orthopedic Surgeon');
INSERT INTO Specialties(name) VALUES ('Pain Management Specialist');
INSERT INTO Specialties(name) VALUES ('Pediatric Dentist');
INSERT INTO Specialties(name) VALUES ('Pediatrician');
INSERT INTO Specialties(name) VALUES ('Physiatrist');
INSERT INTO Specialties(name) VALUES ('Physical Therapist');
INSERT INTO Specialties(name) VALUES ('Plastic Surgeon');
INSERT INTO Specialties(name) VALUES ('Podiatrist');
INSERT INTO Specialties(name) VALUES ('Primary Care Doctor');
INSERT INTO Specialties(name) VALUES ('Prosthodontist');
INSERT INTO Specialties(name) VALUES ('Psychiatrist');
INSERT INTO Specialties(name) VALUES ('Psychologist');
INSERT INTO Specialties(name) VALUES ('Pulmonologist');
INSERT INTO Specialties(name) VALUES ('Radiologist');
INSERT INTO Specialties(name) VALUES ('Rheumatologist');
INSERT INTO Specialties(name) VALUES ('Sleep Medicine Specialist');
INSERT INTO Specialties(name) VALUES ('Sports Medicine Specialist');
INSERT INTO Specialties(name) VALUES ('Surgeon');
INSERT INTO Specialties(name) VALUES ('Therapist/Counselor');
INSERT INTO Specialties(name) VALUES ('Urgent Care Doctor');
INSERT INTO Specialties(name) VALUES ('Urological Surgeon');
INSERT INTO Specialties(name) VALUES ('Urologist');
INSERT INTO Specialties(name) VALUES ('Vascular Surgeon');

CREATE TABLE Degree(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
    );
    
INSERT INTO Degree(name) VALUES ('MD');
INSERT INTO Degree(name) VALUES ('DO');
INSERT INTO Degree(name) VALUES ('MBBS');
INSERT INTO Degree(name) VALUES ('MBchB');
INSERT INTO Degree(name) VALUES ('DMD');
INSERT INTO Degree(name) VALUES ('DDS');
INSERT INTO Degree(name) VALUES ('DPM');
INSERT INTO Degree(name) VALUES ('OD');
INSERT INTO Degree(name) VALUES ('PsyD');
INSERT INTO Degree(name) VALUES ('PhD');


CREATE TABLE Users (
	id INT UNIQUE NOT NULL AUTO_INCREMENT,
	email VARCHAR(50) UNIQUE,
    password VARCHAR(30),
    PRIMARY KEY (id)
	);
    
INSERT INTO Users (email,password) VALUES ('admin@kms-technology.com','admin123');
INSERT INTO Users (email,password) VALUE ('patient@kms-technology.com','patient123');
INSERT INTO Users (email,password) VALUE ('doctor@kms-technology.com','doctor123');

CREATE TABLE Admin (
	user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id)
    );
    
INSERT INTO Admin(user_id) VALUES ((SELECT id FROM Users WHERE email = 'admin@kms-technology.com'));

CREATE TABLE Patient (
	user_id INT UNIQUE NOT NULL,
	name VARCHAR(30),
    gender CHAR(1),
	birth DATE,
    location VARCHAR(50),
    insurance VARCHAR(10),
    health_concern VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users (id)
    );

INSERT INTO Patient(user_id,name,gender,birth,location) VALUES ((SELECT id FROM Users WHERE email = 'patient@kms-technology.com'), 'Alan Turing','M','19900101','Hồ Chí Minh');

CREATE TABLE Doctor (
    user_id INT UNIQUE NOT NULL,
    name VARCHAR(30),
    phone VARCHAR(10),
    degree_id INT,
    rating DOUBLE,
    experience INT,
    price_min DOUBLE,
    price_max DOUBLE,
    gender CHAR(1),
    birth DATE,
    insurance VARCHAR(10),
    FOREIGN KEY (user_id)
        REFERENCES Users (id),
    FOREIGN KEY (degree_id)
        REFERENCES Degree (id)
	);
    

#Hold relationship between doctor and specialites (many-many)
CREATE TABLE Doctor_Specialties (
	doctor_id INT NOT NULL,
    speciality_id INT NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES Doctor (user_id),
    FOREIGN KEY (speciality_id) REFERENCES Specialties (id)
    );

#Hold relationship between doctor and facilities (many-many)
CREATE TABLE Doctor_Facilities (
	doctor_id INT NOT NULL,
    facility_id INT NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES Doctor (user_id),
    FOREIGN KEY (facility_id) REFERENCES Facilities (id)
    );
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience) VALUES ((SELECT id FROM Users WHERE email = 'doctor@kms-technology.com'),'Alexander Fleming', (SELECT id FROM Degree WHERE name = 'PhD'), 'M', '19801230', 5);

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'doctor@kms-technology.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'doctor@kms-technology.com'), (SELECT id FROM specialties WHERE name = 'Ear, Nose, & Throat Doctor'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'doctor@kms-technology.com'), (SELECT id FROM facilities WHERE name = 'QuickCare Clinic'));

CREATE TABLE Rating (
	id INT NOT NULL AUTO_INCREMENT,
	doctor_id INT NOT NULL,
    value DOUBLE,
    votes INT,
    PRIMARY KEY (id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor (user_id)
    );
 
 INSERT INTO Users (email,password) VALUE ('awruck@yahoo.com','annabellewruck');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'awruck@yahoo.com'),'Annabelle Wruck', (SELECT id FROM Degree WHERE name = 'DDS'), 'F', '19700311', 14,4.5,100,250,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'awruck@yahoo.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'awruck@yahoo.com'), (SELECT id FROM specialties WHERE name = 'Orthodontist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'awruck@yahoo.com'), (SELECT id FROM facilities WHERE name = 'Starlight Dental Clinic'));




INSERT INTO Users (email,password) VALUE ('mitrak@gmail.com','mitchellrakes');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'mitrak@gmail.com'),'Mitchell Rakes', (SELECT id FROM Degree WHERE name = 'DDS'), 'M', '19801212', 6,4.0,100,250,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'mitrak@gmail.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'mitrak@gmail.com'), (SELECT id FROM specialties WHERE name = 'Pediatric Dentist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'mitrak@gmail.com'), (SELECT id FROM facilities WHERE name = 'Vietnam National Hospital of Pediatrics'));




INSERT INTO Users (email,password) VALUE ('gmac@gmail.com','glorianamacneil');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'gmac@gmail.com'),'Gloriana Macneil', (SELECT id FROM Degree WHERE name = 'PsyD'), 'F', '19760402', 10,4.0,100,350,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'gmac@gmail.com'), (SELECT id FROM specialties WHERE name = 'Psychiatrist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'gmac@gmail.com'), (SELECT id FROM specialties WHERE name = 'Psychologist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'gmac@gmail.com'), (SELECT id FROM facilities WHERE name = 'Maple Healthcare Center'));





INSERT INTO Users (email,password) VALUE ('nguminh@yahoo.com','minhnguyen');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'nguminh@yahoo.com'),'Minh Nguyen', (SELECT id FROM Degree WHERE name = 'DDS'), 'M', '19660716', 16,4.0,100,4000,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'nguminh@yahoo.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'nguminh@yahoo.com'), (SELECT id FROM specialties WHERE name = 'Oral Surgeon'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'nguminh@yahoo.com'), (SELECT id FROM facilities WHERE name = 'Cho Ray Hospital'));



INSERT INTO Users (email,password) VALUE ('owenpar@yahoo.com','parvinowen');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'owenpar@yahoo.com'),'Parvin Owen', (SELECT id FROM Degree WHERE name = 'OD'), 'M', '19660716', 6,3.5,50,75,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'owenpar@yahoo.com'), (SELECT id FROM specialties WHERE name = 'Optometrist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'owenpar@yahoo.com'), (SELECT id FROM facilities WHERE name = 'Vinaoptic'));





INSERT INTO Users (email,password) VALUE ('catwhitney@gmail.com','cathrinwhitney');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'catwhitney@gmail.com'),'Cathrin Whitney', (SELECT id FROM Degree WHERE name = 'MD'), 'F', '19720518', 15,4.5,100,200,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'catwhitney@gmail.com'), (SELECT id FROM specialties WHERE name = 'Primary Care Doctor'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'catwhitney@gmail.com'), (SELECT id FROM specialties WHERE name = 'Urologist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'catwhitney@gmail.com'), (SELECT id FROM specialties WHERE name = 'OB-GYN'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'catwhitney@gmail.com'), (SELECT id FROM facilities WHERE name = 'Hue Central Hospital'));


INSERT INTO Users (email,password) VALUE ('jonjon@yahoo.com','jonasjonker');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'jonjon@yahoo.com'),'Jonas Jonker', (SELECT id FROM Degree WHERE name = 'DMD'), 'F', '19720518', 6,3.5,100,200,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'jonjon@yahoo.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'jonjon@yahoo.com'), (SELECT id FROM facilities WHERE name = 'Family Medical Practice'));


INSERT INTO Users (email,password) VALUE ('vanhuong@gmail.com','huongvan');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'vanhuong@gmail.com'),'Huong Van', (SELECT id FROM Degree WHERE name = 'DMD'), 'F', '19781229', 10,4.0,200,300,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'vanhuong@gmail.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'vanhuong@gmail.com'), (SELECT id FROM specialties WHERE name = 'Orthodontist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'vanhuong@gmail.com'), (SELECT id FROM facilities WHERE name = 'Starlight Dental Clinic'));




INSERT INTO Users (email,password) VALUE ('armendoza@gmail.com','armenmendoza');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'armendoza@gmail.com'),'Armen Mendoza', (SELECT id FROM Degree WHERE name = 'DDS'), 'M', '19781229', 3,3.0,200,300,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'armendoza@gmail.com'), (SELECT id FROM specialties WHERE name = 'Dentist'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'armendoza@gmail.com'), (SELECT id FROM specialties WHERE name = 'Orthodontist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'armendoza@gmail.com'), (SELECT id FROM facilities WHERE name = 'Maple Healthcare Center'));

 
 
 
 
INSERT INTO Users (email,password) VALUE ('reigun@gmail.com','reigunnarson');
    
INSERT INTO Doctor(user_id,name, degree_id,gender,birth,experience,rating,price_min,price_max,phone) VALUES ((SELECT id FROM Users WHERE email = 'reigun@gmail.com'),'Rei Gunnarson', (SELECT id FROM Degree WHERE name = 'DDS'), 'M', '19781229', 20,3.0,200,600,'123456789');

INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'reigun@gmail.com'), (SELECT id FROM specialties WHERE name = 'Primary Care Doctor'));
INSERT INTO Doctor_Specialties(doctor_id,speciality_id) VALUES ((SELECT id FROM Users WHERE email = 'reigun@gmail.com'), (SELECT id FROM specialties WHERE name = 'Oncologist'));

INSERT INTO Doctor_Facilities(doctor_id,facility_id) VALUES ((SELECT id FROM Users WHERE email = 'reigun@gmail.com'), (SELECT id FROM facilities WHERE name = 'Cho Ray Hospital'));

 
