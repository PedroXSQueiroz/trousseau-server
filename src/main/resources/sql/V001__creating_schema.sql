CREATE TABLE flat(
	flat_id SERIAL NOT NULL PRIMARY KEY,
	floor INT NOT NULL,
	unity INT NOT NULL
);

CREATE TABLE item(
	item_id SERIAL NOT NULL PRIMARY KEY,
	item_name VARCHAR(100) NOT NULL,
	item_value FLOAT NOT NULL
);

CREATE TABLE trousseau(
	trousseau_id SERIAL NOT NULL PRIMARY KEY,
	register_date TIMESTAMP NOT NULL DEFAULT NOW(),
	trousseau_status VARCHAR(50),
	flat_id INT NOT NULL,
	CONSTRAINT FK_Trousseau_Flat
		FOREIGN KEY (flat_id)
		REFERENCES flat(flat_id)
);

CREATE TABLE trousseau_item(
	trousseau_item_id SERIAL NOT NULL PRIMARY KEY,
	trousseau_id INT,
	CONSTRAINT FK_Trousseau_item_Trousseau
		FOREIGN KEY(trousseau_id)
		REFERENCES trousseau(trousseau_id),
	item_id INT,
	CONSTRAINT FK_Trousseau_item_Item
		FOREIGN KEY (item_id)
		REFERENCES item(item_id),
	quantity INT NOT NULL,
	product_type VARCHAR(50)
)