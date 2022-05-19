import java.util.ArrayList;
import java.util.List;

public class Product extends ProductBase {
    public Product(String name, int price, int productID, List<Category> categories) {
        super(name, price, productID);
        this.categories = categories;
    }
    private List<Category> categories = new ArrayList<Category>();

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
