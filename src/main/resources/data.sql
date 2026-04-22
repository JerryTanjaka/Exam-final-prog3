-- ============================================================
-- MEMBRES DE BASE (déjà dans la fédération depuis > 6 mois)
-- pour pouvoir créer une collectivité et parrainer
-- ============================================================
-- 1. Corriger la contrainte
ALTER TABLE collectivities
    ALTER COLUMN number DROP NOT NULL;

ALTER TABLE collectivities
    ALTER COLUMN name DROP NOT NULL;

-- 2. Relancer les inserts
INSERT INTO collectivities (id, city, specialty, creation_date)
VALUES ('b2000000-0000-0000-0000-000000000001', 'Antananarivo', 'Riziculture', '2024-01-01');

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
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000010', 'b2000000-0000-0000-0000-000000000001', 'JUNIOR',         '2024-01-01');
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
    ('a1000000-0000-0000-0000-000000000010', 'RAMIALY', 'Zo', '1987-02-22', 'MALE', 'Antananarivo', 'Forestier', '0340123456', 'zo.ramialy@email.com');

-- ============================================================
-- MEMBERSHIPS dans une collectivité existante
-- avec start_date > 6 mois pour valider la condition A
-- et occupation SENIOR pour pouvoir parrainer (condition B-2)
-- ============================================================

-- On a besoin d'une collectivité existante d'abord
INSERT INTO collectivities (id, city, specialty, creation_date)
VALUES ('b2000000-0000-0000-0000-000000000001', 'Antananarivo', 'Riziculture', '2024-01-01');

-- Les 10 membres dans cette collectivité existante depuis > 6 mois
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
    (gen_random_uuid(), 'a1000000-0000-0000-0000-000000000010', 'b2000000-0000-0000-0000-000000000001', 'JUNIOR',         '2024-01-01');