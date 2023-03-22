INSERT INTO artists (id, name, biography) VALUES (1, 'Andrew Howes', '');

INSERT INTO albums (id, title, cover_url, release_date)
    VALUES (1, 'Gubernator', 'https://freemusicarchive.org/image/?file=images%2Falbums%2FAndrew_Howes_-_Gubernator_-_2015031293329764.jpg&width=290&height=290&type=album', '2015-03-18');

INSERT INTO artists_albums (artist_id, album_id)
    VALUES (1, 1);

INSERT INTO songs (album_id, id, title, data, length, number_of_plays)
    VALUES (1, 1, 'Trebuchet', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/01-Trebuchet.mp3'), 312, 6975821),
           (1, 2, 'Por-Loosh-Ka-Por-Lay', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/02-Por-Loosh-Ka-Por-Lay.mp3'), 250, 6975821),
           (1, 3, 'Snow Pony', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/03-Snow-Pony.mp3'), 287, 6975821),
           (1, 4, 'A Change Of Food', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/04-A-Change-Of-Food.mp3'), 200, 6975821),
           (1, 5, 'A Change Of The Guard', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/05-A-Change-Of-The-Guard.mp3'), 195, 6975821),
           (1, 6, 'Gubernator', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/06-Gubernator.mp3'), 238, 6975821),
           (1, 7, 'Dumb Luck', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/07-Dumb-Luck.mp3'), 280, 6975821),
           (1, 8, 'Helmsman', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/08-Helmsman.mp3'), 76, 6975821),
           (1, 9, 'Crimea', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/09-Crimea.mp3'), 180, 6975821),
           (1, 10, 'Traitors Gate', FILE_READ('classpath:/files/Andrew_Howes_Gubernator/10-Traitors-Gate.mp3'), 232, 6975821);

INSERT INTO genres (id, name) VALUES (1, 'Post-Rock');

INSERT INTO songs_genres (song_id, genre_id)
    VALUES (1, 1),
           (2, 1),
           (3, 1),
           (4, 1),
           (5, 1),
           (6, 1),
           (7, 1),
           (8, 1),
           (9, 1),
           (10, 1);
