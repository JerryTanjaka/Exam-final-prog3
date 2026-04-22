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
    id                              UUID          PRIMARY KEY DEFAULT  gen_random_uuid(),
    number                          VARCHAR(50)   UNIQUE,
    name                            VARCHAR(255)  UNIQUE,
    location                        VARCHAR(255)  NOT NULL,
    specialty                       VARCHAR(255)  NOT NULL,
    creation_date                   DATE          NOT NULL DEFAULT NOW()
);

-- ============================================================
-- TABLE : members
-- ============================================================

CREATE TABLE members (
    id                               UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
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
    id                UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id         UUID            NOT NULL REFERENCES members(id),
    collectivity_id   UUID            NOT NULL REFERENCES collectivities(id),
    occupation        position_type   NOT NULL,
    start_date        DATE            NOT NULL DEFAULT NOW(),
    end_date          DATE,
    UNIQUE (member_id, collectivity_id)
);

-- ============================================================
-- TABLE : referals
-- Parrainage d'un candidat par un membre confirmé (ancienneté > 90j)
-- Règle B-2 : >= 2 parrains, dont autant issus de la collectivité cible
--             que de collectivités extérieures
-- ============================================================

CREATE TABLE referals (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id    UUID         NOT NULL REFERENCES members(id),  -- candidat
    referee_id   UUID         NOT NULL REFERENCES members(id),  -- parrain confirmé
    UNIQUE (member_id, referee_id)
);

CREATE TABLE accounts (
    id                      UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
    collectivity_id         UUID             NOT NULL REFERENCES collectivities(id),  -- NULL = fédération
    type                    account_type     NOT NULL,
    balance                 NUMERIC(15,2)    NOT NULL DEFAULT 0,

    holder_name             VARCHAR(255),

    bank_name               bank_name,
    bank_account_number     CHAR(23),

    mobile_banking_service  mobile_money_service,
    mobile_number           VARCHAR(50)
);

CREATE TABLE payments (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    amount              NUMERIC(15,2)   NOT NULL,
    membership_fee_id   UUID            NOT NULL,
    credited_account_id UUID            NOT NULL,
    payment_method      payment_method  NOT NULL,
    creation_date       DATE            NOT NULL DEFAULT NOW()
);

CREATE TABLE transactions (
    id              UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id       UUID    NOT NULL,
    payment_id      UUID    NOT NULL,
    creation_date   DATE    NOT NULL DEFAULT NOW()
);

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
