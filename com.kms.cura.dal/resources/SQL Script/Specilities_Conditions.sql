USE cura;

CREATE TABLE Specialities_Conditions (
	speciality_id INT NOT NULL,
    condition_id INT NOT NULL,
    FOREIGN KEY (speciality_id) REFERENCES specialties (id),
    FOREIGN KEY (condition_id) REFERENCES conditions (id)
    );
    
INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Allergist'), (SELECT id FROM Conditions WHERE name = 'Allergies'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Allergist'), (SELECT id FROM Conditions WHERE name = 'Asthma'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Cardiologist'), (SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Cardiothoracic Surgeon'), (SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Cardiothoracic Surgeon'), (SELECT id FROM Conditions WHERE name = 'Lung Cancer'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Chiropractor'), (SELECT id FROM Conditions WHERE name = 'Arthritis'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Dentist'), (SELECT id FROM Conditions WHERE name = 'Gum Disease (Periodontal Disease)'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Dermatologist'), (SELECT id FROM Conditions WHERE name = 'Eczema (Atopic Dermatitis)'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Dietitian/Nutritionist'), (SELECT id FROM Conditions WHERE name = 'Diabetes'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Ear, Nose, & Throat Doctor'), (SELECT id FROM Conditions WHERE name = 'Allergies'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Ear, Nose, & Throat Doctor'), (SELECT id FROM Conditions WHERE name = 'Ear Infection'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Ear, Nose, & Throat Doctor'), (SELECT id FROM Conditions WHERE name = 'Strep Throat'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Endocrinologist'), (SELECT id FROM Conditions WHERE name = 'Diabetes'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Gastroenterologist'), (SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Gastroenterologist'), (SELECT id FROM Conditions WHERE name = 'Hepatitis'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Geriatrician'), (SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Hematologist'), (SELECT id FROM Conditions WHERE name = 'Anemia'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Hematologist'), (SELECT id FROM Conditions WHERE name = 'Leukemia'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Infectious Disease Specialist'), (SELECT id FROM Conditions WHERE name = 'HIV/AIDS'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Infectious Disease Specialist'), (SELECT id FROM Conditions WHERE name = 'Meningitis'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Infectious Disease Specialist'), (SELECT id FROM Conditions WHERE name = 'Pneumonia'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Nephrologist'), (SELECT id FROM Conditions WHERE name = 'Kidney Stones'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Neurologist'), (SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Neurologist'), (SELECT id FROM Conditions WHERE name = 'Brain Tumor'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Neurologist'), (SELECT id FROM Conditions WHERE name = 'Stroke'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Neurologist'), (SELECT id FROM Conditions WHERE name = 'Meningitis'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'OB-GYN'), (SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'OB-GYN'), (SELECT id FROM Conditions WHERE name = 'HIV/AIDS'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Oncologist'), (SELECT id FROM Conditions WHERE name = 'Leukemia'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Oncologist'), (SELECT id FROM Conditions WHERE name = 'Lung Cancer'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Oncologist'), (SELECT id FROM Conditions WHERE name = 'Brain Tumor'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Ophthalmologist'), (SELECT id FROM Conditions WHERE name = 'Cataracts'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Optometrist'), (SELECT id FROM Conditions WHERE name = 'Cataracts'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Physiatrist'), (SELECT id FROM Conditions WHERE name = 'Stroke'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Physical Therapist'), (SELECT id FROM Conditions WHERE name = 'Arthritis'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Physical Therapist'), (SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Podiatrist'), (SELECT id FROM Conditions WHERE name = 'Gout'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Psychiatrist'), (SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Psychiatrist'), (SELECT id FROM Conditions WHERE name = 'Depression'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Psychologist'), (SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Psychologist'), (SELECT id FROM Conditions WHERE name = 'Depression'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Pulmonologist'), (SELECT id FROM Conditions WHERE name = 'Asthma'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Pulmonologist'), (SELECT id FROM Conditions WHERE name = 'Lung Cancer'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Pulmonologist'), (SELECT id FROM Conditions WHERE name = 'Pneumonia'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Radiologist'), (SELECT id FROM Conditions WHERE name = 'Lung Cancer'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Radiologist'), (SELECT id FROM Conditions WHERE name = 'Stroke'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Rheumatologist'), (SELECT id FROM Conditions WHERE name = 'Arthritis'));

INSERT INTO Specifacilitiesalities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Urologist'), (SELECT id FROM Conditions WHERE name = 'Kidney Stones'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Urologist'), (SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'));

INSERT INTO Specialities_Conditions VALUES ((SELECT id FROM Specialties WHERE name = 'Vascular Surgeon'), (SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'));
