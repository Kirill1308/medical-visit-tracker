INSERT INTO doctors (first_name, last_name, timezone)
VALUES ('John', 'Smith', 'America/New_York'),
       ('Mary', 'Johnson', 'America/Chicago'),
       ('David', 'Lee', 'America/Los_Angeles');

INSERT INTO patients (first_name, last_name)
VALUES ('Alice', 'Brown'),
       ('Bob', 'White'),
       ('Carol', 'Davis'),
       ('Dan', 'Miller'),
       ('Eve', 'Wilson');

# -- Current date/time to use as a base for visits
# SET @now = NOW();
#
# -- Doctor 1 (John Smith) visits
# INSERT INTO visits (start_date_time, end_date_time, doctor_id, patient_id)
# VALUES (DATE_ADD(@now, INTERVAL 1 DAY), DATE_ADD(@now, INTERVAL 1 DAY) + INTERVAL 1 HOUR, 1, 1),
#        (DATE_ADD(@now, INTERVAL 2 DAY), DATE_ADD(@now, INTERVAL 2 DAY) + INTERVAL 1 HOUR, 1, 2),
#        (DATE_ADD(@now, INTERVAL 3 DAY), DATE_ADD(@now, INTERVAL 3 DAY) + INTERVAL 1 HOUR, 1, 3);
#
# -- Doctor 2 (Mary Johnson) visits
# INSERT INTO visits (start_date_time, end_date_time, doctor_id, patient_id)
# VALUES (DATE_ADD(@now, INTERVAL 1 DAY), DATE_ADD(@now, INTERVAL 1 DAY) + INTERVAL 1 HOUR, 2, 2),
#        (DATE_ADD(@now, INTERVAL 2 DAY), DATE_ADD(@now, INTERVAL 2 DAY) + INTERVAL 1 HOUR, 2, 3),
#        (DATE_ADD(@now, INTERVAL 3 DAY), DATE_ADD(@now, INTERVAL 3 DAY) + INTERVAL 1 HOUR, 2, 4);
#
# -- Doctor 3 (David Lee) visits
# INSERT INTO visits (start_date_time, end_date_time, doctor_id, patient_id)
# VALUES (DATE_ADD(@now, INTERVAL 1 DAY), DATE_ADD(@now, INTERVAL 1 DAY) + INTERVAL 1 HOUR, 3, 3),
#        (DATE_ADD(@now, INTERVAL 2 DAY), DATE_ADD(@now, INTERVAL 2 DAY) + INTERVAL 1 HOUR, 3, 4),
#        (DATE_ADD(@now, INTERVAL 3 DAY), DATE_ADD(@now, INTERVAL 3 DAY) + INTERVAL 1 HOUR, 3, 5);
#
# -- Add some historical visits to test the "latest visits" feature
# INSERT INTO visits (start_date_time, end_date_time, doctor_id, patient_id)
# VALUES
# -- Older visit for Alice with Dr. Smith
# (DATE_SUB(@now, INTERVAL 10 DAY), DATE_SUB(@now, INTERVAL 10 DAY) + INTERVAL 1 HOUR, 1, 1),
# -- Older visit for Bob with Dr. Johnson
# (DATE_SUB(@now, INTERVAL 10 DAY), DATE_SUB(@now, INTERVAL 10 DAY) + INTERVAL 1 HOUR, 2, 2),
# -- Older visit for Carol with Dr. Lee
# (DATE_SUB(@now, INTERVAL 10 DAY), DATE_SUB(@now, INTERVAL 10 DAY) + INTERVAL 1 HOUR, 3, 3);
