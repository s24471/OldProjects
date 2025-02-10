CREATE TABLE Autorzy (
    autorID  int  NOT NULL auto_increment,
    imię  varchar(255)  NOT NULL,
    nazwisko  varchar(255)  NOT NULL,
    rokUrodzenia  int  NOT NULL,
    constraint Autorzy_pk PRIMARY KEY (autorID)
);

CREATE TABLE Kategorie (
    kategoriaID  int  NOT NULL auto_increment,
    nazwaKategorii  varchar(255)  NOT NULL,
    rokUtworzenia int  not NULL,
    CONSTRAINT Kategorie_pk PRIMARY KEY (kategoriaID )
);

CREATE TABLE Książki (
    książkaID  int  NOT NULL auto_increment,
    autorID  int  NOT NULL,
    tytuł varchar(255)  NOT NULL,
    rokWydania  int  NOT NULL,
    kategoriaID  int  NOT NULL,
    CONSTRAINT Książki_pk PRIMARY KEY (książkaID )
);

CREATE TABLE Listy_Książki (
    id int  NOT NULL auto_increment,
    książkaID int  NOT NULL,
    Użytkownicy_użytkownikID  int  NOT NULL,
    CONSTRAINT Listy_Książki_pk PRIMARY KEY (id)
);

CREATE TABLE Użytkownicy (
    użytkownikID  int  NOT NULL auto_increment,
    login varchar(255)  NOT NULL,
    hasło varchar(255)  NOT NULL,
    email varchar(255)  NOT NULL,
    imie varchar(255)  NOT NULL,
    nazwisko varchar(255)  NOT NULL,
    admin boolean not null,
    CONSTRAINT Użytkownicy_pk PRIMARY KEY (użytkownikID )
);

ALTER TABLE Książki
ADD CONSTRAINT FK_Książki_Autorzy FOREIGN KEY (autorID) REFERENCES Autorzy(autorID);

ALTER TABLE Książki
ADD CONSTRAINT FK_Książki_Kategorie FOREIGN KEY (kategoriaID) REFERENCES Kategorie(kategoriaID);

ALTER TABLE Listy_Książki
ADD CONSTRAINT FK_Listy_Książki_Książki FOREIGN KEY (książkaID) REFERENCES Książki(książkaID);

ALTER TABLE Listy_Książki
ADD CONSTRAINT FK_Listy_Książki_Użytkownicy FOREIGN KEY (Użytkownicy_użytkownikID) REFERENCES Użytkownicy(użytkownikID);


INSERT INTO Kategorie (nazwaKategorii, rokUtworzenia) VALUES
    ('Fantastyka', 2000),
    ('Romans', 1995),
    ('Kryminał', 2008),
    ('Naukowa', 2010),
    ('Przygodowa', 2005),
    ('Dramat', 2015);

INSERT INTO Autorzy (imię, nazwisko, rokUrodzenia) VALUES
    ('Jan', 'Kowalski', 1980),
    ('Anna', 'Nowak', 1992),
    ('Marek', 'Dąbrowski', 1975),
    ('Ewa', 'Wójcik', 1988);

INSERT INTO Książki (autorID, tytuł, rokWydania, kategoriaID) VALUES
    (1, 'Władca Pierścieni', 2001, 5),
    (2, 'Miłość i nienawiść', 2010, 2),
    (3, 'Zabójstwo na Orient Expressie', 2005, 3),
    (4, 'Równania różniczkowe', 2012, 4),
    (1, 'Harry Potter i Kamień Filozoficzny', 1997, 5),
    (2, 'Duma i uprzedzenie', 1813, 2),
    (3, 'Sklepik z marzeniami', 1993, 6),
    (4, 'Programowanie w Pythonie', 2018, 4),
    (1, 'Hobbit', 1937, 5),
    (2, 'Romeo i Julia', 1597, 2),
    (3, 'Złodziejka książek', 2005, 1),
    (4, 'Matematyka dla początkujących', 2010, 4);

INSERT INTO Użytkownicy (login, hasło, email, imie, nazwisko, admin) VALUES
    ('admin', '$2b$10$2vdQF2SdhDFVJKywzuP3Seu/YohaS7.KUEYUMuQMJbCGCl7jtslL.', 'admin@admin.com', 'Admin', 'Adminowski', 1),
    ('user', '$2b$10$Kiw0AtFbV7tjp/IL5qVfjO3Ysl/slfaWJnVpi8K3ebCM40UzfqL3m', 'user@user.com', 'userF', 'Goclawski', 0);


