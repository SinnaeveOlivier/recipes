CREATE TABLE recipe
(
    id                   UUID          NOT NULL CONSTRAINT pk_recipe PRIMARY KEY,
    name                 VARCHAR(255)  NOT NULL,
    description          VARCHAR(512)  NOT NULL,
    cooking_instructions VARCHAR(2048) NOT NULL,
    creation_date        TIMESTAMP    NOT NULL,
    last_updated_date    TIMESTAMP    NOT NULL
);

CREATE TABLE ingredient
(
    id                  UUID         NOT NULL CONSTRAINT pk_ingredient PRIMARY KEY,
    recipe_id           UUID         NOT NULL CONSTRAINT fk_ingredient_recipe REFERENCES recipe (id),
    name                VARCHAR(255) NOT NULL,
    quantity            VARCHAR(50) NOT NULL,
    creation_date       TIMESTAMP    NOT NULL,
    last_updated_date   TIMESTAMP    NOT NULL
);