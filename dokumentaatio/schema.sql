CREATE TABLE recipes (
	id INTEGER PRIMARY KEY,
	name TEXT,
	instructions TEXT
);

CREATE TABLE ingredients (
	id INTEGER PRIMARY KEY,
	name TEXT,
	unit TEXT,
);

CREATE TABLE ingredientsInRecipes (
	recipe_id INTEGER,
	ingredient_id INTEGER,
	quantity INTEGER
);

CREATE TABLE ingredientsInKitchen (
	ingredient_id INTEGER,
	quantity INTEGER
);
