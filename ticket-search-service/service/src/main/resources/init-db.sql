
DROP TABLE Tickets;
CREATE TABLE Tickets (
                             id UUID PRIMARY KEY,
                             routeId UUID NOT NULL,
                             fromLocation VARCHAR(255) NOT NULL,
                             toLocation VARCHAR(255) NOT NULL,
                             departure TIMESTAMP NOT NULL,
                             arrival TIMESTAMP NOT NULL
    );
