-- 1. BIOSKOPI 
insert into bioskop(id, naziv, adresa)
values(nextval('bioskop_seq'), 'Arena Cineplex', 'Bulevar Mihajla Pupina 3, Novi Sad'),
	  (nextval('bioskop_seq'), 'Cineplexx Promenada', 'Oslobođenja 119, Novi Sad'),
	  (nextval('bioskop_seq'), 'Cinestar', 'Sentandrejski put 11, Novi Sad'),
      (nextval('bioskop_seq'), 'Cineplexx Ušće', 'Bulevar Mihajla Pupina 4, Beograd'),
	  (nextval('bioskop_seq'), 'Tuckwood Cineplex', 'Kneza Miloša 7, Beograd'),
	  (nextval('bioskop_seq'), 'Vilin Grad', 'Obrenovićeva 38, Niš');

-- 2. FILMOVI
insert into film(id, naziv, zanr, trajanje, recenzija)
values(nextval('film_seq'), 'Dune: Part Two', 'Sci-Fi', 166, 9),
	  (nextval('film_seq'), 'Oppenheimer', 'Drama', 180, 10),
	  (nextval('film_seq'), 'Poor Things', 'Comedy', 141, 8),
      (nextval('film_seq'), 'The Matrix', 'Sci-Fi', 136, 9),
      (nextval('film_seq'), 'Gladiator', 'Action', 155, 8),
      (nextval('film_seq'), 'Joker', 'Drama', 122, 8),
      (nextval('film_seq'), 'Toy Story', 'Animation', 81, 8),
      (nextval('film_seq'), 'The Lord of the Rings', 'Fantasy', 178, 9),
      (nextval('film_seq'), 'Deadpool & Wolverine', 'Action', 127, 8),
      (nextval('film_seq'), 'The Batman', 'Action', 175, 8),
	  (nextval('film_seq'), 'Inception', 'Sci-Fi', 148, 9),
	  (nextval('film_seq'), 'Interstellar', 'Sci-Fi', 169, 10),
	  (nextval('film_seq'), 'Barbie', 'Comedy', 114, 7),
	  (nextval('film_seq'), 'Spider-Man', 'Animation', 140, 9);

-- 3. SALE 
insert into sala(id, kapacitet, broj_redova, id_bioskop)
values(nextval('sala_seq'), 150, 10, 1), -- Sala 1, Arena
	  (nextval('sala_seq'), 200, 12, 1), -- Sala 2, Arena
	  (nextval('sala_seq'), 100, 8, 2),  -- Sala 3, Promenada
      (nextval('sala_seq'), 250, 15, 2), -- Sala 4, Promenada
	  (nextval('sala_seq'), 120, 8, 4),  -- Sala 5, Ušće
	  (nextval('sala_seq'), 80, 5, 5),   -- Sala 6, Tuckwood
	  (nextval('sala_seq'), 300, 20, 6), -- Sala 7, Vilin Grad
      (nextval('sala_seq'), 50, 5, 1);   -- Sala 8, VIP Arena 

-- 4. REZERVACIJE
insert into rezervacija(id, broj_osoba, cena_karte, datum, placeno, id_film, id_sala)
values(nextval('rezervacija_seq'), 2, 1200, to_date('25.03.2026.', 'dd.mm.yyyy.'), TRUE, 1, 1),
      (nextval('rezervacija_seq'), 4, 2400, to_date('26.03.2026.', 'dd.mm.yyyy.'), FALSE, 2, 2),
      (nextval('rezervacija_seq'), 1, 600, to_date('27.03.2026.', 'dd.mm.yyyy.'), TRUE, 3, 3),
      (nextval('rezervacija_seq'), 2, 1400, to_date('28.03.2026.', 'dd.mm.yyyy.'), TRUE, 4, 4),
      (nextval('rezervacija_seq'), 3, 2100, to_date('29.03.2026.', 'dd.mm.yyyy.'), FALSE, 5, 5),
      (nextval('rezervacija_seq'), 5, 3000, to_date('30.03.2026.', 'dd.mm.yyyy.'), TRUE, 6, 6),
      (nextval('rezervacija_seq'), 2, 1200, to_date('01.04.2026.', 'dd.mm.yyyy.'), TRUE, 7, 7),
      (nextval('rezervacija_seq'), 10, 8000, to_date('02.04.2026.', 'dd.mm.yyyy.'), FALSE, 8, 8),
      (nextval('rezervacija_seq'), 1, 800, to_date('05.04.2026.', 'dd.mm.yyyy.'), TRUE, 1, 5),
      (nextval('rezervacija_seq'), 2, 1200, to_date('10.04.2026.', 'dd.mm.yyyy.'), TRUE, 9, 1),
      (nextval('rezervacija_seq'), 4, 2800, to_date('12.04.2026.', 'dd.mm.yyyy.'), FALSE, 10, 2),
      (nextval('rezervacija_seq'), 1, 600, to_date('15.04.2026.', 'dd.mm.yyyy.'), TRUE, 11, 3),
      (nextval('rezervacija_seq'), 6, 3600, to_date('18.04.2026.', 'dd.mm.yyyy.'), TRUE, 12, 4),
      (nextval('rezervacija_seq'), 2, 1400, to_date('20.04.2026.', 'dd.mm.yyyy.'), FALSE, 13, 5),
      (nextval('rezervacija_seq'), 3, 2100, to_date('25.04.2026.', 'dd.mm.yyyy.'), TRUE, 14, 6),
      (nextval('rezervacija_seq'), 2, 1200, to_date('01.05.2026.', 'dd.mm.yyyy.'), TRUE, 9, 8),
      (nextval('rezervacija_seq'), 5, 3000, to_date('05.05.2026.', 'dd.mm.yyyy.'), FALSE, 2, 1),
      (nextval('rezervacija_seq'), 2, 1500, to_date('10.05.2026.', 'dd.mm.yyyy.'), TRUE, 3, 2),
      (nextval('rezervacija_seq'), 1, 750, to_date('12.05.2026.', 'dd.mm.yyyy.'), TRUE, 4, 3);