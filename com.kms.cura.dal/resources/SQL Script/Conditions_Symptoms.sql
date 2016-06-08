USE cura;

CREATE TABLE Conditions_Symptoms (
	condition_id INT NOT NULL,
    symptom_id INT NOT NULL,
    FOREIGN KEY (condition_id) REFERENCES Conditions (id),
    FOREIGN KEY (symptom_id) REFERENCES Symptoms (id)
    );
    
INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Allergies'), (SELECT id FROM Symptoms WHERE name = 'Congestion'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Allergies'), (SELECT id FROM Symptoms WHERE name = 'Itchiness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Allergies'), (SELECT id FROM Symptoms WHERE name = 'Rash'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Allergies'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Allergies'), (SELECT id FROM Symptoms WHERE name = 'Swelling'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Confusion'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Difficulty sleeping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Discoordination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Loss of interest'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Mood changes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Alzheimer\'s Disease'), (SELECT id FROM Symptoms WHERE name = 'Weight loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Cramping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Difficulty concentrating'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Difficulty sleeping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Dizziness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Pallor'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Palpitations'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Anemia'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Arthritis'), (SELECT id FROM Symptoms WHERE name = 'Joint Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Arthritis'), (SELECT id FROM Symptoms WHERE name = 'Stiffness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Arthritis'), (SELECT id FROM Symptoms WHERE name = 'Swelling'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Asthma'), (SELECT id FROM Symptoms WHERE name = 'Chest pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Asthma'), (SELECT id FROM Symptoms WHERE name = 'Coughing'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Asthma'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Brain Tumor'), (SELECT id FROM Symptoms WHERE name = 'Discoordination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Brain Tumor'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Brain Tumor'), (SELECT id FROM Symptoms WHERE name = 'Mood changes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Brain Tumor'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Brain Tumor'), (SELECT id FROM Symptoms WHERE name = 'Seizures'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Brain Tumor'), (SELECT id FROM Symptoms WHERE name = 'Weakness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Cataracts'), (SELECT id FROM Symptoms WHERE name = 'Blurry vision'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Cataracts'), (SELECT id FROM Symptoms WHERE name = 'Double vision'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Cataracts'), (SELECT id FROM Symptoms WHERE name = 'Vision changes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'), (SELECT id FROM Symptoms WHERE name = 'Chest pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'), (SELECT id FROM Symptoms WHERE name = 'Dizziness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'), (SELECT id FROM Symptoms WHERE name = 'Palpitations'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Coronary Artery Disease (CAD)'), (SELECT id FROM Symptoms WHERE name = 'Weakness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Appetite loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Cramping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Difficulty concentrating'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Difficulty sleeping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Excess Hunger/Thirst'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Loss of interest'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Depression'), (SELECT id FROM Symptoms WHERE name = 'Mood changes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Blurry vision'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Dryness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Excess Hunger/Thirst'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Frequent Urination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Itchiness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Diabetes'), (SELECT id FROM Symptoms WHERE name = 'Numbness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Ear Infection'), (SELECT id FROM Symptoms WHERE name = 'Earache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Ear Infection'), (SELECT id FROM Symptoms WHERE name = 'Ear drainage'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Ear Infection'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Eczema (Atopic Dermatitis)'), (SELECT id FROM Symptoms WHERE name = 'Dryness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Eczema (Atopic Dermatitis)'), (SELECT id FROM Symptoms WHERE name = 'Itchiness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Eczema (Atopic Dermatitis)'), (SELECT id FROM Symptoms WHERE name = 'Rash'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gout'), (SELECT id FROM Symptoms WHERE name = 'Itchiness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gout'), (SELECT id FROM Symptoms WHERE name = 'Joint Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gout'), (SELECT id FROM Symptoms WHERE name = 'Skin discoloration'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gout'), (SELECT id FROM Symptoms WHERE name = 'Stiffness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gout'), (SELECT id FROM Symptoms WHERE name = 'Swelling'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gum Disease (Periodontal Disease)'), (SELECT id FROM Symptoms WHERE name = 'Gum pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Gum Disease (Periodontal Disease)'), (SELECT id FROM Symptoms WHERE name = 'Gum bleeding'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Abdominal pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Appetite loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Joint Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Muscle Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Hepatitis'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Bruising/bleeding easily'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Diarrhea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Muscle Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Rash'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Skin discoloration'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Sore throat'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Swollen lymph nodes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'HIV/AIDS'), (SELECT id FROM Symptoms WHERE name = 'Weight loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'), (SELECT id FROM Symptoms WHERE name = 'Abdominal pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'), (SELECT id FROM Symptoms WHERE name = 'Appetite loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'), (SELECT id FROM Symptoms WHERE name = 'Bloating'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'), (SELECT id FROM Symptoms WHERE name = 'Cramping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'), (SELECT id FROM Symptoms WHERE name = 'Diarrhea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Irritable Bowel Syndrome'), (SELECT id FROM Symptoms WHERE name = 'Weight loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Abdominal pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Blood in urine'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Chills'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Cramping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Frequent Urination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Kidney Stones'), (SELECT id FROM Symptoms WHERE name = 'Painful urination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Abdominal pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Appetite loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Bruising/bleeding easily'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Cramping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Difficulty concentrating'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Difficulty sleeping'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Dizziness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Pallor'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Palpitations'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Swollen lymph nodes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Vision changes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Leukemia'), (SELECT id FROM Symptoms WHERE name = 'Weight loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Appetite loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Chest pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Coughing'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Muscle Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Sore throat'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Swelling'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Weakness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Lung Cancer'), (SELECT id FROM Symptoms WHERE name = 'Weight loss'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Confusion'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Muscle Pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Rash'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Seizures'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Stiffness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Meningitis'), (SELECT id FROM Symptoms WHERE name = 'Weakness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Chest pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Chills'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Coughing'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Diarrhea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Fatigue'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Palpitations'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Shortness of breath'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Pneumonia'), (SELECT id FROM Symptoms WHERE name = 'Weakness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Strep Throat'), (SELECT id FROM Symptoms WHERE name = 'Discoloration in mouth'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Strep Throat'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Strep Throat'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Strep Throat'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Strep Throat'), (SELECT id FROM Symptoms WHERE name = 'Sore throat'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Strep Throat'), (SELECT id FROM Symptoms WHERE name = 'Swollen lymph nodes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Stroke'), (SELECT id FROM Symptoms WHERE name = 'Confusion'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Stroke'), (SELECT id FROM Symptoms WHERE name = 'Discoordination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Stroke'), (SELECT id FROM Symptoms WHERE name = 'Double vision'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Stroke'), (SELECT id FROM Symptoms WHERE name = 'Headache'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Stroke'), (SELECT id FROM Symptoms WHERE name = 'Numbness'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Stroke'), (SELECT id FROM Symptoms WHERE name = 'Vision changes'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Abdominal pain'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Blood in urine'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Chills'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Fever'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Frequent Urination'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Nausea'));

INSERT INTO Conditions_Symptoms VALUES ((SELECT id FROM Conditions WHERE name = 'Urinary Tract Infection'), (SELECT id FROM Symptoms WHERE name = 'Painful urination'));


