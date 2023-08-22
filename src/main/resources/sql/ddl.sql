CREATE SCHEMA IF NOT EXISTS public;

ALTER SCHEMA public OWNER TO pg_database_owner;

COMMENT ON SCHEMA public IS 'standard public schema';

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.account
(
    number    character(28)               NOT NULL,
    balance   numeric(10, 2) DEFAULT 0.00 NOT NULL,
    client_id uuid                        NOT NULL,
    bank_id   smallint                    NOT NULL
);

ALTER TABLE public.account
    OWNER TO postgres;

CREATE TABLE public.bank
(
    id   smallint       NOT NULL,
    name character(100) NOT NULL
);

ALTER TABLE public.bank
    OWNER TO postgres;

CREATE SEQUENCE public.bank_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.bank_id_seq
    OWNER TO postgres;

ALTER SEQUENCE public.bank_id_seq OWNED BY public.bank.id;

CREATE TABLE public.client
(
    id              uuid           NOT NULL,
    name            character(100) NOT NULL,
    passport_number character(9)   NOT NULL
);

ALTER TABLE public.client
    OWNER TO postgres;

ALTER TABLE ONLY public.bank
    ALTER COLUMN id SET DEFAULT nextval('public.bank_id_seq'::regclass);

SELECT pg_catalog.setval('public.bank_id_seq', 1, false);

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_client_id_bank_id_key UNIQUE (client_id, bank_id);

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (number);

ALTER TABLE ONLY public.bank
    ADD CONSTRAINT bank_name_key UNIQUE (name);

ALTER TABLE ONLY public.bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_passport_number_key UNIQUE (passport_number);

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_bank_id_fkey FOREIGN KEY (bank_id) REFERENCES public.bank (id) NOT VALID;

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client (id) NOT VALID;
