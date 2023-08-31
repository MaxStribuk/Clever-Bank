CREATE SCHEMA IF NOT EXISTS public;

ALTER SCHEMA public OWNER TO pg_database_owner;

COMMENT ON SCHEMA public IS 'standard public schema';

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.account
(
    number           character(28) NOT NULL,
    balance          numeric(10, 2)         DEFAULT 0.00 NOT NULL,
    client_id        uuid          NOT NULL,
    bank_id          smallint      NOT NULL,
    open_date        date          NOT NULL,
    interest_accrued boolean       NOT NULL DEFAULT false
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

CREATE TABLE public.transaction
(
    id           uuid                           NOT NULL,
    type_id      smallint                       NOT NULL,
    account_from character(28),
    account_to   character(28),
    amount       numeric(10, 2)                 NOT NULL,
    "time"       timestamp(0) without time zone NOT NULL
);

ALTER TABLE public.transaction
    OWNER TO postgres;

CREATE TABLE public.transaction_type
(
    id   smallint      NOT NULL,
    type character(20) NOT NULL
);

ALTER TABLE public.transaction_type
    OWNER TO postgres;

ALTER TABLE ONLY public.bank
    ALTER COLUMN id SET DEFAULT nextval('public.bank_id_seq'::regclass);

COPY public.account (number, balance, client_id, bank_id, open_date) FROM stdin;
BY13CLVR00000000000000000000	10000.00	00000000-0000-4000-8000-000000000000	1	2000-01-01
BY13ALFA00000000000000000000	10000.00	00000000-0000-4000-8000-000000000000	2	2000-01-01
BY13CLVR00000000000000000001	10000.00	00000000-0000-4000-8000-000000000001	1	2000-01-01
BY13SBER00000000000000000000	10000.00	00000000-0000-4000-8000-000000000001	3	2000-01-01
BY13CLVR00000000000000000002	10000.00	00000000-0000-4000-8000-000000000002	1	2000-01-01
BY13TNKF00000000000000000000	10000.00	00000000-0000-4000-8000-000000000002	4	2000-01-01
BY13CLVR00000000000000000003	10000.00	00000000-0000-4000-8000-000000000003	1	2000-01-01
BY13BLRB00000000000000000000	10000.00	00000000-0000-4000-8000-000000000003	5	2000-01-01
BY13CLVR00000000000000000004	10000.00	00000000-0000-4000-8000-000000000004	1	2000-01-01
BY13ALFA00000000000000000001	10000.00	00000000-0000-4000-8000-000000000004	2	2000-01-01
BY13CLVR00000000000000000005	10000.00	00000000-0000-4000-8000-000000000005	1	2000-01-01
BY13SBER00000000000000000001	10000.00	00000000-0000-4000-8000-000000000005	3	2000-01-01
BY13CLVR00000000000000000006	10000.00	00000000-0000-4000-8000-000000000006	1	2000-01-01
BY13TNKF00000000000000000001	10000.00	00000000-0000-4000-8000-000000000006	4	2000-01-01
BY13CLVR00000000000000000007	10000.00	00000000-0000-4000-8000-000000000007	1	2000-01-01
BY13BLRB00000000000000000001	10000.00	00000000-0000-4000-8000-000000000007	5	2000-01-01
BY13CLVR00000000000000000008	10000.00	00000000-0000-4000-8000-000000000008	1	2000-01-01
BY13ALFA00000000000000000002	10000.00	00000000-0000-4000-8000-000000000008	2	2000-01-01
BY13CLVR00000000000000000009	10000.00	00000000-0000-4000-8000-000000000009	1	2000-01-01
BY13SBER00000000000000000002	10000.00	00000000-0000-4000-8000-000000000009	3	2000-01-01
BY13CLVR00000000000000000010	10000.00	00000000-0000-4000-8000-000000000010	1	2000-01-01
BY13TNKF00000000000000000002	10000.00	00000000-0000-4000-8000-000000000010	4	2000-01-01
BY13CLVR00000000000000000011	10000.00	00000000-0000-4000-8000-000000000011	1	2000-01-01
BY13BLRB00000000000000000002	10000.00	00000000-0000-4000-8000-000000000011	5	2000-01-01
BY13CLVR00000000000000000012	10000.00	00000000-0000-4000-8000-000000000012	1	2000-01-01
BY13ALFA00000000000000000003	10000.00	00000000-0000-4000-8000-000000000012	2	2000-01-01
BY13CLVR00000000000000000013	10000.00	00000000-0000-4000-8000-000000000013	1	2000-01-01
BY13SBER00000000000000000003	10000.00	00000000-0000-4000-8000-000000000013	3	2000-01-01
BY13CLVR00000000000000000014	10000.00	00000000-0000-4000-8000-000000000014	1	2000-01-01
BY13TNKF00000000000000000003	10000.00	00000000-0000-4000-8000-000000000014	4	2000-01-01
BY13CLVR00000000000000000015	10000.00	00000000-0000-4000-8000-000000000015	1	2000-01-01
BY13BLRB00000000000000000003	10000.00	00000000-0000-4000-8000-000000000015	5	2000-01-01
BY13CLVR00000000000000000016	10000.00	00000000-0000-4000-8000-000000000016	1	2000-01-01
BY13ALFA00000000000000000004	10000.00	00000000-0000-4000-8000-000000000016	2	2000-01-01
BY13CLVR00000000000000000017	10000.00	00000000-0000-4000-8000-000000000017	1	2000-01-01
BY13SBER00000000000000000004	10000.00	00000000-0000-4000-8000-000000000017	3	2000-01-01
BY13CLVR00000000000000000018	10000.00	00000000-0000-4000-8000-000000000018	1	2000-01-01
BY13TNKF00000000000000000004	10000.00	00000000-0000-4000-8000-000000000018	4	2000-01-01
BY13CLVR00000000000000000019	10000.00	00000000-0000-4000-8000-000000000019	1	2000-01-01
BY13BLRB00000000000000000004	10000.00	00000000-0000-4000-8000-000000000019	5	2000-01-01
\.

COPY public.bank (id, name) FROM stdin;
1	Clever
2	Alfa
3	Sber
4	Tinkoff
5	Беларусбанк
\.

COPY public.client (id, name, passport_number) FROM stdin;
00000000-0000-4000-8000-000000000000	Вася Пупкин                                                                                         	HB0000000
00000000-0000-4000-8000-000000000001	Вася Пупкин                                                                                         	HB0000001
00000000-0000-4000-8000-000000000002	Вася Пупкин                                                                                         	HB0000002
00000000-0000-4000-8000-000000000003	Вася Пупкин                                                                                         	HB0000003
00000000-0000-4000-8000-000000000004	Вася Пупкин                                                                                         	HB0000004
00000000-0000-4000-8000-000000000005	Вася Пупкин                                                                                         	HB0000005
00000000-0000-4000-8000-000000000006	Вася Пупкин                                                                                         	HB0000006
00000000-0000-4000-8000-000000000007	Вася Пупкин                                                                                         	HB0000007
00000000-0000-4000-8000-000000000008	Вася Пупкин                                                                                         	HB0000008
00000000-0000-4000-8000-000000000009	Вася Пупкин                                                                                         	HB0000009
00000000-0000-4000-8000-000000000010	Вася Пупкин                                                                                         	HB0000010
00000000-0000-4000-8000-000000000011	Вася Пупкин                                                                                         	HB0000011
00000000-0000-4000-8000-000000000012	Вася Пупкин                                                                                         	HB0000012
00000000-0000-4000-8000-000000000013	Вася Пупкин                                                                                         	HB0000013
00000000-0000-4000-8000-000000000014	Вася Пупкин                                                                                         	HB0000014
00000000-0000-4000-8000-000000000015	Вася Пупкин                                                                                         	HB0000015
00000000-0000-4000-8000-000000000016	Вася Пупкин                                                                                         	HB0000016
00000000-0000-4000-8000-000000000017	Вася Пупкин                                                                                         	HB0000017
00000000-0000-4000-8000-000000000018	Вася Пупкин                                                                                         	HB0000018
00000000-0000-4000-8000-000000000019	Вася Пупкин                                                                                         	HB0000019
\.

COPY public.transaction_type (id, type) FROM stdin;
1	Пополнение
2	Снятие
3	Перевод
4	Начисление процентов
\.

SELECT pg_catalog.setval('public.bank_id_seq', 5, true);

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

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.transaction_type
    ADD CONSTRAINT transaction_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.transaction_type
    ADD CONSTRAINT transaction_type_type_key UNIQUE (type);

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_bank_id_fkey FOREIGN KEY (bank_id) REFERENCES public.bank (id) NOT VALID;

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client (id) NOT VALID;

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_account_from_fkey FOREIGN KEY (account_from) REFERENCES public.account (number);

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_account_to_fkey FOREIGN KEY (account_to) REFERENCES public.account (number);

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.transaction_type (id);
