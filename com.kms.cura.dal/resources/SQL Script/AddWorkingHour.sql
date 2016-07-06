USE cura;

ALTER TABLE doctor_facilities ADD working_day INT;

ALTER TABLE doctor_facilities ADD start_working_time TIME;

ALTER TABLE doctor_facilities ADD end_working_time TIME;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 3;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 4;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 5;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 6;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 7;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 8;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 9;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 10;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 11;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 12;

INSERT INTO doctor_facilities(doctor_id,facility_id,working_day,start_working_time,end_working_time)
SELECT A.doctor_id,A.facility_id,B.week_day, B.time_open, B.time_close
FROM doctor_facilities as A INNER JOIN opening_hours as B 
WHERE B.facility_id = A.facility_id AND A.doctor_id = 13;


DELETE FROM doctor_facilities WHERE start_working_time IS NULL AND working_day IS NULL AND end_working_time IS NULL LIMIT 11;