CREATE TABLE recipes (
	id INTEGER PRIMARY KEY,
	name TEXT UNIQUE,
	instructions TEXT,
	visible BOOLEAN,
	tags TEXT
);

CREATE TABLE ingredients (
	id INTEGER PRIMARY KEY,
	name TEXT UNIQUE,
	unit TEXT
);

CREATE TABLE ingredientsInRecipes (
	recipe_id INTEGER REFERENCES recipes,
	ingredient_id INTEGER REFERENCES ingredients,
	quantity INTEGER
);

CREATE TABLE ingredientsInKitchen (
	ingredient_id INTEGER REFERENCES ingredients,
	quantity INTEGER
);
