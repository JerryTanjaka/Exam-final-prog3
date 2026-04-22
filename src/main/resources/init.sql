-- ============================================================
-- Fédération de Collectivités Agricoles — Script d'initialisation
-- Base : PostgreSQL 14+
-- ============================================================

-- ============================================================
-- TYPES ÉNUMÉRÉS
-- ============================================================

CREATE TYPE gender_type            AS ENUM ('MALE', 'FEMALE');
CREATE TYPE position_type          AS ENUM ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'SENIOR', 'JUNIOR');
CREATE TYPE contribution_type      AS ENUM ('MONTHLY', 'ANNUAL', 'PUNCTUAL');
CREATE TYPE payment_method         AS ENUM ('CASH', 'BANK_TRANSFER', 'MOBILE_MONEY');
CREATE TYPE account_type           AS ENUM ('CASH', 'BANK', 'MOBILE_MONEY');
CREATE TYPE bank_name              AS ENUM ('BRED','MCB','BMOI','BOA','BGFI','AFG','ACCES_BANQUE','BAOBAB','SIPEM');
CREATE TYPE mobile_money_service   AS ENUM ('ORANGE_MONEY', 'MVOLA', 'AIRTEL_MONEY');
CREATE TYPE activity_type          AS ENUM ('GENERAL_ASSEMBLY', 'JUNIOR_TRAINING', 'EXCEPTIONAL');
CREATE TYPE organizer_type         AS ENUM ('COMMUNITY', 'FEDERATION');
CREATE TYPE attendance_status      AS ENUM ('PRESENT', 'ABSENT', 'EXCUSED');

-- ============================================================
-- TABLE : collectivities
-- ============================================================

CREATE TABLE collectivities (
    id                              uuid      primary key default  gen_random_uuid(),
    number                          VARCHAR(50)   NOT NULL UNIQUE,
    name                            VARCHAR(255)  NOT NULL UNIQUE,
    city                            VARCHAR(255)  NOT NULL,
    specialty                       VARCHAR(255)  NOT NULL,
    creation_date                   DATE          NOT NULL
);

-- ============================================================
-- TABLE : members
-- ============================================================

CREATE TABLE members (
    id                               uuid      primary key default  gen_random_uuid(),
    last_name                        VARCHAR(255)  NOT NULL,
    first_name                       VARCHAR(255)  NOT NULL,
    birth_date                       DATE          NOT NULL,
    gender                           gender_type   NOT NULL,
    address                          TEXT          NOT NULL,
    profession                       VARCHAR(255)  NOT NULL,
    phone                            VARCHAR(50)   NOT NULL,
    email                            VARCHAR(255)  NOT NULL UNIQUE
);

CREATE TABLE memberships (
    id                               uuid      primary key default  gen_random_uuid(),
    member_id         uuid          NOT NULL REFERENCES members(id),
    collectivity_id   uuid           NOT NULL REFERENCES collectivities(id),
    occupation      position_type   NOT NULL,
    start_date      DATE            NOT NULL DEFAULT NOW(),
    end_date        DATE,
    UNIQUE (member_id, collectivity_id)
);

-- ============================================================
-- TABLE : referals
-- Parrainage d'un candidat par un membre confirmé (ancienneté > 90j)
-- Règle B-2 : >= 2 parrains, dont autant issus de la collectivité cible
--             que de collectivités extérieures
-- ============================================================

CREATE TABLE referals (
    id           CHAR(14)         PRIMARY KEY,
    member_id    CHAR(14)         NOT NULL REFERENCES members(id),  -- candidat
    referee_id   CHAR(14)         NOT NULL REFERENCES members(id),  -- parrain confirmé
    UNIQUE (member_id, referee_id)
);

-- ============================================================
-- TABLE : mandates
-- Mandat annuel d'une collectivité (1 mandat = 1 année civile)
-- Règle : un membre ne peut pas occuper le même poste > 2 fois au total
-- ============================================================

/*
CREATE TABLE mandates (
    id                CHAR(14      PRIMARY KEY,
    collectivity_id   CHAR(14      NOT NULL REFERENCES collectivities(id),
    year              INTEGER   NOT NULL,
    president_id      CHAR(14      NOT NULL REFERENCES members(id),
    vice_president_id CHAR(14      NOT NULL REFERENCES members(id),
    treasurer_id      CHAR(14      NOT NULL REFERENCES members(id),
    secretary_id      CHAR(14      NOT NULL REFERENCES members(id),
    start_date        DATE      NOT NULL,
    end_date          DATE      NOT NULL,
    UNIQUE (collectivity_id, year)
);
*/

