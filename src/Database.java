import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.sql.*;
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

    public static class UsernameImporter {
        public static List<String> Import() throws SQLException, ClassNotFoundException {
            Class.forName("com.mysql.jdbc.Driver");
            List<String> resultList = new ArrayList<>();
            String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
            String username = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            String query = "SELECT * FROM users";
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(query);

            while (result.next())
                resultList.add(result.getString("username"));

            System.out.println("Usernames imported");

            return resultList;
        }
    }

    public static class RelationImporter {
        public static Multimap<Integer, Integer> Import() throws SQLException, ClassNotFoundException {
            Class.forName("com.mysql.jdbc.Driver");
            Multimap<Integer, Integer> resultMultimap = ArrayListMultimap.create();
            String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
            String username = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            String query = "SELECT * FROM product_category";
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(query);

            while (result.next()) {
                resultMultimap.put(result.getInt("productID"), result.getInt("categoryID"));
            }

            System.out.println("Relations imported");

            return resultMultimap;
        }
    }

    public static class ProductBaseImporter {
        public static List<ProductBase> Import() throws SQLException, ClassNotFoundException {
            Class.forName("com.mysql.jdbc.Driver");
            List<ProductBase> resultList = new ArrayList<>();
            String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
            String username = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            String query = "SELECT * FROM products";
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(query);

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
            Class.forName("com.mysql.jdbc.Driver");
            List<Category> resultList = new ArrayList<>();
            String jdbcURL = "jdbc:mysql://localhost:3306/supermarketdb";
            String username = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            String query = "SELECT * FROM categories";
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(query);

            while (result.next())
                resultList.add(new Category(result.getString("category"), result.getInt("categoryID")));

            System.out.println("Categories imported");

            return resultList;
        }
    }
}
