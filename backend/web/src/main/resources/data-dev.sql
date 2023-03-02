INSERT INTO artists (id, name, biography) VALUES (1, 'Depeche Mode', 'The best band in the world');

INSERT INTO albums (id, title, cover_url, release_date)
    VALUES (1, 'Black Celebration', 'https://i.scdn.co/image/ab67616d0000b273878ae6fc96ee5954168838fc', '1986-03-17'),
           (2, 'Music For The Masses', 'https://i.scdn.co/image/ab67616d00001e025c2d57aa294b71136bcc510d', '1987-09-28');

INSERT INTO artists_albums (artist_id, album_id)
    VALUES (1, 1),
           (1, 2);

INSERT INTO songs (album_id, id, title, length, number_of_plays)
    VALUES (1, 1, 'Black Celebration', 295, 6975821),
           (1, 2, 'Fly On The Windscreen (Final)', 318, 6975822),
           (1, 3, 'A Question Of Lust', 260, 6975823),
           (1, 4, 'Sometimes', 113, 6975824),
           (1, 5, 'It Doesn''t Matter Two', 170, 6975825),
           (1, 6, 'A Question Of Time', 250, 6975826),
           (1, 7, 'Stripped', 256, 6975827),
           (1, 8, 'Here Is The House', 255, 6975828),
           (1, 9, 'World Full Of Nothing', 170, 6975829),
           (1, 10, 'Dressed In Black', 152, 6975830),
           (1, 11, 'New Dress', 222, 6975831),
           (2, 12, 'Never Let Me Down Again', 287, 6975832),
           (2, 13, 'The Things You Said', 242, 6975833),
           (2, 14, 'Strangelove', 296, 6975834),
           (2, 15, 'Sacred', 287, 6975835),
           (2, 16, 'Little 15', 258, 6975836),
           (2, 17, 'Behind The Wheel', 318, 6975837),
           (2, 18, 'I Want You Now', 224, 6975838),
           (2, 19, 'To Have And To Hold', 171, 6975839),
           (2, 20, 'Nothing', 258, 6975840),
           (2, 21, 'Pimpf', 300, 6975841);

INSERT INTO genres (id, name) VALUES (1, 'Synth-pop');

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
           (10, 1),
           (11, 1),
           (12, 1),
           (13, 1),
           (14, 1),
           (15, 1),
           (16, 1),
           (17, 1),
           (18, 1),
           (19, 1),
           (20, 1),
           (21, 1);
