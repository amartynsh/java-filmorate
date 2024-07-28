INSERT INTO genre (name) VALUES ('Комедия');
INSERT INTO genre (name) VALUES ('Драма');
INSERT INTO genre (name) VALUES ('Мультфильм');
INSERT INTO genre (name) VALUES ('Триллер');
INSERT INTO genre (name) VALUES ('Документальный');
INSERT INTO genre (name) VALUES ('Боевик');

INSERT INTO mpa (name) VALUES ('G');
INSERT INTO mpa (name) VALUES ('PG');
INSERT INTO mpa (name) VALUES ('PG-13');
INSERT INTO mpa (name) VALUES ('R');
INSERT INTO mpa (name) VALUES ('NC-17');

INSERT INTO users(email, login, name, birthday) VALUES
('testuser1@gmail.xxx', 'login1', 'user1', '1900-01-01');
INSERT INTO users(email, login, name, birthday) VALUES
('testuser2@gmail.xxx', 'login2', 'user2', '1901-02-02');
INSERT INTO users(email, login, name, birthday) VALUES
('testuser3@gmail.xxx', 'login3', 'user3', '1902-03-03');
INSERT INTO users(email, login, name, birthday) VALUES
('testuser4@gmail.xxx', 'login4', 'user4', '1903-04-04');
INSERT INTO users(email, login, name, birthday) VALUES
('testuser5@gmail.xxx', 'login5', 'user5', '1904-05-05');

INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES
('film1', 'film1_test', '1900-01-01', 60, 1);
INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES
('film2', 'film2test', '2000-01-01', 70, 2);
INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES
('film3', 'testfilm3', '3000-01-01', 80, 3);

INSERT INTO films_genre_mapping(film_id, genre_id) VALUES 
(1, 1);
INSERT INTO films_genre_mapping(film_id, genre_id) VALUES
(1, 2);
INSERT INTO films_genre_mapping(film_id, genre_id) VALUES
(1, 3);

INSERT INTO films_genre_mapping(film_id, genre_id) VALUES
(2, 2);
INSERT INTO films_genre_mapping(film_id, genre_id) VALUES
(2, 3);

INSERT INTO films_genre_mapping(film_id, genre_id) VALUES
(3, 3);
INSERT INTO films_genre_mapping(film_id, genre_id) VALUES
(3, 4);

INSERT INTO friends(user_id, friend_id) VALUES
(1, 2);
INSERT INTO friends(user_id, friend_id) VALUES
(2, 3);
INSERT INTO friends(user_id, friend_id) VALUES
(2, 4);
INSERT INTO friends(user_id, friend_id) VALUES
(3, 1);
INSERT INTO friends(user_id, friend_id) VALUES
(3, 5);
INSERT INTO friends(user_id, friend_id) VALUES
(4, 1);

INSERT INTO likes(user_id, film_id) VALUES
(1, 1);
INSERT INTO likes(user_id, film_id) VALUES
(2, 2);
INSERT INTO likes(user_id, film_id) VALUES
(2, 3);
INSERT INTO likes(user_id, film_id) VALUES
(3, 3);
INSERT INTO likes(user_id, film_id) VALUES
(3, 1);
INSERT INTO likes(user_id, film_id) VALUES
(3, 2);
INSERT INTO likes(user_id, film_id) VALUES
(4, 1);
INSERT INTO likes(user_id, film_id) VALUES
(4, 3);
INSERT INTO likes(user_id, film_id) VALUES
(5, 2);