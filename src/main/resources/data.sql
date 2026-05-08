-- ============================================================
-- DONNÉES DE TEST — Évaluation 6 Mai 2026
-- ============================================================

-- ============================================================
-- COLLECTIVITÉS (Tableau 1)
-- ============================================================
INSERT INTO collectivities (id, number, name, location, specialty, creation_date)
VALUES
    ('col-1', 1, 'Mpanorina',      'Ambatondrazaka', 'Riziculture',  NOW()),
    ('col-2', 2, 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture', NOW()),
    ('col-3', 3, 'Tantely mamy',   'Brickaville',    'Apiculture',   NOW());

-- ============================================================
-- MEMBRES (Tableaux 2, 3, 4)
-- ============================================================
INSERT INTO members (id, last_name, first_name, birth_date, gender, address, profession, phone, email)
VALUES
    -- Membres partagés col-1 / col-2
    ('C1-M1', 'Nom membre 1',  'Prénom membre 1',  '1980-02-01', 'MALE',   'Lot II V M Ambato.',   'Riziculteur', '0341234567', 'member.1@fed-agri.mg'),
    ('C1-M2', 'Nom membre 2',  'Prénom membre 2',  '1982-03-05', 'MALE',   'Lot II F Ambato.',     'Agriculteur', '0321234567', 'member.2@fed-agri.mg'),
    ('C1-M3', 'Nom membre 3',  'Prénom membre 3',  '1992-03-10', 'MALE',   'Lot II J Ambato.',     'Collecteur',  '0331234567', 'member.3@fed-agri.mg'),
    ('C1-M4', 'Nom membre 4',  'Prénom membre 4',  '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.',   'Distributeur','0381234567', 'member.4@fed-agri.mg'),
    ('C1-M5', 'Nom membre 5',  'Prénom membre 5',  '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',    'Riziculteur', '0373434567', 'member.5@fed-agri.mg'),
    ('C1-M6', 'Nom membre 6',  'Prénom membre 6',  '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',     'Riziculteur', '0372234567', 'member.6@fed-agri.mg'),
    ('C1-M7', 'Nom membre 7',  'Prénom membre 7',  '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',     'Riziculteur', '0374234567', 'member.7@fed-agri.mg'),
    ('C1-M8', 'Nom membre 8',  'Prénom membre 8',  '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',     'Riziculteur', '0370234567', 'member.8@fed-agri.mg'),
    -- Membres col-3
    ('C3-M1', 'Nom membre 9',  'Prénom membre 9',  '1988-01-02', 'MALE',   'Lot 33 J Antsirabe',   'Apiculteur',  '034034567',  'member.9@fed-agri.mg'),
    ('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'MALE',   'Lot 2 J Antsirabe',    'Agriculteur', '0338634567', 'member.10@fed-agri.mg'),
    ('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'MALE',   'Lot 8 KM Antsirabe',   'Collecteur',  '0338234567', 'member.11@fed-agri.mg'),
    ('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur','0382334567', 'member.12@fed-agri.mg'),
    ('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'MALE',   'Lot UV 80 Antsirabe',  'Apiculteur',  '0373365567', 'member.13@fed-agri.mg'),
    ('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe',   'Apiculteur',  '0378234567', 'member.14@fed-agri.mg'),
    ('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'MALE',   'Lot UV 7 Antsirabe',   'Apiculteur',  '0374914567', 'member.15@fed-agri.mg'),
    ('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'MALE',   'Lot UV 8 Antsirabe',   'Apiculteur',  '0370634567', 'member.16@fed-agri.mg');

-- ============================================================
-- NOUVEAUX MEMBRES JUNIORS (Tableaux 18, 19, 20)
-- ============================================================

-- col-1 : 4 juniors (Tableau 18)
INSERT INTO members (id, last_name, first_name, birth_date, gender, address, profession, phone, email)
VALUES
    ('C1-J1', 'Junior Nom 1',  'Junior Prénom 1',  '2000-01-01', 'MALE',   'Lot JR 1 Ambato.', 'Agriculteur', '0340000011', 'junior1.col1@fed-agri.mg'),
    ('C1-J2', 'Junior Nom 2',  'Junior Prénom 2',  '2000-02-01', 'FEMALE', 'Lot JR 2 Ambato.', 'Agriculteur', '0340000012', 'junior2.col1@fed-agri.mg'),
    ('C1-J3', 'Junior Nom 3',  'Junior Prénom 3',  '2000-03-01', 'MALE',   'Lot JR 3 Ambato.', 'Agriculteur', '0340000013', 'junior3.col1@fed-agri.mg'),
    ('C1-J4', 'Junior Nom 4',  'Junior Prénom 4',  '2000-04-01', 'FEMALE', 'Lot JR 4 Ambato.', 'Agriculteur', '0340000014', 'junior4.col1@fed-agri.mg');

-- col-2 : 3 juniors (Tableau 19)
INSERT INTO members (id, last_name, first_name, birth_date, gender, address, profession, phone, email)
VALUES
    ('C2-J1', 'Junior Nom 5',  'Junior Prénom 5',  '2001-01-01', 'MALE',   'Lot JR 5 Ambato.', 'Agriculteur', '0340000021', 'junior1.col2@fed-agri.mg'),
    ('C2-J2', 'Junior Nom 6',  'Junior Prénom 6',  '2001-02-01', 'FEMALE', 'Lot JR 6 Ambato.', 'Agriculteur', '0340000022', 'junior2.col2@fed-agri.mg'),
    ('C2-J3', 'Junior Nom 7',  'Junior Prénom 7',  '2001-03-01', 'MALE',   'Lot JR 7 Ambato.', 'Agriculteur', '0340000023', 'junior3.col2@fed-agri.mg');

-- col-3 : 6 juniors (Tableau 20)
INSERT INTO members (id, last_name, first_name, birth_date, gender, address, profession, phone, email)
VALUES
    ('C3-J1', 'Junior Nom 8',  'Junior Prénom 8',  '2002-01-01', 'MALE',   'Lot JR 8 Antsirabe',  'Apiculteur', '0340000031', 'junior1.col3@fed-agri.mg'),
    ('C3-J2', 'Junior Nom 9',  'Junior Prénom 9',  '2002-02-01', 'FEMALE', 'Lot JR 9 Antsirabe',  'Apiculteur', '0340000032', 'junior2.col3@fed-agri.mg'),
    ('C3-J3', 'Junior Nom 10', 'Junior Prénom 10', '2002-03-01', 'MALE',   'Lot JR 10 Antsirabe', 'Apiculteur', '0340000033', 'junior3.col3@fed-agri.mg'),
    ('C3-J4', 'Junior Nom 11', 'Junior Prénom 11', '2002-04-01', 'FEMALE', 'Lot JR 11 Antsirabe', 'Apiculteur', '0340000034', 'junior4.col3@fed-agri.mg'),
    ('C3-J5', 'Junior Nom 12', 'Junior Prénom 12', '2002-05-01', 'MALE',   'Lot JR 12 Antsirabe', 'Apiculteur', '0340000035', 'junior5.col3@fed-agri.mg'),
    ('C3-J6', 'Junior Nom 13', 'Junior Prénom 13', '2002-06-01', 'FEMALE', 'Lot JR 13 Antsirabe', 'Apiculteur', '0340000036', 'junior6.col3@fed-agri.mg');

-- ============================================================
-- MEMBERSHIPS — start_date = 01/01/2026 pour tous les anciens membres
-- ============================================================

-- Collectivité 1 (Tableau 2)
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 'col-1', 'PRESIDENT',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 'col-1', 'VICE_PRESIDENT', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 'col-1', 'SECRETARY',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 'col-1', 'TREASURER',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 'col-1', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 'col-1', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7', 'col-1', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 'col-1', 'SENIOR',         '2026-01-01');

-- Collectivité 2 (Tableau 3)
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 'col-2', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 'col-2', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 'col-2', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 'col-2', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 'col-2', 'PRESIDENT',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 'col-2', 'VICE_PRESIDENT', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7', 'col-2', 'SECRETARY',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 'col-2', 'TREASURER',      '2026-01-01');

-- Collectivité 3 (Tableau 4)
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C3-M1', 'col-3', 'PRESIDENT',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M2', 'col-3', 'VICE_PRESIDENT', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M3', 'col-3', 'SECRETARY',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M4', 'col-3', 'TREASURER',      '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M5', 'col-3', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M6', 'col-3', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M7', 'col-3', 'SENIOR',         '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M8', 'col-3', 'SENIOR',         '2026-01-01');

-- Nouveaux juniors col-1 (Tableau 18)
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-J1', 'col-1', 'JUNIOR', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C1-J2', 'col-1', 'JUNIOR', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C1-J3', 'col-1', 'JUNIOR', '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C1-J4', 'col-1', 'JUNIOR', '2026-06-01');

-- Nouveaux juniors col-2 (Tableau 19)
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C2-J1', 'col-2', 'JUNIOR', '2026-03-01'),
    (gen_random_uuid()::VARCHAR, 'C2-J2', 'col-2', 'JUNIOR', '2026-03-01'),
    (gen_random_uuid()::VARCHAR, 'C2-J3', 'col-2', 'JUNIOR', '2026-03-01');

-- Nouveaux juniors col-3 (Tableau 20)
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C3-J1', 'col-3', 'JUNIOR', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-J2', 'col-3', 'JUNIOR', '2026-02-01'),
    (gen_random_uuid()::VARCHAR, 'C3-J3', 'col-3', 'JUNIOR', '2026-02-01'),
    (gen_random_uuid()::VARCHAR, 'C3-J4', 'col-3', 'JUNIOR', '2026-03-01'),
    (gen_random_uuid()::VARCHAR, 'C3-J5', 'col-3', 'JUNIOR', '2026-03-01'),
    (gen_random_uuid()::VARCHAR, 'C3-J6', 'col-3', 'JUNIOR', '2026-03-01');

-- ============================================================
-- REFERALS (Tableaux 2, 3, 4)
-- ============================================================
INSERT INTO referals (id, member_id, referee_id) VALUES
                                                     -- col-1 (anciens)
                                                     (gen_random_uuid()::VARCHAR, 'C1-M3', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M3', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M4', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M4', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M5', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M5', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M6', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M6', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M7', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M7', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M8', 'C1-M6'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-M8', 'C1-M7'),
                                                     -- col-3 (anciens)
                                                     (gen_random_uuid()::VARCHAR, 'C3-M1', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M1', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M2', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M2', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M3', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M3', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M4', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M4', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M5', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M5', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M6', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M6', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M7', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M7', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M8', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-M8', 'C3-M2'),
                                                     -- Nouveaux juniors col-1 (Tableau 18)
                                                     (gen_random_uuid()::VARCHAR, 'C1-J1', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J1', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J2', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J2', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J3', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J3', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J4', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C1-J4', 'C1-M2'),
                                                     -- Nouveaux juniors col-2 (Tableau 19)
                                                     (gen_random_uuid()::VARCHAR, 'C2-J1', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C2-J1', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C2-J2', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C2-J2', 'C1-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C2-J3', 'C1-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C2-J3', 'C1-M2'),
                                                     -- Nouveaux juniors col-3 (Tableau 20)
                                                     (gen_random_uuid()::VARCHAR, 'C3-J1', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J1', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J2', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J2', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J3', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J3', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J4', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J4', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J5', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J5', 'C3-M2'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J6', 'C3-M1'),
                                                     (gen_random_uuid()::VARCHAR, 'C3-J6', 'C3-M2');

-- ============================================================
-- COMPTES FINANCIERS
-- ============================================================

-- Collectivité 1
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, mobile_banking_service, mobile_number)
VALUES
    ('C1-A-CASH',     'col-1', 'CASH',         0, NULL,        NULL,           NULL),
    ('C1-A-MOBILE-1', 'col-1', 'MOBILE_MONEY', 0, 'Mpanorina', 'ORANGE_MONEY', '0370489612');

-- Collectivité 2
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, mobile_banking_service, mobile_number)
VALUES
    ('C2-A-CASH',     'col-2', 'CASH',         0, NULL,              NULL,           NULL),
    ('C2-A-MOBILE-1', 'col-2', 'MOBILE_MONEY', 0, 'Dobo voalohany', 'ORANGE_MONEY', '0320489612');

-- Collectivité 3 — caisse de base
INSERT INTO accounts (id, collectivity_id, type, balance)
VALUES ('C3-A-CASH', 'col-3', 'CASH', 0);

-- Collectivité 3 — comptes bancaires
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, bank_name, bank_account_number)
VALUES
    ('C3-A-BANK-1', 'col-3', 'BANK', 0, 'Koto',  'BMOI', '0000400001123456789012'),
    ('C3-A-BANK-2', 'col-3', 'BANK', 0, 'Naivo', 'BRED', '0000800003456789012358');

-- Collectivité 3 — compte mobile money
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, mobile_banking_service, mobile_number)
VALUES ('C3-A-MOBILE-1', 'col-3', 'MOBILE_MONEY', 0, 'Kolo', 'MVOLA', '0341889612');

-- ============================================================
-- COTISATIONS (Tableaux 12, 13, 14)
-- ============================================================

-- col-1 (Tableau 12)
INSERT INTO fees (id, collectivity_id, eligible_from, amount, label, frequency, status)
VALUES
    ('cot-1', 'col-1', '2026-01-01', 200000.00, 'Cotisation annuelle', 'ANNUALLY',   'ACTIVE'),
    ('cot-2', 'col-1', '2026-04-30',  20000.00, 'Famangiana',          'PUNCTUALLY', 'ACTIVE');

-- col-2 (Tableau 13)
INSERT INTO fees (id, collectivity_id, eligible_from, amount, label, frequency, status)
VALUES
    ('cot-3', 'col-2', '2026-01-01', 200000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE'),
    ('cot-4', 'col-2', '2025-01-01', 100000.00, 'Cotisation 2025',     'ANNUALLY', 'INACTIVE');

-- col-3 (Tableau 14)
INSERT INTO fees (id, collectivity_id, eligible_from, amount, label, frequency, status)
VALUES
    ('cot-5', 'col-3', '2026-04-01', 25000.00, 'Cotisation mensuelle', 'MONTHLY', 'ACTIVE');

-- ============================================================
-- PAIEMENTS (Tableaux 15, 16, 17)
-- ============================================================

-- col-1 (Tableau 15)
INSERT INTO payments (id, member_id, amount, membership_fee_id, credited_account_id, payment_method, creation_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 200000, 'cot-1', 'C1-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 200000, 'cot-1', 'C1-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 200000, 'cot-1', 'C1-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 200000, 'cot-1', 'C1-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 150000, 'cot-1', 'C1-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 100000, 'cot-1', 'C1-A-CASH',     'CASH',           '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7',  60000, 'cot-1', 'C1-A-CASH',     'CASH',           '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8',  90000, 'cot-1', 'C1-A-CASH',     'CASH',           '2026-05-01');

-- Mise à jour des soldes col-1
-- C1-A-CASH     : 200000+200000+100000+60000+90000 = 650000
-- C1-A-MOBILE-1 : 200000+200000+150000             = 550000
UPDATE accounts SET balance = 650000 WHERE id = 'C1-A-CASH';
UPDATE accounts SET balance = 550000 WHERE id = 'C1-A-MOBILE-1';

-- col-2 (Tableau 16)
INSERT INTO payments (id, member_id, amount, membership_fee_id, credited_account_id, payment_method, creation_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 120000, 'cot-3', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 180000, 'cot-3', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 200000, 'cot-3', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 200000, 'cot-3', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 200000, 'cot-3', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 200000, 'cot-3', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7',  80000, 'cot-3', 'C2-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 120000, 'cot-3', 'C2-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01');

-- Mise à jour des soldes col-2
-- C2-A-CASH     : 120000+180000+200000+200000+200000+200000 = 1100000
-- C2-A-MOBILE-1 : 80000+120000                              =  200000
UPDATE accounts SET balance = 1100000 WHERE id = 'C2-A-CASH';
UPDATE accounts SET balance =  200000 WHERE id = 'C2-A-MOBILE-1';

-- col-3 (Tableau 17)
INSERT INTO payments (id, member_id, amount, membership_fee_id, credited_account_id, payment_method, creation_date)
VALUES
    -- Avril
    (gen_random_uuid()::VARCHAR, 'C3-M1', 25000, 'cot-5', 'C3-A-BANK-1',   'BANK_TRANSFER', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M2', 25000, 'cot-5', 'C3-A-BANK-1',   'BANK_TRANSFER', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M3', 25000, 'cot-5', 'C3-A-BANK-1',   'BANK_TRANSFER', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M4', 25000, 'cot-5', 'C3-A-BANK-1',   'BANK_TRANSFER', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M5', 25000, 'cot-5', 'C3-A-BANK-2',   'BANK_TRANSFER', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M6', 25000, 'cot-5', 'C3-A-BANK-2',   'BANK_TRANSFER', '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M7', 25000, 'cot-5', 'C3-A-CASH',     'CASH',          '2026-04-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M8', 25000, 'cot-5', 'C3-A-CASH',     'CASH',          '2026-04-01'),
    -- Mai
    (gen_random_uuid()::VARCHAR, 'C3-M1', 25000, 'cot-5', 'C3-A-BANK-1',   'BANK_TRANSFER', '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M2', 25000, 'cot-5', 'C3-A-BANK-1',   'BANK_TRANSFER', '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M3', 15000, 'cot-5', 'C3-A-MOBILE-1', 'MOBILE_BANKING','2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M4', 15000, 'cot-5', 'C3-A-MOBILE-1', 'MOBILE_BANKING','2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M5', 20000, 'cot-5', 'C3-A-BANK-2',   'BANK_TRANSFER', '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M6', 25000, 'cot-5', 'C3-A-BANK-2',   'BANK_TRANSFER', '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M7',  5000, 'cot-5', 'C3-A-CASH',     'CASH',          '2026-05-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M8',  5000, 'cot-5', 'C3-A-CASH',     'CASH',          '2026-05-01');

-- Mise à jour des soldes col-3
-- C3-A-CASH     : 25000+25000+5000+5000         =  60000
-- C3-A-BANK-1   : 25000*4(avril) + 25000*2(mai) = 150000
-- C3-A-BANK-2   : 25000+25000+20000+25000        =  95000
-- C3-A-MOBILE-1 : 15000+15000                    =  30000
UPDATE accounts SET balance =  60000 WHERE id = 'C3-A-CASH';
UPDATE accounts SET balance = 150000 WHERE id = 'C3-A-BANK-1';
UPDATE accounts SET balance =  95000 WHERE id = 'C3-A-BANK-2';
UPDATE accounts SET balance =  30000 WHERE id = 'C3-A-MOBILE-1';

-- ============================================================
-- ACTIVITÉS
-- ============================================================

-- ============================================================
-- COLLECTIVITÉ 1 (Tableau 21)
-- act-1  : template récurrent (1er samedi), SANS executive_date → NE PAS y insérer de présences
-- act-1a : instance mars 07/03/2026 avec executive_date → présences du Tableau 24
-- act-1b : instance avril 04/04/2026 avec executive_date → présences du Tableau 25
-- act-2  : template récurrent (2e dimanche), pour JUNIOR uniquement
-- ============================================================

-- Template récurrent AG1 col-1 (pour que le test get_col1_activities le trouve avec recurrenceRule)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-1', 'col-1', 'AG1', 'MEETING', NULL, 1, 'SA');

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-1', 'JUNIOR'),
    ('act-1', 'SENIOR'),
    ('act-1', 'SECRETARY'),
    ('act-1', 'TREASURER'),
    ('act-1', 'VICE_PRESIDENT'),
    ('act-1', 'PRESIDENT');

-- Template récurrent Formation de base col-1
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-2', 'col-1', 'Formation de base', 'TRAINING', NULL, 2, 'SU');

INSERT INTO activity_required_members (activity_id, required_member)
VALUES ('act-2', 'JUNIOR');

-- Instance datée AG1 col-1 — Mars 2026 (Tableau 24 : 07/03/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-1a', 'col-1', 'AG1', 'MEETING', '2026-03-07', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-1a', 'JUNIOR'),
    ('act-1a', 'SENIOR'),
    ('act-1a', 'SECRETARY'),
    ('act-1a', 'TREASURER'),
    ('act-1a', 'VICE_PRESIDENT'),
    ('act-1a', 'PRESIDENT');

-- Instance datée AG1 col-1 — Avril 2026 (Tableau 25 : 04/04/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-1b', 'col-1', 'AG1', 'MEETING', '2026-04-04', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-1b', 'JUNIOR'),
    ('act-1b', 'SENIOR'),
    ('act-1b', 'SECRETARY'),
    ('act-1b', 'TREASURER'),
    ('act-1b', 'VICE_PRESIDENT'),
    ('act-1b', 'PRESIDENT');

-- ============================================================
-- COLLECTIVITÉ 2 (Tableau 22)
-- act-3  : template récurrent (1er dimanche), SANS executive_date
-- act-3a : instance mars 08/03/2026 → présences du Tableau 26
-- act-3b : instance avril 05/04/2026 → présences du Tableau 27
-- act-4  : template récurrent (3e dimanche), pour JUNIOR
-- act-5  : ponctuel 30/04/2026, pour SENIOR → présences du Tableau 28
-- ============================================================

-- Template récurrent AG2 col-2
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-3', 'col-2', 'AG2', 'MEETING', NULL, 1, 'SU');

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-3', 'JUNIOR'),
    ('act-3', 'SENIOR'),
    ('act-3', 'SECRETARY'),
    ('act-3', 'TREASURER'),
    ('act-3', 'VICE_PRESIDENT'),
    ('act-3', 'PRESIDENT');

-- Template récurrent Formation de base col-2
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-4', 'col-2', 'Formation de base', 'TRAINING', NULL, 3, 'SU');

INSERT INTO activity_required_members (activity_id, required_member)
VALUES ('act-4', 'JUNIOR');

-- Ponctuel Perfectionnement col-2 (30/04/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-5', 'col-2', 'Perfectionnement', 'OTHER', '2026-04-30', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES ('act-5', 'SENIOR');

-- Instance datée AG2 col-2 — Mars 2026 (Tableau 26 : 08/03/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-3a', 'col-2', 'AG2', 'MEETING', '2026-03-08', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-3a', 'JUNIOR'),
    ('act-3a', 'SENIOR'),
    ('act-3a', 'SECRETARY'),
    ('act-3a', 'TREASURER'),
    ('act-3a', 'VICE_PRESIDENT'),
    ('act-3a', 'PRESIDENT');

-- Instance datée AG2 col-2 — Avril 2026 (Tableau 27 : 05/04/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-3b', 'col-2', 'AG2', 'MEETING', '2026-04-05', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-3b', 'JUNIOR'),
    ('act-3b', 'SENIOR'),
    ('act-3b', 'SECRETARY'),
    ('act-3b', 'TREASURER'),
    ('act-3b', 'VICE_PRESIDENT'),
    ('act-3b', 'PRESIDENT');

-- ============================================================
-- COLLECTIVITÉ 3 (Tableau 23)
-- act-6  : template récurrent (1er vendredi), SANS executive_date
-- act-6a : instance mars 06/03/2026 → présences du Tableau 29
-- act-6b : instance avril 03/04/2026 → présences du Tableau 30
-- act-7  : template récurrent (4e mercredi), pour JUNIOR
-- ============================================================

-- Template récurrent AG3 col-3
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-6', 'col-3', 'AG3', 'MEETING', NULL, 1, 'FR');

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-6', 'JUNIOR'),
    ('act-6', 'SENIOR'),
    ('act-6', 'SECRETARY'),
    ('act-6', 'TREASURER'),
    ('act-6', 'VICE_PRESIDENT'),
    ('act-6', 'PRESIDENT');

-- Template récurrent Formation de base col-3
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-7', 'col-3', 'Formation de base', 'TRAINING', NULL, 4, 'WE');

INSERT INTO activity_required_members (activity_id, required_member)
VALUES ('act-7', 'JUNIOR');

-- Instance datée AG3 col-3 — Mars 2026 (Tableau 29 : 06/03/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-6a', 'col-3', 'AG3', 'MEETING', '2026-03-06', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-6a', 'JUNIOR'),
    ('act-6a', 'SENIOR'),
    ('act-6a', 'SECRETARY'),
    ('act-6a', 'TREASURER'),
    ('act-6a', 'VICE_PRESIDENT'),
    ('act-6a', 'PRESIDENT');

-- Instance datée AG3 col-3 — Avril 2026 (Tableau 30 : 03/04/2026)
INSERT INTO activities (id, collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
VALUES ('act-6b', 'col-3', 'AG3', 'MEETING', '2026-04-03', NULL, NULL);

INSERT INTO activity_required_members (activity_id, required_member)
VALUES
    ('act-6b', 'JUNIOR'),
    ('act-6b', 'SENIOR'),
    ('act-6b', 'SECRETARY'),
    ('act-6b', 'TREASURER'),
    ('act-6b', 'VICE_PRESIDENT'),
    ('act-6b', 'PRESIDENT');

-- ============================================================
-- PRÉSENCES
-- ============================================================

-- ============================================================
-- Tableau 24 : AG1 col-1 — Mars 2026 (07/03/2026) → act-1a
-- M1-M6 Présent, M7-M8 Absent
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M1', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M2', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M3', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M4', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M5', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M6', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M7', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-1a', 'C1-M8', 'MISSING');

-- ============================================================
-- Tableau 25 : AG1 col-1 — Avril 2026 (04/04/2026) → act-1b
-- M1/M2/M5-M8 Présent, M3/M4 Absent
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M1', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M2', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M3', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M4', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M5', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M6', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M7', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-1b', 'C1-M8', 'ATTENDED');

-- ============================================================
-- Tableau 26 : AG2 col-2 — Mars 2026 (08/03/2026) → act-3a
-- M1/M2/M5-M8 Présent, M3/M4 Absent
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M1', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M2', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M3', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M4', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M5', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M6', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M7', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3a', 'C1-M8', 'ATTENDED');

-- ============================================================
-- Tableau 27 : AG2 col-2 — Avril 2026 (05/04/2026) → act-3b
-- M1/M2/M4-M7 Présent, M3/M8 Absent
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M1', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M2', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M3', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M4', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M5', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M6', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M7', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-3b', 'C1-M8', 'MISSING');

-- ============================================================
-- Tableau 28 : Perfectionnement col-2 — 30/04/2026 → act-5
-- M1/M2/M3 Présent, M4 Absent, M5-M8 Non-défini
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M1', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M2', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M3', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M4', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M5', 'UNDEFINED'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M6', 'UNDEFINED'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M7', 'UNDEFINED'),
    (gen_random_uuid()::VARCHAR, 'act-5', 'C1-M8', 'UNDEFINED');

-- ============================================================
-- Tableau 29 : AG3 col-3 — Mars 2026 (06/03/2026) → act-6a
-- M1-M6 Présent, M7-M8 Absent
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M1', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M2', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M3', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M4', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M5', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M6', 'ATTENDED'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M7', 'MISSING'),
    (gen_random_uuid()::VARCHAR, 'act-6a', 'C3-M8', 'MISSING');

-- ============================================================
-- Tableau 30 : AG3 col-3 — Avril 2026 (03/04/2026) → act-6b
-- M1/M2/M5/M6/M8 Présent, M3/M4/M7 Absent
-- C1-M1 : membre extérieur (is_outsider = TRUE), non comptabilisé dans assiduité
-- ============================================================
INSERT INTO activity_attendances (id, activity_id, member_id, status, is_outsider)
VALUES
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M1', 'ATTENDED', FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M2', 'ATTENDED', FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M3', 'MISSING',  FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M4', 'MISSING',  FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M5', 'ATTENDED', FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M6', 'ATTENDED', FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M7', 'MISSING',  FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C3-M8', 'ATTENDED', FALSE),
    (gen_random_uuid()::VARCHAR, 'act-6b', 'C1-M1', 'ATTENDED', TRUE);