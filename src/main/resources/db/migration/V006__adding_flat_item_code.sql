CREATE SEQUENCE item_code;

ALTER TABLE item
    ADD COLUMN item_code INT;

UPDATE item SET item_code = NEXTVAL('item_code')
    WHERE item_id in ( SELECT item_id FROM flat_item WHERE up_to_date = true );

UPDATE item SET item_code = (
        SELECT item_code
        FROM item current_item
        WHERE   item.item_name = current_item.item_name
                AND current_item.item_code is not null
        ORDER BY item_code
    LIMIT 1);

ALTER TABLE item
    ALTER COLUMN item_code SET DEFAULT NEXTVAL('item_code');