DROP TABLE businesses;
DROP TABLE users;

CREATE TABLE users (username VARCHAR(20) PRIMARY KEY, password VARCHAR(20), type INT);
CREATE TABLE businesses (name VARCHAR(20) PRIMARY KEY, owner VARCHAR(20) REFERENCES users(username), city VARCHAR(20));

INSERT INTO users VALUES ('admin1', 'pass', 3);
INSERT INTO users VALUES ('admin2', 'pass', 3);
INSERT INTO users VALUES ('owner1', 'pass', 2);
INSERT INTO users VALUES ('user1', 'pass', 1);
INSERT INTO users VALUES ('user2', 'pass', 1);
INSERT INTO users VALUES ('user3', 'pass', 1);

INSERT INTO businesses VALUES ('sals', 'owner1', 'saint joseph');