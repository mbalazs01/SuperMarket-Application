import java.util.*;

public class Product extends ProductBase {
    public Product(String name, int price, int productID, String categories, String ids) {
        super(name, price, productID);
        this.categories = categories;
        this.categoriesList = uggghhhh(categories, ids);
    }

    private List<Category> uggghhhh(String categories, String categoryIDs) {
        List<String> categoryNames = Arrays.asList(categories.split("#"));
        List<String> ids = Arrays.asList(categoryIDs.split("#"));
        List<Category> resultingList = new ArrayList<>();

        int i = 0;
        for(String name: categoryNames) {
            resultingList.add(new Category(name, Integer.parseInt(ids.get(i))));
            i++;
        }

        return resultingList;
    }

    private String categoryIDs;
    private String categories;
    private List<Category> categoriesList = new ArrayList<Category>();

    public List<Category> getCategories() {
        return categoriesList;
    }

    public void setCategories(List<Category> categories) {
        this.categoriesList = categories;
    }
}
