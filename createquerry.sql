create table Turnier(
	ID int,
    Jahr int,
    anzahlTische int,
    primary key(ID)
    );
    
create table Spieler(
	ID int,
    Vorname varchar(30),
    Nachname varchar(30),
    primary key(ID)
    );
    
create table nehmenTeil(
    Turnier int,
    Spieler int,
    foreign key(Turnier) references Turnier(ID) ON DELETE CASCADE,
    foreign key(Spieler) references Spieler(ID)
    );
    
create table Gang(
	ID int,
    GangNR int,
    Turnier int,
    primary key(ID),
    foreign key(Turnier) references Turnier(ID) ON DELETE CASCADE
    );
    
create table Team(
	ID int,
    Punkte int,
    Spieler1 int,
    Spieler2 int,
    Gang int,
    primary key(ID),
    foreign key(Spieler1) references Spieler(ID),
    foreign key(Spieler2) references Spieler(ID),
    foreign key (Gang) references Gang(ID) ON DELETE CASCADE
    );
    
create table Spiel(
	ID int,
    TischNr int,
    Gang int,
    Punkte1 int,
    Punkte2 int,
    Team1 int,
    Team2 int,
    primary key(ID),
    foreign key(Gang) references Gang(ID) ON DELETE CASCADE,
    foreign key(Team1) references Team(ID),
    foreign key(Team2) references Team(ID)
    );
