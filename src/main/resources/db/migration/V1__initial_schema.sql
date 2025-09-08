-- V1: initial schema snapshot

-- 1. Sequences
CREATE SEQUENCE public.czesci_magazyn_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.dzialy_id_seq          START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.harmonogramy_id_seq    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.maszyny_id_seq         START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.osoby_id_seq           START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.part_usages_id_seq     START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.parts_id_seq           START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.raporty_id_seq         START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.roles_id_seq           START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.users_id_seq           START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE public.zgloszenie_id_seq      START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- 2. Tables
CREATE TABLE public.czesci_magazyn (
                                       id               BIGINT NOT NULL,
                                       data_dodania     DATE,
                                       dostawca         VARCHAR(255),
                                       ilosc            INTEGER,
                                       nazwa            VARCHAR(255),
                                       numer_katalogowy VARCHAR(255),
                                       producent        VARCHAR(255)
);

CREATE TABLE public.dzialy (
                               id    BIGINT NOT NULL,
                               nazwa VARCHAR(255) NOT NULL
);

CREATE TABLE public.harmonogramy (
                                     id        BIGINT NOT NULL,
                                     data      DATE,
                                     opis      VARCHAR(255),
                                     status    VARCHAR(40) NOT NULL,
                                     maszyna_id BIGINT,
                                     osoba_id   BIGINT,
                                     CONSTRAINT harmonogramy_status_check CHECK (
                                         status IN ('PLANOWANE','W_TRAKCIE','BRAK_CZESCI','OCZEKIWANIE_NA_CZESC','ZAKONCZONE')
                                         )
);

CREATE TABLE public.maszyny (
                                id       BIGINT NOT NULL,
                                nazwa    VARCHAR(255) NOT NULL,
                                dzial_id BIGINT
);

CREATE TABLE public.osoby (
                              id            BIGINT NOT NULL,
                              haslo         VARCHAR(255),
                              imie_nazwisko VARCHAR(255),
                              login         VARCHAR(255),
                              rola          VARCHAR(255)
);

CREATE TABLE public.part_usages (
                                    id        BIGINT NOT NULL,
                                    ilosc     INTEGER,
                                    part_id   BIGINT NOT NULL,
                                    raport_id BIGINT NOT NULL
);

CREATE TABLE public.parts (
                              id        BIGINT NOT NULL,
                              ilosc     INTEGER,
                              jednostka VARCHAR(255),
                              kategoria VARCHAR(255),
                              kod       VARCHAR(255) NOT NULL,
                              min_ilosc INTEGER,
                              nazwa     VARCHAR(255) NOT NULL
);

CREATE TABLE public.raporty (
                                id           BIGINT NOT NULL,
                                czas_do      TIME(6) WITHOUT TIME ZONE,
                                czas_od      TIME(6) WITHOUT TIME ZONE,
                                data_naprawy DATE,
                                opis         VARCHAR(4000),
                                status       VARCHAR(40),
                                typ_naprawy  VARCHAR(255),
                                maszyna_id   BIGINT,
                                osoba_id     BIGINT,
                                CONSTRAINT raporty_status_check CHECK (
                                    status IN ('NOWY','W_TOKU','OCZEKUJE_CZESCI','ZAKONCZONE','ANULOWANE')
                                    )
);

CREATE TABLE public.roles (
                              id   BIGINT NOT NULL,
                              name VARCHAR(255) NOT NULL
);

CREATE TABLE public.user_roles (
                                   user_id BIGINT NOT NULL,
                                   role_id BIGINT NOT NULL
);

CREATE TABLE public.users (
                              id       BIGINT NOT NULL,
                              password VARCHAR(255) NOT NULL,
                              username VARCHAR(255) NOT NULL
);

CREATE TABLE public.zgloszenie (
                                   id          BIGINT NOT NULL,
                                   created_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                                   data_godzina TIMESTAMP(6) WITHOUT TIME ZONE,
                                   imie        VARCHAR(50),
                                   nazwisko    VARCHAR(50),
                                   opis        VARCHAR(2000),
                                   priorytet   VARCHAR(255) NOT NULL,
                                   status      VARCHAR(255) NOT NULL,
                                   typ         VARCHAR(100),
                                   tytul       VARCHAR(200),
                                   updated_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                                   autor_id    BIGINT,
                                   dzial_id    BIGINT,
                                   CONSTRAINT zgloszenie_priorytet_check CHECK (
                                       priorytet IN ('NISKI','NORMALNY','WYSOKI','KRYTYCZNY')
                                       ),
                                   CONSTRAINT zgloszenie_status_check CHECK (
                                       status IN ('OPEN','IN_PROGRESS','ON_HOLD','DONE','REJECTED')
                                       )
);

-- 3. OWNED BY (opcjonalne – można zostawić, zapewnia powiązanie sekwencji z kolumną)
ALTER SEQUENCE public.czesci_magazyn_id_seq OWNED BY public.czesci_magazyn.id;
ALTER SEQUENCE public.dzialy_id_seq        OWNED BY public.dzialy.id;
ALTER SEQUENCE public.harmonogramy_id_seq  OWNED BY public.harmonogramy.id;
ALTER SEQUENCE public.maszyny_id_seq       OWNED BY public.maszyny.id;
ALTER SEQUENCE public.osoby_id_seq         OWNED BY public.osoby.id;
ALTER SEQUENCE public.part_usages_id_seq   OWNED BY public.part_usages.id;
ALTER SEQUENCE public.parts_id_seq         OWNED BY public.parts.id;
ALTER SEQUENCE public.raporty_id_seq       OWNED BY public.raporty.id;
ALTER SEQUENCE public.roles_id_seq         OWNED BY public.roles.id;
ALTER SEQUENCE public.users_id_seq         OWNED BY public.users.id;
ALTER SEQUENCE public.zgloszenie_id_seq    OWNED BY public.zgloszenie.id;

