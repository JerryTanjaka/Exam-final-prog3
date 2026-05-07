#!/bin/bash

# ============================================================
# reset_db.sh — Drop et recréation de la base de données
# ============================================================

DB_NAME="fprog3"
DB_USER="postgres"
DB_HOST="localhost"
DB_PORT="5432"

export PGPASSWORD="kingaff"

INIT_SQL="$(dirname "$0")/src/main/resources/init.sql"
DATA_SQL="$(dirname "$0")/src/main/resources/data.sql"

echo "🔴 Drop de la base $DB_NAME..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -c "DROP DATABASE IF EXISTS $DB_NAME;"

echo "🟡 Création de la base $DB_NAME..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -c "CREATE DATABASE $DB_NAME;"

echo "🟡 Exécution de init.sql..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$INIT_SQL"

echo "🟡 Exécution de data.sql..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$DATA_SQL"

echo "✅ Base de données réinitialisée avec succès !"