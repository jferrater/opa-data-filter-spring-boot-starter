DROP TABLE IF EXISTS pets;
CREATE TABLE pets
(
    id int NOT NULL,
    name varchar(250),
    owner varchar(250),
    veterinarian varchar(250),
    clinic varchar(50)
);

INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(1, 'fluffy', 'alice', 'alice', 'SOMA');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(2, 'browny', 'bob', 'alice', 'SOMA');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(3, 'chopper', 'peter', 'alice', 'SOMA');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(4, 'soba', 'jolly', 'alice', 'SOMA');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(5, 'samsam', 'joffry', 'wella', 'VETE');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(6, 'chiko', 'mongkoy', 'wella', 'VETE');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(7, 'hatchi', 'alvin', 'wella', 'VETE');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(8, 'kaikai', 'joy', 'wella', 'VETE');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(9, 'nicole', 'justin', 'wella', 'VETE');
INSERT INTO pets (id, name, owner, veterinarian, clinic) VALUES(10, 'whitey', 'debby', 'alice', 'SOMA');
