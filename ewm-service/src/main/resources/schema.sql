DROP TABLE IF EXISTS participation_requests CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;


CREATE TABLE users(
    id INTEGER NOT NULL  GENERATED ALWAYS AS IDENTITY  PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE categories(
    id INTEGER NOT NULL  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE locations(
    id INTEGER NOT NULL  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL
);

CREATE TABLE events(
    annotation VARCHAR(2000) NOT NULL,
    category_id INTEGER REFERENCES categories(id) ON DELETE CASCADE,
    confirmed INTEGER,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(20),
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    id INTEGER NOT NULL  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    initiator_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    location_id INTEGER REFERENCES locations(id) ON DELETE CASCADE,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER,
    published TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state VARCHAR(9),
    title VARCHAR(120),
    views INTEGER
);

CREATE TABLE participation_requests(
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id INTEGER REFERENCES events(id) ON DELETE CASCADE,
    id INTEGER NOT NULL  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(9)
);

