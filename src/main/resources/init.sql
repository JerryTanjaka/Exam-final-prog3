-- ============================================================
-- Fédération de Collectivités Agricoles — Script d'initialisation
-- Base : PostgreSQL 14+
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- TYPES ÉNUMÉRÉS
-- ============================================================

CREATE TYPE community_status       AS ENUM ('PENDING', 'AUTHORIZED', 'REJECTED');
CREATE TYPE gender_type            AS ENUM ('MALE', 'FEMALE');
CREATE TYPE position_type          AS ENUM ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'CONFIRMED_MEMBER', 'JUNIOR_MEMBER');
CREATE TYPE contribution_type      AS ENUM ('MONTHLY', 'ANNUAL', 'PUNCTUAL');
CREATE TYPE payment_method         AS ENUM ('CASH', 'BANK_TRANSFER', 'MOBILE_MONEY');
CREATE TYPE account_type           AS ENUM ('CASH', 'BANK', 'MOBILE_MONEY');
CREATE TYPE bank_name              AS ENUM ('BRED','MCB','BMOI','BOA','BGFI','AFG','ACCES_BANQUE','BAOBAB','SIPEM');
CREATE TYPE mobile_money_service   AS ENUM ('ORANGE_MONEY', 'MVOLA', 'AIRTEL_MONEY');
CREATE TYPE activity_type          AS ENUM ('GENERAL_ASSEMBLY', 'JUNIOR_TRAINING', 'EXCEPTIONAL');
CREATE TYPE organizer_type         AS ENUM ('COMMUNITY', 'FEDERATION');
CREATE TYPE attendance_status      AS ENUM ('PRESENT', 'ABSENT', 'EXCUSED');

-- ============================================================
-- TABLE : communities
-- ============================================================

