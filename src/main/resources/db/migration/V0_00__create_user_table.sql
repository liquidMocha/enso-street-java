CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public.user
(
    id                        UUID PRIMARY KEY         DEFAULT uuid_generate_v4(),
    password                  VARCHAR(76),
    email                     VARCHAR(76) NOT NULL UNIQUE,
    created_on                TIMESTAMP WITH TIME ZONE DEFAULT now(),
    failed_login_attempts     INTEGER                  DEFAULT 0,
    profile_name              VARCHAR(254),
    first_name                VARCHAR(100),
    last_name                 VARCHAR(100),
    phone                     VARCHAR(30),
    stripe_connect_account_id VARCHAR(45)
);
