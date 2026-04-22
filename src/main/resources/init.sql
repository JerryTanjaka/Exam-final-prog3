-- ============================================================
-- Fédération de Collectivités Agricoles — Script d'initialisation
-- Base : PostgreSQL 14+
-- ============================================================

-- ============================================================
-- TYPES ÉNUMÉRÉS
-- ============================================================

CREATE TYPE gender_type            AS ENUM ('MALE', 'FEMALE');
CREATE TYPE position_type          AS ENUM ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'SENIOR', 'JUNIOR');
CREATE TYPE fee_frequency_type     AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE payment_method         AS ENUM ('CASH', 'BANK_TRANSFER', 'MOBILE_BANKING');
CREATE TYPE account_type           AS ENUM ('CASH', 'BANK', 'MOBILE_MONEY');
CREATE TYPE bank_name              AS ENUM ('BRED','MCB','BMOI','BOA','BGFI','AFG','ACCES_BANQUE','BAOBAB','SIPEM');
CREATE TYPE mobile_money_service   AS ENUM ('ORANGE_MONEY', 'MVOLA', 'AIRTEL_MONEY');
CREATE TYPE activity_status        AS ENUM ('ACTIVE', 'INACTIVE');

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

CREATE TABLE fees (
    id              UUID            PRIMARY KEY DEFAULT  gen_random_uuid(),
    eligible_from   DATE            NOT NULL,
    amount          NUMERIC(15,2)   NOT NULL,
    label           VARCHAR(255)    NOT NULL,
    frequency       fee_frequency_type NOT NULL,
    status          activity_status NOT NULL
);

CREATE TABLE collectivityFee (
    id              UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    collectivity_id UUID    NOT NULL REFERENCES collectivities(id),
    fee_id          UUID    NOT NULL REFERENCES fees(id)
);