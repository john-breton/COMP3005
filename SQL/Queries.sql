/* Please note variable names are included as they are replaced with appropriate values within the application. */

-- UploadBookData.java Queries (Initializies the database with dummy data
/* Creates a user with the username 'postgres' that will be registered as an admin in the system. They are also assigned their own cart. */
INSERT INTO project.user values ('" + USER + "', '" + USER + "', 'Post', 'Gres', 'postgres@email.ca'); 								-- insert 'postgres' into the user table
INSERT INTO project.librarian values ('" + USER + "', 300.00);													-- insert 'postgres' into the librarian table, with a massive $300 salary
INSERT INTO project.bask_manage values ('" + USER + "', '1');													-- associate 'postgres' with the newly created basket with bask_id '1'

/* Get a random isbn of a book currently contained within the database to create dummy orders */
SELECT isbn FROM project.book ORDER BY random() limit 1;													-- get a random isbn from the book table
SELECT price FROM project.book WHERE isbn = '" + isbn + "';													-- get the price of the book associated with the isbn that was just retreived


-- DatabaseQueries.java Queries
-- Insertions
/* Insert a user into the database. Should a conflict occur, the application takes appropriate action and the program continues execution */
INSERT INTO project.user values ('" + username.toLowerCase() + "', '" + password + "', '" + first_name + "', '" + last_name + "', '" + email + "') ON CONFLICT (username) DO NOTHING;

/* Insert an order into the database. Also associates the order with the addresses supplied by the user. */
/* CASE 1: The user has the same shipping and billing address for the order */
INSERT INTO project.order values (nextval('project.order_order_num_seq'), '" + trackingNumber + "', '" + ts + "', '" + totalCost + "', 'Processing');
INSERT INTO project.ordadd values (currval('project.order_order_num_seq'), currval('project.address_add_id_seq'), true, true);					-- true, true indicates that the address is both the shipping and billing address
/* CASE 2: The user has a different shipping and billing address for th order */	
INSERT INTO project.order values (nextval('project.order_order_num_seq'), '" + trackingNumber + "', '" + ts + "', '" + totalCost + "', 'Processing');
INSERT INTO project.ordadd values (currval('project.order_order_num_seq'), currval('project.address_add_id_seq') - 1, true, false);				-- true, false indicates that the address is a shipping address
INSERT INTO project.ordadd values (currval('project.order_order_num_seq'), currval('project.address_add_id_seq'), false, true);					-- false, true indicates that the address is a billing address

/* Insert a checkout, which associates an order number with a cart of items that were purchased in said order */
INSERT INTO project.checkout VALUES ('" + basketID + "', '" + orderNum + "');											-- the order number before this is every run

/* Insert an address into the database provided values supplied by a user. */
INSERT into project.address " +
                    "values (nextval('project.address_add_id_seq'), " +
                    "'" + streetNum +
                    "', '" + streetName +
                    "', '" + apartment +
                    "', '" + city +
                    "', '" + province +
                    "', '" + country +
                    "', '" + postalCode +
                    "');

/* Insert an association between a user and an address, along with the type of address that is associated with the user. */
INSERT into project.hasadd values (currval('project.address_add_id_seq'), '" + username.toLowerCase() + "', '" + isShipping + "');				-- isShipping is true if the address being associated is a shipping address, 
																				-- false for billing

/* Insert an association between a publisher and an address. */
INSERT into project.pubadd values (currval('project.address_add_id_seq'), '" + pub_name + "');

/* Insert a publisher into the database. */
INSERT INTO project.publisher values ('" + name + "','" + email + "', '" + phoneNum + "', '" + bankAcc + "') ON CONFLICT (pub_name) DO NOTHING;			-- conflicts are handled application-side. Should a conflict occur execution continued

/* Insert an association between a book and a publisher that published said book */
INSERT INTO project.publishes values ('" + isbn + "', '" + name + "', '" + year + "');

/* Insert an association between a basket and a user */
INSERT into project.bask_manage values ('" + username + "', currval('project.basket_basket_id_seq'));

/* Insert an item into a basket */
INSERT into project.bask_item " +																-- '0' is the quantity, after the insertion it is immediately updated
                    "values ('" + cartID +
                    "', '" + isbn + "', 0);

/* Insert a librarian (admin) into the database. Should the user already exist in the table, just update their salary */
INSERT INTO project.librarian values('" + username + "','" + salary + "') ON CONFLICT (username) DO UPDATE SET salary = '" + salary + "';

/* Insert a genre into the database.
INSERT INTO project.genre " +
                        "VALUES ('" + s + "')" +
                        "ON CONFLICT (name) DO NOTHING;														-- conflicts are handled application-side. Should a conflict occur execution continued

/* Insert an association between a book and a genre. */
INSERT INTO project.hasgenre " +
                        "VALUES ('" + s + "', '" + isbn + "')" +
                        "ON CONFLICT (name, isbn) DO NOTHING;

/* Insert an author into the database */
INSERT INTO project.author " +
                            "VALUES ('" + names[0].trim() + "', '" + names[names.length - 1].trim() + "')" +
                            "ON CONFLICT (auth_fn, auth_ln) DO NOTHING;

/* Insert an association between an author and a book. */
INSERT INTO project.writes " +
                            "VALUES ('" + names[0].trim() + "', '" + names[names.length - 1].trim() + "', '" + isbn + "')" +
                            "ON CONFLICT (auth_fn, auth_ln, isbn) DO NOTHING;											-- conflicts are handled application-side. Should a conflict occur execution continued

/* Insert a book into the database. The application takes care of error checking the inputs to avoid insertion errors. */
INSERT INTO project.book values ('" + isbn + "','" + title + "','" + version + "','" + pageCount + "','" + price + "','" + royalty + "','" + stock + "');

-- Selections
/* Search for a user in both the user and librarian table with a specific username. */
SELECT * from project.user LEFT OUTER JOIN project.librarian USING (username) where username = '" + username.toLowerCase().trim() + "';

/* Search for an order with a specific order number. */
SELECT * FROM project.order WHERE order_num = '" + orderNumber + "';

/* Search for addresses associated with a user. */
SELECT * FROM project.address NATURAL JOIN project.hasadd WHERE username = '" + username.toLowerCase() + "';

/* Search for an author using both their first and last name. */
SELECT * FROM project.writes WHERE auth_fn = '" + names[0] + "' AND auth_ln = " + names[names.length - 1] + "';							-- here, names[0] contains the first name, and names[names.length - 1] contains
																				-- the last name. Users can enter a middle name, but this is ignored while searching

/* Search for a book that matches the isbn passed. Information retreived includes all book information, as well as the publisher information for the book. */
SELECT * FROM project.book natural join project.publishes WHERE isbn = '" + isbn + "';

/* Search for an author using only their first name. */
SELECT * FROM project.writes WHERE auth_fn = '" + names[0] + "';												-- here, names[0] contains the first name

/* Search for an author using only their last name. */
SELECT * FROM project.writes WHERE auth_ln = '" + names[0] + "';												-- here, names[0] contains the last name

/* Search for a genre that contains the pattern that was passed by the user. The less precise, the more results are returned. */
SELECT * FROM project.hasgenre WHERE name LIKE '%" + searchText + "%';												-- in postgresSQL, "%" is used to indicate that there can be any number of characters 
																				   before/after the pattern and it would still be a valid result

/* Search for a book via isbn or year. As isbns and years are numeric, they do not quality for wildcard searching. */
SELECT * FROM project.book natural join project.publishes WHERE '" + searchype + "' = '" + searchText + "';							-- the user is expected to enter the exact isbn/year they are searching for

/* Search for a book via the search type and search text provided by the user. Supports wildcard searching. */
SELECT * FROM project.book natural join project.publishes WHERE " + searchType + " LIKE '%" + searchText + "%';							-- this runs for book titles and publisher names. genres and author search also do
																				-- searching, but they are handled differently
/*  Search for an author that wrote the book that corresponds to the passed isbn. */
SELECT * FROM project.author natural join project.writes WHERE isbn = '" + isbn + "';

/* Search for a genre and that relates to the book that corresponds to the passed isbn. */
SELECT * FROM project.genre natural join project.hasgenre WHERE isbn = '" + isbn + "';

/* Return the most recently associated basket that corresponds to the passed username. Used primarilly after a user has placed an order and a new empty basket is assigned. */
SELECT max(basket_id) FROM project.bask_manage WHERE username = '" + username + "';										-- baskets are added via a sequential identifier, taking the max retrieves the most
																				   recently inserted cart.

/* Return all basket items from the basket that was most recently assigned to a username (their active cart). */
SELECT * FROM project.bask_item WHERE basket_id = (\n" +
                    "\tSELECT max(basket_id) FROM project.bask_manage WHERE project.bask_manage.username = '" + username + "';	 

/* Search for an order based on the passed tracking number. Used to retrieve the order number. */
SELECT * FROM project.order WHERE tracking_num = '" + trackingNumber + "';	

/* Search for a basket item based on the passed basket id. Used to look up items in completed orders. */
SELECT * FROM project.bask_item WHERE basket_id = '" + basketID + "';	

/* Retrieve the email address of a publisher that published a book with the corresponding isbn. Used to email publishers when a book's stock drops below 0. */
SELECT pub_name, email_add FROM project.publisher NATURAL JOIN project.publishes WHERE isbn = '" + isbn + "';									

/* Search for a book based on the passed isbn. Used to ensure such a book does not already exist in the database. */
SELECT * FROM project.book WHERE isbn = '" + isbn + "';

/* Search for a publisher based on the passed publisher name. Used to ensure such a book does not already exist in the database, */
SELECT * FROM project.publisher WHERE pub_name = '" + publisher + "';

/* Execute the function order_more_books() once a books stock has fallen below 10. */
SELECT order_more_books('" +  result.getString("isbn") + "');													-- See Functions.SQL for further implementation.
 
-- Deletions
/* Delete all the genres associated with a book (used when updating the book). */
DELETE FROM project.hasgenre WHERE isbn = '" + isbn + "';

/* Delete all the authors associated with a book (used when updating the book). */
DELETE FROM project.writes WHERE isbn = '" + isbn + "';

/* Delete an entity from the schema
DELETE FROM project.'" + from + "' WHERE '" + where + "' = '" + identifier + "';


-- Updates
/* Update all books stock depending on the quantity of each books (identified by isbn) that is contained within a user's basekt. */
UPDATE project.book SET stock = stock - '" + '" + result.getString("quantity") + "' + " WHERE isbn = '" +  result.getString("isbn") + "';	

/* Update a user's information, including their password. */
UPDATE project.user SET password = '" + password + "', first_name = '" + firstName + "', last_name = '" + lastName + "', email = '" + email + "' WHERE username = '" + username.toLowerCase() + "';

/* Update a user's information, excluding their password. */
UPDATE project.user SET first_name = '" + firstName + "', last_name = '" + lastName + "', email = '" + email + "' WHERE username = '" + username.toLowerCase() + "';

/* Mark an admin for deletion by updating its salary to null. */		
UPDATE project.librarian SET salary = NULL WHERE username = '" + username + "';

/* Update a user's address. */
UPDATE project.address SET street_num = '" + num + "',street_name = '" + name + "',apartment = '" + apartment + "',city = '" + city + "',province = '" + prov + "',country = '" + country + "',postal_code = '" + postalCode + "' FROM project.hasadd WHERE project.address.add_id = project.hasadd.add_id AND project.hasadd.username = '" + username + "'AND project.hasadd.isshipping = '" + isShipping + "';

/* Update a book. */
UPDATE project.book SET title = '" + title + "',version = '" + version + "',num_pages = '" + pageCount + "',price = '" + price + "',royalty = '" + royalty + "',stock = '" + stock + "' WHERE isbn = '" + isbn + "';

/* Update a publisher. */
UPDATE project.publishes SET year = '" + year + "', pub_name = '" + publisher + "' WHERE isbn = '" + isbn + "';

/* Increase the quantity of an item within a specific cart by 1. */
UPDATE project.bask_item SET quantity = quantity + 1 WHERE isbn = '" + isbn + "' AND basket_id = '" + cartID + "';

/* Decrease the quantity of an item within a specific cart by 1. */
UPDATE project.bask_item SET quantity = quantity - 1 WHERE isbn = '" + isbn + "' AND basket_id = '" + cartID + "';

/* Update an orders status and order number. */
UPDATE project.order SET status = '" + status + "' WHERE order_num = '" + orderNum + "';

/* Update an orders status, tracking number, and order number. */
UPDATE project.order SET status = '" + status + "', tracking_num = '" + trackingNum + "' WHERE order_num = '" + orderNum + "';


-- Reports.java Queries
-- Selections
/* Select all information necessary to generate a genre reprot. */
SELECT name, " +
                    "count(name) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, s" +
                    "um(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book natural join project.hasgenre) " +
                    "WHERE date_placed > now() - interval '" + timeInter + "' " +
                    "group by name " +
                    "order by '" + sortOption[0] + "' '" + sortOption[1] + "';											-- sortOptions are used to determine how the data should be sorted. The user has a
																				-- dropdown menu to choose from. The timeInter is how far into the past this report 
																				-- should cover
/* Select all information necessary to generate a author reprot. */
SELECT coalesce(auth_fn, '') ||' '|| coalesce(auth_ln, '') as name, " +
                    "sum(quantity) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, " +
                    "sum(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book natural join project.writes) " +
                    "WHERE date_placed > now() - interval '" + timeInter + "' " +
                    "group by auth_fn, auth_ln " +
                    "order by '" + sortOption[0] + "' '" + sortOption[1] + "';											-- sortOptions are used to determine how the data should be sorted. The user has a
																				-- dropdown menu to choose from. The timeInter is how far into the past this report 
																				-- should cover

/* Select all information necessary to generate a publisher reprot. */
SELECT pub_name as name, " +
                    "sum(quantity) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, " +
                    "sum(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book natural join project.publishes) " +
                    "WHERE date_placed > now() - interval '" + timeInter + "' " +
                    "group by pub_name " +
                    "order by '" + sortOption[0] + "' '" + sortOption[1] + "';											-- sortOptions are used to determine how the data should be sorted. The user has a
																				-- dropdown menu to choose from. The timeInter is how far into the past this report 
																				-- should cover
/* Select all information necessary to generate an expenses reprot. */
SELECT date_trunc('" + timeInter + "', date_trunc('" + sortOption[1] + "', date_placed)) as time, " +
                    "sum(quantity) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, " +
                    "sum(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book) " +
                    "WHERE date_placed < now() " +
                    "group by date_trunc('" + timeInter + "', date_placed) " +
                    "ORDER BY '" + sortDate + "' '" + sortOption[1] + "';											-- sortOptions are  used to determine how the data should be sorted. The user has a
																				-- dropdown menu to choose from. The timeInter is how far into the past this report 
																				-- should cover. 		

/* Return the amount of books sold for a given time period (option). */
SELECT isbn, sum(quantity) as quantity " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book) " +
                    "WHERE date_placed > now() - interval '" + option + "' " +
                    "AND isbn = '" + isbn + "'" +
                    "group by isbn;															