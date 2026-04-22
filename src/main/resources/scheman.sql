-- Énumération pour la fréquence (selon le YAML)
CREATE TYPE frequency AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE membership_fees (
                                 id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                 collectivity_id uuid NOT NULL REFERENCES collectivities(id),
                                 label VARCHAR(255) NOT NULL,
                                 amount NUMERIC(15,2) NOT NULL CHECK (amount >= 0),
                                 fee_frequency frequency NOT NULL,
                                 eligible_from DATE NOT NULL,
                                 status activity_status DEFAULT 'ACTIVE'
);