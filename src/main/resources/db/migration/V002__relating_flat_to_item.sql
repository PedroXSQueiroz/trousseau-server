DELETE FROM trousseau_item;
DELETE FROM item;
DELETE FROM trousseau;

CREATE TABLE flat_item(
	flat_item_id SERIAL NOT NULL PRIMARY KEY,
	flat_id INT NOT NULL,
	CONSTRAINT FK_Flat_x_item_Flat
		FOREIGN KEY (flat_id)
		REFERENCES flat(flat_id),
	item_id INT NOT NULL,
	CONSTRAINT FK_Flat_x_item_Item
		FOREIGN KEY (item_id)
		REFERENCES item(item_id),
	quantity INT NOT NULL 
);
	