CREATE TABLE communities (
    id                              serial          PRIMARY KEY DEFAULT gen_random_uuid(),
    number                          VARCHAR(50)   NOT NULL UNIQUE,
    name                            VARCHAR(255)  NOT NULL UNIQUE,
    city                            VARCHAR(255)  NOT NULL,
    agricultural_specialty          VARCHAR(255)  NOT NULL,
    creation_date                   DATE          NOT NULL,
    status                          community_status NOT NULL DEFAULT 'PENDING',
    authorization_comment           TEXT,
    authorization_date              DATE,
    -- Montant des cotisations annuelles obligatoires imposées par la collectivité (MGA)
    mandatory_annual_contribution   NUMERIC(15,2) NOT NULL DEFAULT 0,
    created_at                      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- ============================================================
-- TABLE : members
-- ============================================================

CREATE TABLE members (
    id                               serial          PRIMARY KEY DEFAULT gen_random_uuid(),
    last_name                        VARCHAR(255)  NOT NULL,
    first_name                       VARCHAR(255)  NOT NULL,
    birth_date                       DATE          NOT NULL,
    gender                           gender_type   NOT NULL,
    address                          TEXT          NOT NULL,
    occupation                       VARCHAR(255)  NOT NULL,
    phone                            VARCHAR(50)   NOT NULL,
    email                            VARCHAR(255)  NOT NULL UNIQUE,
    membership_date                  DATE          NOT NULL,
    community_id                     serial          NOT NULL REFERENCES communities(id),
    -- Poste du membre (mis à jour à chaque nouveau mandat)
    position                         position_type NOT NULL DEFAULT 'JUNIOR_MEMBER',
    active                           BOOLEAN       NOT NULL DEFAULT TRUE,
    resignation_date                 DATE,
    -- Paiement versé lors de l'adhésion (espèce non autorisée)
    registration_fee_paid            NUMERIC(15,2) NOT NULL DEFAULT 50000,
    registration_annual_contribution NUMERIC(15,2) NOT NULL DEFAULT 0,
    registration_payment_method      payment_method NOT NULL DEFAULT 'MOBILE_MONEY',
    registration_payment_date        DATE          NOT NULL DEFAULT CURRENT_DATE,
    created_at                       TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at                       TIMESTAMP     NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_registration_payment_method
        CHECK (registration_payment_method IN ('MOBILE_MONEY', 'BANK_TRANSFER'))
);

CREATE INDEX idx_members_community ON members(community_id);
CREATE INDEX idx_members_active    ON members(active);
CREATE INDEX idx_members_position  ON members(position);

-- ============================================================
-- TABLE : sponsors
-- Parrainage d'un candidat par un membre confirmé (ancienneté > 90j)
-- Règle B-2 : >= 2 parrains, dont autant issus de la collectivité cible
--             que de collectivités extérieures
-- ============================================================

CREATE TABLE sponsors (
    id           serial         PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id    serial         NOT NULL REFERENCES members(id),  -- candidat
    sponsor_id   serial         NOT NULL REFERENCES members(id),  -- parrain confirmé
    relationship VARCHAR(255) NOT NULL,                          -- famille, amis, collègues…
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE (member_id, sponsor_id)
);

CREATE INDEX idx_sponsors_member  ON sponsors(member_id);
CREATE INDEX idx_sponsors_sponsor ON sponsors(sponsor_id);

-- ============================================================
-- TABLE : mandates
-- Mandat annuel d'une collectivité (1 mandat = 1 année civile)
-- Règle : un membre ne peut pas occuper le même poste > 2 fois au total
-- ============================================================

CREATE TABLE mandates (
    id                serial      PRIMARY KEY DEFAULT gen_random_uuid(),
    community_id      serial      NOT NULL REFERENCES communities(id),
    year              INTEGER   NOT NULL,
    president_id      serial      NOT NULL REFERENCES members(id),
    vice_president_id serial      NOT NULL REFERENCES members(id),
    treasurer_id      serial      NOT NULL REFERENCES members(id),
    secretary_id      serial      NOT NULL REFERENCES members(id),
    start_date        DATE      NOT NULL,
    end_date          DATE      NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (community_id, year)
);

CREATE INDEX idx_mandates_community_year ON mandates(community_id, year);

-- ============================================================
-- TABLE : community_changes
-- Historique des changements de collectivité d'un membre
-- ============================================================

CREATE TABLE community_changes (
    id               serial      PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id        serial      NOT NULL REFERENCES members(id),
    old_community_id serial      REFERENCES communities(id),        -- NULL à l'admission initiale
    new_community_id serial      NOT NULL REFERENCES communities(id),
    reason           TEXT      NOT NULL,
    change_date      DATE      NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_community_changes_member ON community_changes(member_id);

-- ============================================================
-- TABLE : accounts
-- Comptes d'une collectivité ou de la fédération
-- community_id = NULL  =>  compte de la fédération
-- ============================================================

CREATE TABLE accounts (
    id                   serial             PRIMARY KEY DEFAULT gen_random_uuid(),
    community_id         serial             REFERENCES communities(id),  -- NULL = fédération
    type                 account_type     NOT NULL,
    balance              NUMERIC(15,2)    NOT NULL DEFAULT 0,
    statement_date       DATE,
    opening_date         DATE,
    -- Champs communs BANK + MOBILE_MONEY
    holder_name          VARCHAR(255),
    -- Champs bancaires
    bank_name            bank_name,
    bank_account_number  CHAR(23),
    -- Champs mobile money
    mobile_money_service mobile_money_service,
    phone_number         VARCHAR(50),
    created_at           TIMESTAMP        NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP        NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_bank_account_number
        CHECK (bank_account_number IS NULL OR bank_account_number ~ '^\d{23}$')
);

-- Une seule caisse par collectivité
CREATE UNIQUE INDEX idx_unique_cash_per_community
    ON accounts (community_id)
    WHERE type = 'CASH' AND community_id IS NOT NULL;

-- Une seule caisse pour la fédération
CREATE UNIQUE INDEX idx_unique_cash_federation
    ON accounts ((type))
    WHERE type = 'CASH' AND community_id IS NULL;

CREATE INDEX idx_accounts_community ON accounts(community_id);

-- ============================================================
-- TABLE : contribution_rates
-- Historique des taux de reversement à la fédération
-- Taux actif = ligne dont effective_date est la plus récente <= CURRENT_DATE
-- ============================================================

CREATE TABLE contribution_rates (
    id             serial         PRIMARY KEY DEFAULT gen_random_uuid(),
    rate           NUMERIC(5,4) NOT NULL CHECK (rate >= 0 AND rate <= 1),
    effective_date DATE         NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ============================================================
-- TABLE : contributions
-- Encaissements de cotisations enregistrés par le trésorier
-- ============================================================

CREATE TABLE contributions (
    id                      serial              PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id               serial              NOT NULL REFERENCES members(id),
    community_id            serial              NOT NULL REFERENCES communities(id),
    type                    contribution_type NOT NULL,
    amount                  NUMERIC(15,2)     NOT NULL,
    collection_date         DATE              NOT NULL,
    payment_method          payment_method    NOT NULL,
    reason                  TEXT,             -- obligatoire si type = PUNCTUAL
    federation_share_amount NUMERIC(15,2),    -- NULL si type = PUNCTUAL
    created_at              TIMESTAMP         NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_punctual_reason
        CHECK (type != 'PUNCTUAL' OR reason IS NOT NULL)
);

CREATE INDEX idx_contributions_community ON contributions(community_id);
CREATE INDEX idx_contributions_member    ON contributions(member_id);
CREATE INDEX idx_contributions_date      ON contributions(collection_date);
CREATE INDEX idx_contributions_type      ON contributions(type);

-- ============================================================
-- TABLE : activities
-- Activités d'une collectivité ou de la fédération
-- community_id = NULL  =>  activité de la fédération
-- ============================================================

CREATE TABLE activities (
    id            serial           PRIMARY KEY DEFAULT gen_random_uuid(),
    community_id  serial           REFERENCES communities(id),  -- NULL = fédération
    title         VARCHAR(255)   NOT NULL,
    description   TEXT,
    type          activity_type  NOT NULL,
    activity_date TIMESTAMP      NOT NULL,
    mandatory     BOOLEAN        NOT NULL DEFAULT FALSE,
    organized_by  organizer_type NOT NULL,
    created_at    TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activities_community ON activities(community_id);
CREATE INDEX idx_activities_date      ON activities(activity_date);
CREATE INDEX idx_activities_type      ON activities(type);

-- ============================================================
-- TABLE : activity_mandatory_members
-- Membres pour lesquels la présence est obligatoire (activités EXCEPTIONAL)
-- Vide = tous les membres concernés (selon la logique du type d'activité)
-- ============================================================

CREATE TABLE activity_mandatory_members (
    activity_id serial NOT NULL REFERENCES activities(id) ON DELETE CASCADE,
    member_id   serial NOT NULL REFERENCES members(id),
    PRIMARY KEY (activity_id, member_id)
);

-- ============================================================
-- TABLE : activity_invited_communities
-- Collectivités invitées aux activités de la fédération
-- Vide = toutes les collectivités sont invitées
-- ============================================================

CREATE TABLE activity_invited_communities (
    activity_id  serial NOT NULL REFERENCES activities(id) ON DELETE CASCADE,
    community_id serial NOT NULL REFERENCES communities(id),
    PRIMARY KEY (activity_id, community_id)
);

-- ============================================================
-- TABLE : attendances
-- Fiche de présence par activité et par membre
-- ============================================================

CREATE TABLE attendances (
    id                 serial              PRIMARY KEY DEFAULT gen_random_uuid(),
    activity_id        serial              NOT NULL REFERENCES activities(id),
    member_id          serial              NOT NULL REFERENCES members(id),
    status             attendance_status NOT NULL,
    absence_reason     TEXT,             -- obligatoire si status = EXCUSED
    is_external_member BOOLEAN           NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP         NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP         NOT NULL DEFAULT NOW(),

    UNIQUE (activity_id, member_id),

    CONSTRAINT chk_excused_reason
        CHECK (status != 'EXCUSED' OR absence_reason IS NOT NULL)
);

CREATE INDEX idx_attendances_activity ON attendances(activity_id);
CREATE INDEX idx_attendances_member   ON attendances(member_id);

-- ============================================================
-- DONNÉES INITIALES
-- ============================================================

-- Taux de reversement par défaut : 10%
INSERT INTO contribution_rates (rate, effective_date)
VALUES (0.10, CURRENT_DATE);
