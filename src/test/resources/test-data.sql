INSERT INTO doctors (id, first_name, last_name, timezone)
VALUES (1, 'John', 'Smith', 'America/New_York'),
       (2, 'Mary', 'Johnson', 'America/Chicago'),
       (3, 'David', 'Lee', 'America/Los_Angeles');

INSERT INTO patients (id, first_name, last_name)
VALUES (1, 'Alice', 'Brown'),
       (2, 'Bob', 'White'),
       (3, 'Carol', 'Davis'),
       (4, 'Dan', 'Miller');

-- Insert visits for Alice (patient_id = 1)
-- Latest visit with John (doctor_id = 1)
INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time)
VALUES (1, 1, '2025-01-20 10:00:00', '2025-01-20 11:00:00');

-- Middle visit with Mary (doctor_id = 2)
INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time)
VALUES (1, 2, '2025-01-10 10:00:00', '2025-01-10 11:00:00');

-- Oldest visit with David (doctor_id = 3)
INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time)
VALUES (1, 3, '2025-01-01 10:00:00', '2025-01-01 11:00:00');

-- Insert visits for Bob (patient_id = 2)
-- Latest visit with Mary (doctor_id = 2)
INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time)
VALUES (2, 2, '2025-01-25 10:00:00', '2025-01-25 11:00:00');

-- Older visit with Mary (doctor_id = 2)
INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time)
VALUES (2, 2, '2025-01-15 10:00:00', '2025-01-15 11:00:00');

-- Insert visit for Carol (patient_id = 3)
INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time)
VALUES (3, 3, '2025-01-23 10:00:00', '2025-01-23 11:00:00');

-- Dan (patient_id = 4) has no visits
