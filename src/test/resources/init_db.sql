SELECT 'CREATE DATABASE drones_db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'drones_db')\gexec