create table public.poshiv
(
    kod_marh  numeric(8),
    kod_izd   numeric(8),
    rzm       numeric(3),
    rst       numeric(3),
    srt       numeric(1),
    kol       numeric(10),
    pdrto     numeric(3),
    sdacha    numeric(10),
    kol_sdano numeric(10),
    data      timestamp(3),
    ncw       char(15),
    brigada   numeric(3),
    brigadir  char(20),
    sostav1   char(12),
    sostav2   char(15),
    sostav3   char(15),
    datevrkv  timestamp(3),
    uservrkv  char(15),
    kpodvrkv  char(5),
    datekrkv  timestamp(3),
    userkrkv  char(15),
    kpodkrkv  char(5),
    barcode   serial,
    marh1_id  numeric(10)
);

alter table public.poshiv
    owner to postgres;

create table public.nsi_sd
(
    sot        numeric(3),
    rst        numeric(3),
    rzm        numeric(3),
    srt        numeric(1),
    cnp        numeric(8, 2),
    cno        numeric(18, 6),
    cnr        numeric(8, 2),
    cnv        numeric(8, 2),
    not1       char(15),
    nds        numeric(2),
    vzr        numeric(2),
    ean        char(13),
    kod        numeric(8),
    kod1       serial,
    rzm_print  char(27),
    preiscur   char(30),
    datevrkv   timestamp(3),
    uservrkv   char(15),
    kpodvrkv   char(5),
    datekrkv   timestamp(3),
    userkrkv   char(15),
    kpodkrkv   char(5),
    massa      numeric(8, 6),
    rzm_pr1    char(10),
    rzm_pr2    char(10),
    rzm_pr3    char(10),
    d_preiscur timestamp(3)
);

alter table public.nsi_sd
    owner to postgres;

create table public.nsi_kld
(
    sar       numeric(8),
    nar       char(10),
    ngpr      char(45),
    brend     char(25),
    komplekt  char(25),
    sc        numeric(4),
    kkr       numeric(3),
    sgpr      numeric(2),
    fas       numeric(6),
    narp      numeric(10),
    ptk       numeric(3),
    kod       numeric(8),
    prim      char(50),
    datevrkv  timestamp(3),
    uservrkv  char(15),
    kpodvrkv  char(5),
    datekrkv  timestamp(3),
    userkrkv  char(15),
    kpodkrkv  char(5),
    rzm_naim1 char(20),
    rzm_naim2 char(20),
    rzm_naim3 char(20)
);

alter table public.nsi_kld
    owner to postgres;



-- ----------------------------------------

