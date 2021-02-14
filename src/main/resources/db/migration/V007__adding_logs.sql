CREATE TABLE log(
    log_id SERIAL NOT NULL PRIMARY KEY,
    message VARCHAR(256),
    log_type VARCHAR(20) NOT NULL,
    id_trousseau_user INT NOT NULL,
    register_date TIMESTAMP,
    CONSTRAINT FK_Log_Trousseau_user
        FOREIGN KEY (id_trousseau_user)
        REFERENCES trousseau_user(id_trousseau_user)
);


CREATE TABLE trousseau_log(
    trousseau_log_id INT NOT NULL,
    trousseau_id    INT NOT NULL,
    status_attributed VARCHAR(50) NOT NULL,
    CONSTRAINT FK_Log_x_trousseau_Log
        FOREIGN KEY (trousseau_log_id)
        REFERENCES log(log_id),
    CONSTRAINT FK_Log_x_trousseau_Trousseau
        FOREIGN KEY (trousseau_id)
        REFERENCES trousseau_user(id_trousseau_user)
);