import java.sql.SQLException;

public class Main {
    static Database db;

    public static void main(String[] args) throws Exception {
        // for each product in the sql database, create an instance
        Initialize();
        /* if (db.getCategories() != null) {
            for (Category category : db.getCategories().values()) {
                System.out.println(category.getId());
                System.out.println(category.getName());
            }
        } */

        if (db.getProducts() != null) {
            for (Product product : db.getProducts().values()) {
                System.out.println(product.getName());
                System.out.println(product.getPrice());
                System.out.println(product.getProductID());

                for(Category category: product.getCategories()) {
                    System.out.println(category.getName());
                }
            }
        }

        //List<Category> categories = db.getCategories().values().stream().toList();
        //Database.ProductCRUD.Create(400, "Lays 100G", categories);

        /*
        Checkout checkout = Checkout.getInstance();

        ProductBase temp1 = new ProductBase("1KG of Potatoes", 420, 1);
        ProductBase temp2 = new ProductBase("1KG of Coal", 3215, 2);


        List<CheckoutCommand> commands = new ArrayList<CheckoutCommand>();
        CheckoutCommand commandOne = new CheckoutCommand(checkout, CheckoutCommand.Action.Add, temp1);
        CheckoutCommand commandTwo = new CheckoutCommand(checkout, CheckoutCommand.Action.Add, temp2);
        commands.add(commandOne);
        commands.add(commandTwo);

        for(CheckoutCommand command: commands) {
            command.Call();
        }

        for(CheckoutCommand command: commands) {
            command.Undo();
        }

        checkout.listItems(); */
    }

    private static void Initialize() throws SQLException, ClassNotFoundException {
        db = Database.getInstance();
    }
}
