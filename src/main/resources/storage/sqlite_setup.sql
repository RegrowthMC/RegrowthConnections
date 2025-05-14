CREATE TABLE IF NOT EXISTS connections_users
(
    uuid BINARY(128) NOT NULL,
    username TEXT,
    incoming_requests TEXT,
    outgoing_requests TEXT,
    connections TEXT,
    PRIMARY KEY (uuid)
);