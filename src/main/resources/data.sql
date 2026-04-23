-- ============================================================
-- DONNÉES DE TEST — TD Final 23 Avril 2026
-- ============================================================

-- ============================================================
-- COLLECTIVITÉS (Tableau 1)
-- ============================================================

INSERT INTO collectivities (id, number, name, location, specialty, creation_date)
VALUES
    ('col-1', '1', 'Mpanorina',       'Ambatondrazaka', 'Riziculture', NOW()),
    ('col-2', '2', 'Dobo voalohany',  'Ambatondrazaka', 'Pisciculture', NOW()),
    ('col-3', '3', 'Tantely mamy',    'Brickaville',    'Apiculture', NOW());

-- ============================================================
-- MEMBRES
-- Notes :
--   - Les membres C1-M1 à C1-M8 sont PARTAGÉS entre col-1 et col-2
--     (mêmes personnes, IDs identiques)
--   - C3-M1 à C3-M8 sont des membres distincts (membres 9 à 16)
-- ============================================================

INSERT INTO members (id, last_name, first_name, birth_date, gender, address, profession, phone, email)
VALUES
    -- Membres partagés col-1 / col-2
    ('C1-M1', 'Nom membre 1',  'Prénom membre 1',  '1980-02-01', 'MALE',   'Lot II V M Ambato.',  'Riziculteur', '0341234567', 'member.1@fed-agri.mg'),
    ('C1-M2', 'Nom membre 2',  'Prénom membre 2',  '1982-03-05', 'MALE',   'Lot II F Ambato.',    'Agriculteur', '0321234567', 'member.2@fed-agri.mg'),
    ('C1-M3', 'Nom membre 3',  'Prénom membre 3',  '1992-03-10', 'MALE',   'Lot II J Ambato.',    'Collecteur',  '0331234567', 'member.3@fed-agri.mg'),
    ('C1-M4', 'Nom membre 4',  'Prénom membre 4',  '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.',  'Distributeur','0381234567', 'member.4@fed-agri.mg'),
    ('C1-M5', 'Nom membre 5',  'Prénom membre 5',  '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',   'Riziculteur', '0373434567', 'member.5@fed-agri.mg'),
    ('C1-M6', 'Nom membre 6',  'Prénom membre 6',  '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',    'Riziculteur', '0372234567', 'member.6@fed-agri.mg'),
    ('C1-M7', 'Nom membre 7',  'Prénom membre 7',  '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',    'Riziculteur', '0374234567', 'member.7@fed-agri.mg'),
    ('C1-M8', 'Nom membre 8',  'Prénom membre 8',  '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',    'Riziculteur', '0370234567', 'member.8@fed-agri.mg'),
    -- Membres col-3
    ('C3-M1', 'Nom membre 9',  'Prénom membre 9',  '1988-01-02', 'MALE',   'Lot 33 J Antsirabe',  'Apiculteur',  '034034567',  'member.9@fed-agri.mg'),
    ('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'MALE',   'Lot 2 J Antsirabe',   'Agriculteur', '0338634567', 'member.10@fed-agri.mg'),
    ('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'MALE',   'Lot 8 KM Antsirabe',  'Collecteur',  '0338234567', 'member.11@fed-agri.mg'),
    ('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe','Distributeur','0382334567', 'member.12@fed-agri.mg'),
    ('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'MALE',   'Lot UV 80 Antsirabe.','Apiculteur',  '0373365567', 'member.13@fed-agri.mg'),
    ('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe.', 'Apiculteur',  '0378234567', 'member.14@fed-agri.mg'),
    ('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'MALE',   'Lot UV 7 Antsirabe',  'Apiculteur',  '0374914567', 'member.15@fed-agri.mg'),
    ('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'MALE',   'Lot UV 8 Antsirabe',  'Apiculteur',  '0370634567', 'member.16@fed-agri.mg');

-- ============================================================
-- MEMBERSHIPS — Collectivité 1 (Tableau 2)
-- start_date reculée pour satisfaire la condition d'ancienneté 6 mois
-- ============================================================

INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 'col-1', 'PRESIDENT',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 'col-1', 'VICE_PRESIDENT', '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 'col-1', 'SECRETARY',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 'col-1', 'TREASURER',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 'col-1', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 'col-1', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7', 'col-1', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 'col-1', 'SENIOR',         '2024-01-01');

-- ============================================================
-- MEMBERSHIPS — Collectivité 2 (Tableau 3)
-- Les mêmes membres (C1-M1..M8) mais avec des rôles différents
-- UNIQUE (member_id, collectivity_id) => pas de conflit car col-2 != col-1
-- ============================================================

INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 'col-2', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 'col-2', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 'col-2', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 'col-2', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 'col-2', 'PRESIDENT',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 'col-2', 'VICE_PRESIDENT', '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7', 'col-2', 'SECRETARY',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 'col-2', 'TREASURER',      '2024-01-01');

-- ============================================================
-- MEMBERSHIPS — Collectivité 3 (Tableau 4)
-- ============================================================

INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C3-M1', 'col-3', 'PRESIDENT',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M2', 'col-3', 'VICE_PRESIDENT', '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M3', 'col-3', 'SECRETARY',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M4', 'col-3', 'TREASURER',      '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M5', 'col-3', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M6', 'col-3', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M7', 'col-3', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid()::VARCHAR, 'C3-M8', 'col-3', 'SENIOR',         '2024-01-01');

-- ============================================================
-- REFERALS (Tableaux 2, 3, 4)
-- ============================================================

-- col-1 referals
INSERT INTO referals (id, member_id, referee_id) VALUES
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
                                                     (gen_random_uuid()::VARCHAR, 'C1-M8', 'C1-M7');

-- col-3 referals
INSERT INTO referals (id, member_id, referee_id) VALUES
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
                                                     (gen_random_uuid()::VARCHAR, 'C3-M8', 'C3-M2');

-- ============================================================
-- COTISATIONS (Tableaux 5, 6, 7)
-- ============================================================

INSERT INTO fees (id, eligible_from, amount, label, frequency, status)
VALUES
    ('cot-1', '2026-01-01', 100000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE'),
    ('cot-2', '2026-01-01', 100000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE'),
    ('cot-3', '2026-01-01',  50000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE');

INSERT INTO collectivityFee (id, collectivity_id, fee_id)
VALUES
    (gen_random_uuid()::VARCHAR, 'col-1', 'cot-1'),
    (gen_random_uuid()::VARCHAR, 'col-2', 'cot-2'),
    (gen_random_uuid()::VARCHAR, 'col-3', 'cot-3');

-- ============================================================
-- COMPTES (page 16 du PDF)
-- ============================================================

-- Collectivité 1
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, mobile_banking_service, mobile_number)
VALUES
    ('C1-A-CASH',     'col-1', 'CASH',        0.00, NULL,          NULL,           NULL),
    ('C1-A-MOBILE-1', 'col-1', 'MOBILE_MONEY', 0.00, 'Mpanorina',  'ORANGE_MONEY', '0370489612');

-- Collectivité 2
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, mobile_banking_service, mobile_number)
VALUES
    ('C2-A-CASH',     'col-2', 'CASH',         0.00, NULL,              NULL,           NULL),
    ('C2-A-MOBILE-1', 'col-2', 'MOBILE_MONEY', 0.00, 'Dobo voalohany', 'ORANGE_MONEY', '0320489612');

-- Collectivité 3
INSERT INTO accounts (id, collectivity_id, type, balance)
VALUES
    ('C3-A-CASH', 'col-3', 'CASH', 0.00);

-- ============================================================
-- PAIEMENTS — Collectivité 1 (Tableau 8)
-- ============================================================

INSERT INTO payments (id, amount, membership_fee_id, credited_account_id, payment_method, creation_date)
VALUES
    ('PAY-C1-M1', 100000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M2', 100000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M3', 100000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M4', 100000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M5', 100000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M6', 100000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M7',  60000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('PAY-C1-M8',  90000.00, 'cot-1', 'C1-A-CASH', 'CASH', '2026-01-01');

-- Mise à jour du solde du compte caisse col-1
UPDATE accounts SET balance = 760000.00 WHERE id = 'C1-A-CASH';

-- ============================================================
-- TRANSACTIONS — Collectivité 1 (Tableau 9)
-- ============================================================

INSERT INTO transactions (id, member_id, payment_id, creation_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 'PAY-C1-M1', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 'PAY-C1-M2', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 'PAY-C1-M3', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 'PAY-C1-M4', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 'PAY-C1-M5', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 'PAY-C1-M6', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7', 'PAY-C1-M7', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 'PAY-C1-M8', '2026-01-01');

-- ============================================================
-- PAIEMENTS — Collectivité 2 (Tableau 10)
-- ============================================================

INSERT INTO payments (id, amount, membership_fee_id, credited_account_id, payment_method, creation_date)
VALUES
    ('PAY-C2-M1',  60000.00, 'cot-2', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    ('PAY-C2-M2',  90000.00, 'cot-2', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    ('PAY-C2-M3', 100000.00, 'cot-2', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    ('PAY-C2-M4', 100000.00, 'cot-2', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    ('PAY-C2-M5', 100000.00, 'cot-2', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    ('PAY-C2-M6', 100000.00, 'cot-2', 'C2-A-CASH',     'CASH',           '2026-01-01'),
    ('PAY-C2-M7',  40000.00, 'cot-2', 'C2-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01'),
    ('PAY-C2-M8',  60000.00, 'cot-2', 'C2-A-MOBILE-1', 'MOBILE_BANKING', '2026-01-01');

-- Mise à jour des soldes des comptes col-2
UPDATE accounts SET balance = 550000.00 WHERE id = 'C2-A-CASH';
UPDATE accounts SET balance = 100000.00 WHERE id = 'C2-A-MOBILE-1';

-- ============================================================
-- TRANSACTIONS — Collectivité 2 (Tableau 11)
-- ============================================================

INSERT INTO transactions (id, member_id, payment_id, creation_date)
VALUES
    (gen_random_uuid()::VARCHAR, 'C1-M1', 'PAY-C2-M1', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M2', 'PAY-C2-M2', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M3', 'PAY-C2-M3', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M4', 'PAY-C2-M4', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M5', 'PAY-C2-M5', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M6', 'PAY-C2-M6', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M7', 'PAY-C2-M7', '2026-01-01'),
    (gen_random_uuid()::VARCHAR, 'C1-M8', 'PAY-C2-M8', '2026-01-01');

-- Collectivité 3 : aucun paiement ni transaction (liste vide)