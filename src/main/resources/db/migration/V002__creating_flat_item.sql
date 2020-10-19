CREATE TABLE flat_item(
    flat_item_id SERIAL NOT NULL PRIMARY KEY,
    item_id INT,
    CONSTRAINT FK_Flat_item_Item
        FOREIGN KEY(item_id)
        REFERENCES item(item_id),
    flat_id INT,
    CONSTRAINT Fk_Flat_item_Flat
        FOREIGN KEY(flat_id)
        REFERENCES flat(flat_id),
    quantity INT NOT NULL,
    up_to_date BOOLEAN NOT NULL
);