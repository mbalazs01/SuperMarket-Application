import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// SINGLETON DATABASE
public class Database {
    private static Logger logger = Logger.getLogger(Database.class);
    private HashMap<Integer, Product> products;
    private HashMap<Integer, ProductBase> productBases;
    private Multimap<Integer, Integer> productCategoryRelations = ArrayListMultimap.create();
    private HashMap<Integer, Category> categories;
    static Database instance;

    private Database() throws SQLException, ClassNotFoundException {
        productBases = ProductBaseImporter.Import();
        categories = CategoryImporter.Import();
        productCategoryRelations = RelationImporter.Import();

        products = BuildProducts();
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private HashMap<Integer, Product> BuildProducts() {
        if(productBases.isEmpty() || productCategoryRelations.isEmpty()) {
            return null;
        }

        HashMap<Integer, Product> products = new HashMap<>();
        List<Category> productCategories = new ArrayList<Category>();

        int productID ;
        String productName;
        int productPrice;
        String productCategoriesString = "";
        String productCategoriesIDs = "";

        int categoryID = 0;
        String categoryName = "";

        for(int key: productCategoryRelations.keys()) {
            productID = key;
            productName = productBases.get(productID).getName();
            productPrice = productBases.get(productID).getPrice();

            for(int id: productCategoryRelations.get(key)) {
                categoryID = id;
                categoryName = categories.get(categoryID).getName();
                productCategories.add(new Category(categoryName, categoryID));
            }
            for(Category c: productCategories) {
                productCategoriesString += categoryName + "#";
                productCategoriesIDs += categoryID + "#";
            }

            products.put(productID, new Product(productName, productPrice, productID, productCategoriesString, productCategoriesIDs));
            productCategoriesString = "";
            productCategoriesIDs = "";
            productCategories.clear();
        }

        logger.info("Products successfully imported");
        return products;
    }
    public HashMap<Integer, Product> getProducts() {
        return products;
    }

    public HashMap<Integer, Category> getCategories() {
        return categories;
    }

    public static ResultSet RunQuery(String query) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
        String username = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        Statement st = connection.createStatement();
        ResultSet result = st.executeQuery(query);
        return result;
    }

    public static ResultSet RunQuery(String query, boolean recieveSets) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
        String username = "root";
        String password = "";

        if(recieveSets == false) {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            return null;
        } else {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(query);
            return result;
        }
    }
    public static class UserCRUD {
        public static User Create(String firstname, String lastname, String password, String username) throws ClassNotFoundException, SQLException {
            String query = MessageFormat.format("INSERT INTO users SET" +
                    " firstname = \"{0}\", lastname = \"{1}\", password = \"{2}\"," +
                    " username = \"{3}\", balance = 0, isAdmin = 0", firstname, lastname, password, username);
            RunQuery(query, false);
            logger.info("User Registered a new account with the username " + username);
            return new User(firstname, lastname, username);
        }
        public static void Delete(String username) throws ClassNotFoundException, SQLException {
            String query = MessageFormat.format("DELETE FROM users WHERE username= \"{0}\"", username);
            logger.info(username + " was deleted from the database");
            RunQuery(query, false);
        }
        public static User Find(String username, String password) throws SQLException, ClassNotFoundException {
            String query = MessageFormat.format("SELECT * FROM users WHERE (username = \"{0}\" AND password = \"{1}\") LIMIT 1", username, password);
            ResultSet result = RunQuery(query);
            if(result.next() == false) {
                return null;
            } else {
                logger.info("User logged in " + result.getString("username"));
                return new User(result.getString("firstname"), result.getString("lastname"), result.getString("username"));
            }
        }
        public static boolean hasRegisteredUsers() throws SQLException, ClassNotFoundException {
            String query = "SELECT COUNT(*) FROM users";
            ResultSet result = RunQuery(query);

            if(result.next()) {
                int numUsers = result.getInt(1);

                if(numUsers > 0) {
                    logger.info("Found users in database");
                    return true;
                }
            }
            logger.info("Found no users in database");
            return false;
        }
    }

    public static class ProductCRUD {
        public static void Create(int price, String name, List<Category> categories) throws ClassNotFoundException, SQLException {
            String query = MessageFormat.format("INSERT INTO products SET price = {0}, name = \"{1}\"", price, name);
            RunQuery(query, false);

            for(Category category: categories) {
                int categoryID = category.getId();
                int productID = 0;

                query = "SELECT * FROM products WHERE name = '" + name + "' ";
                ResultSet result = RunQuery(query);

                while (result.next()) {
                    productID = result.getInt("productID");
                }

                query = MessageFormat.format("INSERT INTO product_category SET" +
                        " productID = {0}, categoryID = {1}", productID, categoryID);
                RunQuery(query, false);
            }
        }
        public static void Update() throws SQLException, ClassNotFoundException {
            String query = "";
            RunQuery(query, false);
        }
        public static void Delete(String productName) throws ClassNotFoundException, SQLException {
            String query = MessageFormat.format("DELETE FROM products WHERE name= \"{0}\"", productName);
            RunQuery(query, false);
        }
    }

    public static class RelationImporter {
        public static Multimap<Integer, Integer> Import() throws SQLException, ClassNotFoundException {
            Multimap<Integer, Integer> resultMultimap = ArrayListMultimap.create();
            String query = "SELECT * FROM product_category";

            ResultSet result = RunQuery(query);

            while (result.next()) {
                resultMultimap.put(result.getInt("productID"), result.getInt("categoryID"));
            }

            logger.info("Relations Table successfully imported");
            //System.out.println("Relations imported");

            return resultMultimap;
        }
    }

    public static class ProductBaseImporter {
        public static HashMap<Integer, ProductBase> Import() throws SQLException, ClassNotFoundException {
            HashMap<Integer, ProductBase> resultMap = new HashMap<>();
            String query = "SELECT * FROM products";
            ResultSet result = RunQuery(query);

            while (result.next())
                resultMap.put(result.getInt("productID"), new ProductBase(
                        result.getString("name")
                        , result.getInt("price")
                        , result.getInt("productID")));

            logger.info("Product Table successfully imported");
            //System.out.println("Products imported");

            return resultMap;
        }
    }

    public static class CategoryImporter {
        public static HashMap<Integer, Category> Import() throws SQLException, ClassNotFoundException {
            HashMap<Integer, Category> resultMap = new HashMap<>();
            String query = "SELECT * FROM categories";
            ResultSet result = RunQuery(query);

            while (result.next())
                resultMap.put(result.getInt("categoryID") ,new Category(result.getString("category"), result.getInt("categoryID")));

            logger.info("Categories Table successfully imported");
            //System.out.println("Categories imported");

            return resultMap;
        }
    }
}