-- 4. Defaults
ALTER TABLE ONLY public.czesci_magazyn ALTER COLUMN id SET DEFAULT nextval('public.czesci_magazyn_id_seq');
ALTER TABLE ONLY public.dzialy         ALTER COLUMN id SET DEFAULT nextval('public.dzialy_id_seq');
ALTER TABLE ONLY public.harmonogramy   ALTER COLUMN id SET DEFAULT nextval('public.harmonogramy_id_seq');
ALTER TABLE ONLY public.maszyny        ALTER COLUMN id SET DEFAULT nextval('public.maszyny_id_seq');
ALTER TABLE ONLY public.osoby          ALTER COLUMN id SET DEFAULT nextval('public.osoby_id_seq');
ALTER TABLE ONLY public.part_usages    ALTER COLUMN id SET DEFAULT nextval('public.part_usages_id_seq');
ALTER TABLE ONLY public.parts          ALTER COLUMN id SET DEFAULT nextval('public.parts_id_seq');
ALTER TABLE ONLY public.raporty        ALTER COLUMN id SET DEFAULT nextval('public.raporty_id_seq');
ALTER TABLE ONLY public.roles          ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq');
ALTER TABLE ONLY public.users          ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq');
ALTER TABLE ONLY public.zgloszenie     ALTER COLUMN id SET DEFAULT nextval('public.zgloszenie_id_seq');

-- 5. Primary Keys / Unique / Checks already inline except those below
ALTER TABLE ONLY public.czesci_magazyn ADD CONSTRAINT czesci_magazyn_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.dzialy         ADD CONSTRAINT dzialy_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.harmonogramy   ADD CONSTRAINT harmonogramy_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.maszyny        ADD CONSTRAINT maszyny_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.osoby          ADD CONSTRAINT osoby_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.part_usages    ADD CONSTRAINT part_usages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.parts          ADD CONSTRAINT parts_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.raporty        ADD CONSTRAINT raporty_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.roles          ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.users          ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.user_roles     ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);
ALTER TABLE ONLY public.zgloszenie     ADD CONSTRAINT zgloszenie_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users  ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);
ALTER TABLE ONLY public.dzialy ADD CONSTRAINT uk_r4x0m22ica7ypr5gnbcpdmb3m UNIQUE (nazwa);
ALTER TABLE ONLY public.roles  ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);

-- 6. Foreign Keys
ALTER TABLE ONLY public.harmonogramy ADD CONSTRAINT fk5rhnw24mwf14g04ok0j8suorr FOREIGN KEY (maszyna_id) REFERENCES public.maszyny(id);
ALTER TABLE ONLY public.maszyny      ADD CONSTRAINT fk7bb1dvm5ow59a7oq1en8wagpa FOREIGN KEY (dzial_id) REFERENCES public.dzialy(id);
ALTER TABLE ONLY public.raporty      ADD CONSTRAINT fk7cp4xeuuxgld54w7m4w8xmtbb FOREIGN KEY (maszyna_id) REFERENCES public.maszyny(id);
ALTER TABLE ONLY public.harmonogramy ADD CONSTRAINT fkcv5h81sjp4qeu812ss08yrca8 FOREIGN KEY (osoba_id)   REFERENCES public.osoby(id);
ALTER TABLE ONLY public.part_usages  ADD CONSTRAINT fke718wanexa1inob61ev4yx4sx FOREIGN KEY (part_id)   REFERENCES public.parts(id);
ALTER TABLE ONLY public.zgloszenie   ADD CONSTRAINT fkebvy8lgtgxgu1klfjbvyetmlc FOREIGN KEY (autor_id)  REFERENCES public.users(id);
ALTER TABLE ONLY public.part_usages  ADD CONSTRAINT fkevaf4h2vny6h7py09iwdwcdct FOREIGN KEY (raport_id) REFERENCES public.raporty(id);
ALTER TABLE ONLY public.user_roles   ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id)   REFERENCES public.roles(id);
ALTER TABLE ONLY public.user_roles   ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id)   REFERENCES public.users(id);
ALTER TABLE ONLY public.zgloszenie   ADD CONSTRAINT fkknomyoggc447smuxq3oypry1x FOREIGN KEY (dzial_id)  REFERENCES public.dzialy(id);
ALTER TABLE ONLY public.raporty      ADD CONSTRAINT fkofxcge0mo5c3m87xwargw79aw FOREIGN KEY (osoba_id)  REFERENCES public.osoby(id);

-- Optional (future): create indexes on FK columns for performance (leave for V2)
-- Example:
-- CREATE INDEX idx_harmonogramy_maszyna ON public.harmonogramy(maszyna_id);
-- CREATE INDEX idx_harmonogramy_osoba   ON public.harmonogramy(osoba_id);
-- etc.