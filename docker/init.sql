-- создаём базу и пользователя
CREATE DATABASE keycloak;
CREATE USER keycloak WITH PASSWORD 'keycloak';

-- назначаем владельцем базы keycloak
ALTER DATABASE keycloak OWNER TO keycloak;

-- даём пользователю полный доступ к схеме public этой базы
\connect keycloak;
GRANT ALL PRIVILEGES ON SCHEMA public TO keycloak;

-- теперь создаём таблицы для ostock_dev
\connect ostock_dev;

CREATE TABLE IF NOT EXISTS public.organizations (
    organization_id text PRIMARY KEY,
    name text,
    contact_name text,
    contact_email text,
    contact_phone text
);

CREATE TABLE IF NOT EXISTS public.licenses (
    license_id text PRIMARY KEY,
    organization_id text NOT NULL REFERENCES public.organizations (organization_id),
    description text,
    product_name text NOT NULL,
    license_type text NOT NULL,
    comment text
);

ALTER TABLE public.organizations OWNER TO postgres;
ALTER TABLE public.licenses OWNER TO postgres;
