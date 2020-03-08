DROP DATABASE IF EXISTS stocks;
CREATE DATABASE stocks;

CREATE TABLE stocks (
                        id integer NOT NULL,
                        description character varying(255),
                        percent_left numeric(19,2),
                        symbol character varying(255),
                        version bigint,
                        PRIMARY KEY (id)
);

CREATE TABLE fractions (
                          id integer NOT NULL,
                          percent numeric(5,2),
                          stock_id integer REFERENCES stocks(id),
                          user_id integer,
                          version bigint,
                          transaction_id bigint REFERENCES transactions(id) ON DELETE NO ACTION,
                          PRIMARY KEY (id)
    );

CREATE TABLE transactions (
                         id bigint NOT NULL,
                         amount double precision NOT NULL,
                         stock_id integer NOT NULL,
                         time_stamp timestamp without time zone,
                         transaction_type integer,
                         user_id integer NOT NULL,
                         PRIMARY KEY (id)
);

