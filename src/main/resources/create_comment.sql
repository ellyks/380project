CREATE TABLE comment (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    content VARCHAR(255) DEFAULT NULL,
    ticket_id INTEGER DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (ticket_id) REFERENCES ticket(id) 
);
