-- ============================================================
-- MEMBRES DE BASE (déjà dans la fédération depuis > 6 mois)
-- pour pouvoir créer une collectivité et parrainer
-- ============================================================
-- 1. Corriger la contrainte
-- 2. Relancer les inserts
INSERT INTO collectivities (id, "location", specialty, creation_date)
VALUES ('b2000000-0000-0000-0000-000000000001', 'Antananarivo', 'Riziculture', '2024-01-01');

INSERT INTO members (id, last_name, first_name, birth_date, gender, address, profession, phone, email)
VALUES
    ('a1000000-0000-0000-0000-000000000001', 'RAKOTO', 'Jean', '1990-01-15', 'MALE', 'Antananarivo', 'Agriculteur', '0341234567', 'jean.rakoto@email.com'),
    ('a1000000-0000-0000-0000-000000000002', 'RABE', 'Marie', '1988-05-20', 'FEMALE', 'Antananarivo', 'Éleveuse', '0342345678', 'marie.rabe@email.com'),
    ('a1000000-0000-0000-0000-000000000003', 'RANDRIA', 'Paul', '1992-03-10', 'MALE', 'Antananarivo', 'Riziculteur', '0343456789', 'paul.randria@email.com'),
    ('a1000000-0000-0000-0000-000000000004', 'RASOA', 'Hanta', '1995-07-25', 'FEMALE', 'Antananarivo', 'Maraîchère', '0344567890', 'hanta.rasoa@email.com'),
    ('a1000000-0000-0000-0000-000000000005', 'RAIVO', 'Luc', '1991-11-08', 'MALE', 'Antananarivo', 'Apiculteur', '0345678901', 'luc.raivo@email.com'),
    ('a1000000-0000-0000-0000-000000000006', 'RANORO', 'Soa', '1993-09-14', 'FEMALE', 'Antananarivo', 'Agricultrice', '0346789012', 'soa.ranoro@email.com'),
    ('a1000000-0000-0000-0000-000000000007', 'RAFY', 'Niry', '1989-04-30', 'MALE', 'Antananarivo', 'Éleveur', '0347890123', 'niry.rafy@email.com'),
    ('a1000000-0000-0000-0000-000000000008', 'RAVELO', 'Tina', '1994-12-05', 'FEMALE', 'Antananarivo', 'Horticultrice', '0348901234', 'tina.ravelo@email.com'),
    ('a1000000-0000-0000-0000-000000000009', 'RATOVO', 'Marc', '1990-06-18', 'MALE', 'Antananarivo', 'Pisciculteur', '0349012345', 'marc.ratovo@email.com'),
    ('a1000000-0000-0000-0000-000000000010', 'RAMIALY', 'Zo', '1987-02-22', 'MALE', 'Antananarivo', 'Forestier', '0340123456', 'zo.ramialy@email.com'),
    ('a1000000-0000-0000-0000-000000000011', 'RAZAFY', 'Lova', '1996-08-12', 'FEMALE', 'Antananarivo', 'Cultivatrice', '0341112223', 'lova.razafy@email.com');

-- ============================================================
-- MEMBERSHIPS dans une collectivité existante
-- avec start_date > 6 mois pour valider la condition A
-- et occupation SENIOR pour pouvoir parrainer (condition B-2)
-- ============================================================

-- Les 11 membres dans cette collectivité existante depuis > 6 mois
INSERT INTO memberships (id, member_id, collectivity_id, occupation, start_date)
VALUES
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000001', 'b2000000-0000-0000-0000-000000000001', 'PRESIDENT',      '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000002', 'b2000000-0000-0000-0000-000000000001', 'VICE_PRESIDENT', '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'b2000000-0000-0000-0000-000000000001', 'TREASURER',      '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000004', 'b2000000-0000-0000-0000-000000000001', 'SECRETARY',      '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000005', 'b2000000-0000-0000-0000-000000000001', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000006', 'b2000000-0000-0000-0000-000000000001', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000007', 'b2000000-0000-0000-0000-000000000001', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000008', 'b2000000-0000-0000-0000-000000000001', 'SENIOR',         '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000009', 'b2000000-0000-0000-0000-000000000001', 'JUNIOR',         '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000010', 'b2000000-0000-0000-0000-000000000001', 'JUNIOR',         '2024-01-01'),
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000011', 'b2000000-0000-0000-0000-000000000001', 'JUNIOR',         '2024-01-01');

-- ============================================================
-- MOCK DATA v0.0.3
-- ============================================================

-- Fees
INSERT INTO fees (id, eligible_from, amount, label, frequency, status)
VALUES
    ('f1000000-0000-0000-0000-000000000001', '2024-01-01', 5000.00, 'Cotisation Mensuelle', 'MONTHLY', 'ACTIVE'),
    ('f1000000-0000-0000-0000-000000000002', '2024-01-01', 10000.00, 'Frais d''inscription', 'PUNCTUALLY', 'ACTIVE'),
    ('f1000000-0000-0000-0000-000000000003', '2024-01-01', 15000.00, 'Contribution annuelle matériel', 'ANNUALLY', 'ACTIVE');

-- Link Fees to Collectivity
INSERT INTO collectivityFee (id, collectivity_id, fee_id)
VALUES
    (gen_random_uuid(), 'b2000000-0000-0000-0000-000000000001', 'f1000000-0000-0000-0000-000000000001'),
    (gen_random_uuid(), 'b2000000-0000-0000-0000-000000000001', 'f1000000-0000-0000-0000-000000000002'),
    (gen_random_uuid(), 'b2000000-0000-0000-0000-000000000001', 'f1000000-0000-0000-0000-000000000003');

-- Accounts
INSERT INTO accounts (id, collectivity_id, type, balance, holder_name)
VALUES
    ('c1000000-0000-0000-0000-000000000001', 'b2000000-0000-0000-0000-000000000001', 'CASH', 0.00, 'Caisse Antananarivo');

INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, bank_name, bank_account_number)
VALUES
    ('c1000000-0000-0000-0000-000000000002', 'b2000000-0000-0000-0000-000000000001', 'BANK', 0.00, 'Compte BOA Antananarivo', 'BOA', '12345678901234567890123');

INSERT INTO accounts (id, collectivity_id, type, balance, holder_name, mobile_banking_service, mobile_number)
VALUES
    ('c1000000-0000-0000-0000-000000000003', 'b2000000-0000-0000-0000-000000000001', 'MOBILE_MONEY', 0.00, 'MVola Antananarivo', 'MVOLA', '0340000001');

-- Payments
INSERT INTO payments (id, amount, membership_fee_id, credited_account_id, payment_method, creation_date)
VALUES
    ('e1000000-0000-0000-0000-000000000001', 5000.00, 'f1000000-0000-0000-0000-000000000001', 'c1000000-0000-0000-0000-000000000001', 'CASH', '2024-04-20');

-- Transactions
INSERT INTO transactions (id, member_id, payment_id, creation_date)
VALUES
    ('d1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', 'e1000000-0000-0000-0000-000000000001', '2024-04-20');