-- ============================================================
-- TABLE : collectivity_changes
-- Historique des changements de collectivité d'un membre
-- ============================================================

-- ============================================================
-- TABLE : accounts
-- Comptes d'une collectivité ou de la fédération
-- collectivity_id = NULL  =>  compte de la fédération
-- ============================================================

CREATE TABLE accounts (
    id                   CHAR(14)         PRIMARY KEY,
    collectivity_id      CHAR(14)         REFERENCES collectivities(id),  -- NULL = fédération
    type                 account_type     NOT NULL,
    balance              NUMERIC(15,2)    NOT NULL DEFAULT 0,
    -- Champs communs BANK + MOBILE_MONEY
    holder_name          VARCHAR(255),
    -- Champs bancaires
    bank_name            bank_name,
    bank_account_number  CHAR(23),
    -- Champs mobile money
    mobile_money_service mobile_money_service,
    phone_number         VARCHAR(50),

    CONSTRAINT chk_bank_account_number
        CHECK (bank_account_number IS NULL OR bank_account_number ~ '^\d{23}$')
);

-- Une seule caisse par collectivité
CREATE UNIQUE INDEX idx_unique_cash_per_collectivity
    ON accounts (collectivity_id)
    WHERE type = 'CASH' AND collectivity_id IS NOT NULL;

-- Une seule caisse pour la fédération
CREATE UNIQUE INDEX idx_unique_cash_federation
    ON accounts ((type))
    WHERE type = 'CASH' AND collectivity_id IS NULL;

-- ============================================================
-- TABLE : federation_contribution_rates
-- Historique des taux de reversement à la fédération
-- Taux actif = ligne dont effective_date est la plus récente <= CURRENT_DATE
-- ============================================================

CREATE TABLE federation_contribution_rates (
    id             CHAR(14)     PRIMARY KEY,
    rate           NUMERIC(5,4) NOT NULL CHECK (rate >= 0 AND rate <= 1),
    effective_date DATE         NOT NULL,
);

-- ============================================================
-- TABLE : collectivity_contributions
-- Encaissements de cotisations enregistrés par le trésorier
-- ============================================================

CREATE TABLE collectivity_contributions (
    id                      CHAR(14)          PRIMARY KEY,
    collectivity_id         CHAR(14)          NOT NULL REFERENCES collectivities(id),
    type                    contribution_type NOT NULL,
    amount                  NUMERIC(15,2)     NOT NULL,
);

CREATE TABLE payments (
    id CHAR(14) PRIMARY KEY,
    member_id CHAR(14) NOT NULL,
    contribution_id CHAR(14) NOT NULL,
    payment_method payment_method NOT NULL,
    payment_date DATE NOT NULL DEFAULT NOW()
)

-- ============================================================
-- TABLE : activities
-- Activités d'une collectivité ou de la fédération
-- collectivity_id = NULL  =>  activité de la fédération
-- ============================================================

CREATE TABLE activities (
    id               CHAR(14)       PRIMARY KEY,
    collectivity_id  CHAR(14)       REFERENCES collectivities(id),  -- NULL = fédération
    title            VARCHAR(255)   NOT NULL,
    description      TEXT,
    type             activity_type  NOT NULL,
    activity_date    TIMESTAMP      NOT NULL,
    organized_by     organizer_type NOT NULL,
);

-- ============================================================
-- TABLE : activity_mandatory_members
-- Membres pour lesquels la présence est obligatoire (activités EXCEPTIONAL)
-- Vide = tous les membres concernés (selon la logique du type d'activité)
-- ============================================================

-- ============================================================
-- TABLE : activity_invited_collectivities
-- Collectivités invitées aux activités de la fédération
-- Vide = toutes les collectivités sont invitées
-- ============================================================

-- ============================================================
-- TABLE : attendances
-- Fiche de présence par activité et par membre
-- ============================================================

CREATE TABLE attendances (
    id                 CHAR(14)          PRIMARY KEY,
    activity_id        CHAR(14)          NOT NULL REFERENCES activities(id),
    member_id          CHAR(14)          NOT NULL REFERENCES members(id),
    status             attendance_status NOT NULL,
    absence_reason     TEXT,             -- obligatoire si status = EXCUSED
    is_mandatory       BOOLEAN           NOT NULL
    is_external_member BOOLEAN           NOT NULL DEFAULT FALSE,
    UNIQUE (activity_id, member_id),
);

-- ============================================================
-- DONNÉES INITIALES
-- ============================================================

-- Taux de reversement par défaut : 10%
INSERT INTO federation_contribution_rates (rate, effective_date)
VALUES (0.10, CURRENT_DATE);
