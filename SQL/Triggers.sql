DROP TRIGGER IF EXISTS delete_item ON project.bask_item;
DROP TRIGGER IF EXISTS delete_admin ON project.librarian;
DROP TRIGGER IF EXISTS new_cart ON project.user;
DROP TRIGGER IF EXISTS another_cart ON project.order;

-- Triggers
/* Triggered when a cart item has its quantity reduced to 0 */
CREATE TRIGGER delete_item
AFTER UPDATE ON project.bask_item		-- Whenever there is an update on a basket item, check for items that have a quantity of 0.
FOR EACH ROW
WHEN (new.quantity = 0)				-- There is no way application wise to reduce an item's quantity by more than 1, so there is no fear we will reach negative values.
						-- As such, we just check if the quantity is 0 (no longer should be in the cart).
EXECUTE PROCEDURE delete_cart_item();

/* Triggered when a librarian is added or edited to detect admins that should be deleted */
CREATE TRIGGER delete_admin
AFTER UPDATE OR INSERT ON project.librarian	-- Whenever there is an update/insertion to an admin, check for admins that should be deleted (an insertion can be done with a null salary.
FOR EACH ROW
WHEN (new.salary IS NULL)			-- Should the insertion/update have the salary as null, the admin must be deleted. 
EXECUTE PROCEDURE delete_old_admin();

/* Triggered when a new user is inserted into the database (since they'll need a cart to place orders) */
CREATE TRIGGER new_cart
AFTER INSERT ON project.user			-- Upon insertion, prepare to create a cart to associate with the user. 
EXECUTE PROCEDURE create_cart();

/* Triggered when a user successfully places an order in the system */
CREATE TRIGGER another_cart
AFTER INSERT ON project.order			-- Their cart is now used to keep track of their order, so a new cart must be assigned to them. They may want to buy more things!
EXECUTE PROCEDURE create_cart();