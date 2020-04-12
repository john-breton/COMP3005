DROP FUNCTION IF EXISTS delete_old_admin();
DROP FUNCTION IF EXISTS order_more_books(numeric);
DROP FUNCTION IF EXISTS create_cart();

-- Functions
/* Deletes a cart item from project.bask_item when the quantity associated with said item is 0 */
CREATE OR REPLACE FUNCTION delete_cart_item() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM project.bask_item WHERE quantity = 0;			-- Delete any items with quantity = 0, executed via a trigger.
    RETURN NULL;
END;
$$;;

/* Deletes an admin if they have been marked for deletion */
CREATE OR REPLACE FUNCTION delete_old_admin() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM project.librarian WHERE salary IS NULL;			-- Having a null salary is indication that the admin should be deleted, executed via a trigger.
    RETURN NULL;
END;
$$;;

/* Indicates whether more books should be ordered based on the remaining quantity */
CREATE OR REPLACE FUNCTION order_more_books(searchISBN numeric(13,0))
    RETURNS BOOLEAN AS $$						-- True if more books should be ordered (i.e. stock < 10), false otherwise.
BEGIN
    RETURN 10 > (
        SELECT stock
        FROM project.book
        WHERE isbn = searchISBN
    );
END;									-- Used to print to the console saying that an email will be sent to the publisher to get more books based on the 
									-- amount sold last month.
$$ 	LANGUAGE plpgsql;

/* Creates a cart that can be associated to a user */
CREATE OR REPLACE FUNCTION create_cart() RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO project.basket default values;				-- Insert a new cart with the next sequential ID identifier.
    RETURN NULL;
END;
$$;;