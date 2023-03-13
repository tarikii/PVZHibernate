CREATE TABLE weapons
(
  id_weapon serial NOT NULL,
  name character varying(3000) NOT NULL,
  damage integer NOT NULL,
  CONSTRAINT pk_weapons PRIMARY KEY (id_weapon)
);



CREATE TABLE character_type
(
  id_character_type serial NOT NULL,
  name_type character varying(3000) NOT NULL,
  CONSTRAINT pk_characters_types PRIMARY KEY (id_character_type)
);


CREATE TABLE characters
(
  id_character serial NOT NULL,
  id_character_type integer,
  id_weapon integer NOT NULL,
  name character varying(3000) NOT NULL,
  image character varying(3000) NOT NULL ,
  health character varying(3000) NOT NULL,
  variant character varying(3000) NOT NULL,
  abilities character varying(3000) NOT NULL,
  FPSClass character varying(3000) NOT NULL,
  CONSTRAINT pk_characters PRIMARY KEY (id_character),
  CONSTRAINT fk_characters_types FOREIGN KEY (id_character_type)
      REFERENCES character_type (id_character_type) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_characters_weapons FOREIGN KEY (id_weapon)
      REFERENCES weapons (id_weapon) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

