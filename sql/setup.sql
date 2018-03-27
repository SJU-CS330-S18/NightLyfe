DROP TABLE friendgroups;
DROP TABLE specials;
DROP TABLE plans;
DROP TABLE friends;
DROP TABLE reviews;
DROP TABLE businesses;
DROP TABLE users;

CREATE TABLE users (username VARCHAR(20) PRIMARY KEY, password VARCHAR(20), type INT, name VARCHAR(30));
CREATE TABLE businesses (name VARCHAR(20) PRIMARY KEY, owner VARCHAR(20) REFERENCES users(username), city VARCHAR(20), address VARCHAR(100), latitude FLOAT(9), longitude FLOAT(9));
CREATE TABLE reviews (username VARCHAR(20) REFERENCES users(username), business VARCHAR(20) REFERENCES businesses(name), commenttext VARCHAR2(2000));
CREATE TABLE friends (user1 VARCHAR(20) REFERENCES users(username), user2 VARCHAR(20) REFERENCES users(username), status INT);
CREATE TABLE plans (who VARCHAR(20), business VARCHAR(20) REFERENCES businesses(name), plantime DATE);
CREATE TABLE specials (business VARCHAR(20) REFERENCES businesses(name), special VARCHAR2(2000), starttime DATE, endtime DATE);
CREATE TABLE friendgroups (username VARCHAR(20) REFERENCES users(username), groupnumber INT);

INSERT INTO users VALUES ('admin1', 'pass', 3, 'Admin One');
INSERT INTO users VALUES ('admin2', 'pass', 3, 'Admin Two');
INSERT INTO users VALUES ('owner1', 'pass', 2, 'Owner One');
INSERT INTO users VALUES ('user1', 'pass', 1, 'User One');
INSERT INTO users VALUES ('user2', 'pass', 1, 'User Two');
INSERT INTO users VALUES ('user3', 'pass', 1, 'User Three');

INSERT INTO businesses VALUES ('sals', 'owner1', 'saint joseph', '109 W Minnesota St, St Joseph, MN 56374', 45.564497, -94.320641);