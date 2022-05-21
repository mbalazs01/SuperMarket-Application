import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

// SINGLETON DATABASE
public class Database {
    private List<String> usernames;
    private List<Product> products;
    private List<ProductBase> productBases;
    Multimap<Integer, Integer> productCategoryRelations = ArrayListMultimap.create();
    private List<Category> categories;
    static Database instance;

    private Database() throws SQLException, ClassNotFoundException {
        productBases = ProductBaseImporter.Import();
        categories = CategoryImporter.Import();
        usernames = UsernameImporter.Import();
        productCategoryRelations = RelationImporter.Import();
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    public List<String> getUsernames() {
        return usernames;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Category> getCategories() {
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

    public static void RunQuery2(String query) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
        String username = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        Statement st = connection.createStatement();
        st.executeUpdate(query);
    }
    public static class UserCRUD {
        public static void Create(String firstname, String lastname, String password, String username) throws ClassNotFoundException, SQLException {
            String query = MessageFormat.format("INSERT INTO users SET" +
                    " firstname = \"{0}\", lastname = \"{1}\", password = \"{2}\"," +
                    " username = \"{3}\", balance = 0, isAdmin = 0", firstname, lastname, password, username);
            RunQuery2(query);
        }
        public static void Remove() throws ClassNotFoundException, SQLException {
            String query = "";
            RunQuery(query);
        }
        public static void Update() throws SQLException, ClassNotFoundException {
            String query = "";
            RunQuery(query);
        }
        public static void Delete() throws ClassNotFoundException, SQLException {
            String query = "";
            RunQuery(query);
        }
    }

    public static class ProductCRUD {
        public static void Create(int price, String name, List<Category> categories) throws ClassNotFoundException, SQLException {
            String query = MessageFormat.format("INSERT INTO products SET price = {0}, name = \"{1}\"", price, name);
            RunQuery2(query);

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
                RunQuery2(query);
            }
        }
        public static void Remove() throws ClassNotFoundException, SQLException {
            String query = "";
            RunQuery(query);
        }
        public static void Update() throws SQLException, ClassNotFoundException {
            String query = "";
            RunQuery(query);
        }
        public static void Delete() throws ClassNotFoundException, SQLException {
            String query = "";
            RunQuery(query);
        }
    }


    public static class UsernameImporter {
        public static List<String> Import() throws SQLException, ClassNotFoundException {
            List<String> resultList = new ArrayList<>();
            String query = "SELECT * FROM users";
            ResultSet result = RunQuery(query);

            while (result.next())
                resultList.add(result.getString("username"));

            System.out.println("Usernames imported");

            return resultList;
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

            System.out.println("Relations imported");

            return resultMultimap;
        }
    }

    public static class ProductBaseImporter {
        public static List<ProductBase> Import() throws SQLException, ClassNotFoundException {
            List<ProductBase> resultList = new ArrayList<>();
            String query = "SELECT * FROM products";
            ResultSet result = RunQuery(query);

            while (result.next())
                resultList.add(new ProductBase(
                        result.getString("name")
                        , result.getInt("price")
                        , result.getInt("productID")));

            System.out.println("Products imported");

            return resultList;
        }
    }

    public static class CategoryImporter {
        public static List<Category> Import() throws SQLException, ClassNotFoundException {
            List<Category> resultList = new ArrayList<>();
            String query = "SELECT * FROM categories";
            ResultSet result = RunQuery(query);

            while (result.next())
                resultList.add(new Category(result.getString("category"), result.getInt("categoryID")));

            System.out.println("Categories imported");

            return resultList;
        }
    }
}
