INSERT INTO public.bank(name)
VALUES ('Clever');
INSERT INTO public.bank(name)
VALUES ('Alfa');
INSERT INTO public.bank(name)
VALUES ('Sber');
INSERT INTO public.bank(name)
VALUES ('Tinkoff');
INSERT INTO public.bank(name)
VALUES ('Беларусбанк');

INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000000', 'Вася Пупкин', 'HB0000000');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000001', 'Вася Пупкин', 'HB0000001');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000002', 'Вася Пупкин', 'HB0000002');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000003', 'Вася Пупкин', 'HB0000003');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000004', 'Вася Пупкин', 'HB0000004');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000005', 'Вася Пупкин', 'HB0000005');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000006', 'Вася Пупкин', 'HB0000006');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000007', 'Вася Пупкин', 'HB0000007');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000008', 'Вася Пупкин', 'HB0000008');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000009', 'Вася Пупкин', 'HB0000009');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000010', 'Вася Пупкин', 'HB0000010');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000011', 'Вася Пупкин', 'HB0000011');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000012', 'Вася Пупкин', 'HB0000012');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000013', 'Вася Пупкин', 'HB0000013');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000014', 'Вася Пупкин', 'HB0000014');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000015', 'Вася Пупкин', 'HB0000015');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000016', 'Вася Пупкин', 'HB0000016');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000017', 'Вася Пупкин', 'HB0000017');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000018', 'Вася Пупкин', 'HB0000018');
INSERT INTO public.client(id, name, passport_number)
VALUES ('00000000-0000-4000-8000-000000000019', 'Вася Пупкин', 'HB0000019');

INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000000', 10000, '00000000-0000-4000-8000-000000000000', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13ALFA00000000000000000000', 10000, '00000000-0000-4000-8000-000000000000', 2);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000001', 10000, '00000000-0000-4000-8000-000000000001', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13SBER00000000000000000000', 10000, '00000000-0000-4000-8000-000000000001', 3);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000002', 10000, '00000000-0000-4000-8000-000000000002', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13TNKF00000000000000000000', 10000, '00000000-0000-4000-8000-000000000002', 4);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000003', 10000, '00000000-0000-4000-8000-000000000003', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13BLRB00000000000000000000', 10000, '00000000-0000-4000-8000-000000000003', 5);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000004', 10000, '00000000-0000-4000-8000-000000000004', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13ALFA00000000000000000001', 10000, '00000000-0000-4000-8000-000000000004', 2);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000005', 10000, '00000000-0000-4000-8000-000000000005', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13SBER00000000000000000001', 10000, '00000000-0000-4000-8000-000000000005', 3);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000006', 10000, '00000000-0000-4000-8000-000000000006', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13TNKF00000000000000000001', 10000, '00000000-0000-4000-8000-000000000006', 4);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000007', 10000, '00000000-0000-4000-8000-000000000007', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13BLRB00000000000000000001', 10000, '00000000-0000-4000-8000-000000000007', 5);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000008', 10000, '00000000-0000-4000-8000-000000000008', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13ALFA00000000000000000002', 10000, '00000000-0000-4000-8000-000000000008', 2);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000009', 10000, '00000000-0000-4000-8000-000000000009', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13SBER00000000000000000002', 10000, '00000000-0000-4000-8000-000000000009', 3);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000010', 10000, '00000000-0000-4000-8000-000000000010', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13TNKF00000000000000000002', 10000, '00000000-0000-4000-8000-000000000010', 4);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000011', 10000, '00000000-0000-4000-8000-000000000011', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13BLRB00000000000000000002', 10000, '00000000-0000-4000-8000-000000000011', 5);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000012', 10000, '00000000-0000-4000-8000-000000000012', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13ALFA00000000000000000003', 10000, '00000000-0000-4000-8000-000000000012', 2);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000013', 10000, '00000000-0000-4000-8000-000000000013', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13SBER00000000000000000003', 10000, '00000000-0000-4000-8000-000000000013', 3);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000014', 10000, '00000000-0000-4000-8000-000000000014', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13TNKF00000000000000000003', 10000, '00000000-0000-4000-8000-000000000014', 4);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000015', 10000, '00000000-0000-4000-8000-000000000015', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13BLRB00000000000000000003', 10000, '00000000-0000-4000-8000-000000000015', 5);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000016', 10000, '00000000-0000-4000-8000-000000000016', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13ALFA00000000000000000004', 10000, '00000000-0000-4000-8000-000000000016', 2);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000017', 10000, '00000000-0000-4000-8000-000000000017', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13SBER00000000000000000004', 10000, '00000000-0000-4000-8000-000000000017', 3);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000018', 10000, '00000000-0000-4000-8000-000000000018', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13TNKF00000000000000000004', 10000, '00000000-0000-4000-8000-000000000018', 4);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13CLVR00000000000000000019', 10000, '00000000-0000-4000-8000-000000000019', 1);
INSERT INTO public.account("number", balance, client_id, bank_id)
VALUES ('BY13BLRB00000000000000000004', 10000, '00000000-0000-4000-8000-000000000019', 5);
