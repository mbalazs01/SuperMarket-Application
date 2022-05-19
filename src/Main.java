import java.sql.SQLException;

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

    }

    private static void Initialize() throws SQLException, ClassNotFoundException {
        db = Database.getInstance();
    }
}
