DROP Schema project CASCADE;

-- Schema 

CREATE schema project;

-- Entities

CREATE TABLE project.book (
    isbn 	numeric(13,0) PRIMARY KEY,
    title 	character varying(200),
    version 	numeric(2,0),
    num_pages 	numeric(4,0),
    price 	numeric(5,2),
    royalty 	numeric(4,2),
    stock 	integer
);

CREATE TABLE project.user (
    username 	character varying(16) PRIMARY KEY,
    password 	character varying(16),
    first_name 	character varying(10),
    last_name 	character varying(20),
    email 	character varying(30)
);

CREATE TABLE project.librarian (
    username 	character varying(16) REFERENCES project.user(username) ON DELETE CASCADE,
    salary 	numeric(8,2),

    CONSTRAINT librarian_pkey PRIMARY KEY (username)
);

CREATE TABLE project.address (
    add_id 	SERIAL NOT NULL PRIMARY KEY,
    street_num 	numeric(6,0),
    street_name character varying(20),
    apartment 	character varying(6),
    city 	character varying(20),
    province 	character varying(20),
    country 	character varying(20),
    postal_code character varying(6)
);

CREATE TABLE project.publisher (
    pub_name 	character varying(200) PRIMARY KEY,
    email_add 	character varying(30) UNIQUE,
    phone_num 	numeric(10,0) UNIQUE,
    bank_acc 	numeric(15,0) UNIQUE
);

CREATE TABLE project.genre (
    name 	character varying(150) PRIMARY KEY
);

CREATE TABLE project.author (
    auth_fn 	character varying(20) NOT NULL,
    auth_ln 	character varying(40) NOT NULL,

    CONSTRAINT author_pkey PRIMARY KEY (auth_fn, auth_ln)
);

CREATE TABLE project.basket (
    basket_id 	SERIAL NOT NULL PRIMARY KEY
);

CREATE TABLE project.order (
    order_num 	 	SERIAL NOT NULL PRIMARY KEY,
    tracking_num 	numeric(16,0) UNIQUE,
    date_placed 	timestamp without time zone,
    total_price 	numeric(9,2),
    status          	varchar(20)
);

-- Relations

CREATE TABLE project.writes (
    auth_fn 	character varying(20),
    auth_ln 	character varying(40),
    isbn    	numeric(13,0) REFERENCES project.book(isbn) ON DELETE CASCADE,

    CONSTRAINT writes_pkey PRIMARY KEY (auth_fn, auth_ln, isbn),
    CONSTRAINT writes_fkey FOREIGN KEY (auth_fn, auth_ln) REFERENCES project.author
);

CREATE TABLE project.publishes (
    isbn 	numeric(13,0) REFERENCES project.book(isbn) ON DELETE CASCADE,
    pub_name 	character varying(200) REFERENCES project.publisher NOT NULL,
    year 	numeric(4,0),

    CONSTRAINT publishes_pkey PRIMARY KEY (isbn)
);

CREATE TABLE project.hasgenre (
    name 	character varying(150) REFERENCES project.genre(name),
    isbn	numeric(13,0) REFERENCES project.book(isbn) ON DELETE CASCADE,

    CONSTRAINT	hasgenre_pkey PRIMARY KEY (name, isbn)

);

CREATE TABLE project.bask_item (
    basket_id 	SERIAL NOT NULL REFERENCES project.basket(basket_id),
    isbn 	numeric(13,0) REFERENCES project.book(isbn) ON DELETE CASCADE,
    quantity 	integer,

    CONSTRAINT bask_item_pkey PRIMARY KEY (basket_id, isbn)
);

CREATE TABLE project.checkout (
    basket_id 	SERIAL NOT NULL REFERENCES project.basket(basket_id),
    order_num 	SERIAL NOT NULL UNIQUE REFERENCES project.order(order_num),

    CONSTRAINT checkout_pkey PRIMARY KEY (basket_id)
);

CREATE TABLE project.bask_manage (
    username 	character varying(16) REFERENCES project.user(username) ON DELETE CASCADE,
    basket_id 	SERIAL NOT NULL REFERENCES project.basket(basket_id),

    CONSTRAINT bask_manage_pkey PRIMARY KEY (basket_id)
);

CREATE TABLE project.ordadd (
    order_num 	SERIAL REFERENCES project.order(order_num),
    add_id 	SERIAL REFERENCES project.address(add_id),
    isshipping  boolean NOT NULL,
    isbilling   boolean NOT NULL,

    CONSTRAINT	ordadd_pkey PRIMARY KEY (order_num, add_id),
    UNIQUE (order_num, isshipping, isbilling)

);

CREATE TABLE project.hasAdd (
    add_id 	SERIAL NOT NULL REFERENCES project.address(add_id),
    username 	character varying(16) NOT NULL REFERENCES project.user(username) ON DELETE CASCADE,
    isshipping 	boolean NOT NULL,

    CONSTRAINT 	hasadd_pkey PRIMARY KEY (add_id),
    UNIQUE (username, isshipping)
);

CREATE TABLE project.pubadd (
    add_id 	SERIAL NOT NULL UNIQUE REFERENCES project.address(add_id),
    pub_name 	character varying(200) REFERENCES project.publisher,

    CONSTRAINT 	pubadd_pkey PRIMARY KEY (pub_name)
);