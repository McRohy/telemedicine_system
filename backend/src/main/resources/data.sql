INSERT INTO personal_data (email, first_name, last_name, role, password)
VALUES ('admin@tlmd.sk', 'Matej', 'Adminovic', 'ADMIN', '$2a$10$a2FI7iaW4TA5SxH7se0SvOGGWAZ7VQOBU75txlnx9nquCZpCdfivm') ON CONFLICT (email) DO NOTHING;

INSERT INTO personal_data (email, first_name, last_name, role, password)
VALUES ('doctor@tlmd.sk', 'Lubos', 'Roh', 'DOCTOR', '$2a$10$7kXpv/gTkNcai/FnM7v9kOzgr7NvEwUKG4wF0xPYFTyKHG0/LgIH2') ON CONFLICT (email) DO NOTHING;

INSERT INTO doctor(pan_number, email, specialization)
VALUES ('2354678909876543', 'doctor@tlmd.sk', 'CARDIOLOGIST') ON CONFLICT (pan_number) DO NOTHING;

INSERT INTO personal_data (email, first_name, last_name, role, password)
VALUES ('patient@firma.sk', 'Emma', 'Roh', 'PATIENT', '$2a$10$w4p7hvP38xp0lZ8.FelVK.z88co4GAxw2HNU7mNvXOWgdCh8G8GzO') ON CONFLICT (email) DO NOTHING;

INSERT INTO patient(personal_number, email, gender, care_provider)
VALUES ('0204234567', 'patient@firma.sk', 'FEMALE', '2354678909876543') ON CONFLICT (personal_number) DO NOTHING;

INSERT INTO type_of_measurement(type_name, units, min_value, max_value)
VALUES ('Teplota', 'C', 34, 37) ON CONFLICT (type_name) DO NOTHING;

INSERT INTO type_of_measurement(type_name, units, min_value, max_value)
VALUES ('Hmotnost', 'Kg', 1, 150) ON CONFLICT (type_name) DO NOTHING;