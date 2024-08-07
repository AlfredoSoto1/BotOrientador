
create table if not exists Advancement (
    advid           serial primary key,
    name            varchar(50) not null,
    message_count   int,
    profanity_count int,
    activity_points int
);

create table if not exists Building (
    buildid serial primary key,
    gpin    varchar(512) not null,
    name    varchar(50)  not null
);

create table if not exists Contact (
    contid serial primary key,
    ext    varchar(10)  not null,
    web    varchar(512) not null,
    phone  varchar(15)  not null,
    media  varchar(100) not null,
    email  varchar(100) unique not null
);

create table if not exists Project (
    projecid    serial primary key,
    name        varchar(50)   not null,
    description varchar(1024) not null,
    fcontid     int references Contact(contid)
);

create table if not exists Organization (
    orgid       serial primary key,
    name        varchar(50)   not null,
    description varchar(1024) not null,
    fcontid     int references Contact(contid)
);

create table if not exists Team (
    teamid  serial primary key,
    name    varchar(50) not null,
    orgname varchar(50) not null
);

create table if not exists Room (
    roomid   serial primary key,
    code     varchar(10) not null,
    fbuildid int references Building(buildid)
);

create table if not exists Department (
    depid       serial primary key,
    name        varchar(50)   not null,
    description varchar(1024) not null,
    fbuildid    int references Building(buildid)
);

create table if not exists ServerOwnership (
    seoid     serial primary key,
    discserid bigint,
    fdepid    int references Department(depid)
);


create table if not exists Faculty (
    facid          serial primary key,
    name           varchar(50) not null,
    jobentitlement varchar(50) not null,
    fcontid        int references Contact(contid),
    fdepid         int references Department(depid)
);

create table if not exists Verification (
    verid  serial primary key,
    email  varchar(100) unique not null,
    fdepid int references Department(depid)
);

create table if not exists Orientador (
    orid   serial primary key,
    fname  varchar(50) not null,
    flname varchar(50) not null,
    mlname varchar(50) not null,
    fverid int references Verification(verid)
);

create table if not exists Prepa (
    prepaid serial primary key,
    fname   varchar(50) not null,
    flname  varchar(50) not null,
    mlname  varchar(50) not null,
    fverid  int references Verification(verid)
);

create table if not exists Program (
    progid     serial primary key,
    name       varchar(50) not null,
    curriculum varchar(50) not null,
    fdepid     int references Department(depid)
);

create table if not exists Member (
    memid   serial primary key,
    fteamid int references Team(teamid),
    fadvid  int references Advancement(advid),
    fverid  int references Verification(verid)
);

create table if not exists Role (
    roleid serial primary key,
    name   varchar(50) not null,
    fmemid int references Member(memid)
);

create table if not exists Service (
    servid      serial primary key,
    name        varchar(50)   not null,
    description varchar(1024) not null,
    fcontid     int references Contact(contid),
    fdepid      int references Department(depid)
);

create table if not exists Acesor (
    aceid       serial primary key,
    name        varchar(50)   not null,
    description varchar(1024) not null,
    fcontid     int references Contact(contid),
    fservid     int references Service(servid)
);

create table if not exists Consejero (
    conseid     serial primary key,
    name        varchar(50)   not null,
    description varchar(1024) not null,
    fcontid     int references Contact(contid),
    fservid     int references Service(servid)
);


-- drop table orientador cascade;
-- drop table prepa cascade;
-- drop table verification cascade;
-- drop table member cascade;
-- drop table team cascade;
-- drop table advancement cascade;
-- drop table acesor cascade;
-- drop table program cascade;
-- drop table serverownership cascade;
-- drop table department cascade;
-- drop table service cascade;
-- drop table consejero cascade;
-- drop table building cascade;
-- drop table contact cascade;
-- drop table faculty cascade;
-- drop table organization cascade;
-- drop table project cascade;
-- drop table room cascade;


-- insert into team (name, orgname, fadvid) values('aliens', 'Team-Made', 1);
