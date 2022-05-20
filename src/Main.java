import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static Database db;

    public static void main(String[] args) throws Exception {
        // for each product in the sql database, create an instance
        Initialize();
        if (db.getCategories() != null) {
            for (Category category : db.getCategories()) {
                System.out.println(category.getId());
                System.out.println(category.getName());
            }
        }

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

        checkout.listItems();
    }

    private static void Initialize() throws SQLException, ClassNotFoundException {
        db = Database.getInstance();
    }
}
