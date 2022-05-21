public class ProductBase {
    public ProductBase(String name, int price, int productID) {
        this.name = name;
        this.price = price;
        this.productID = productID;
    }
    public ProductBase(ProductBase other) {
        this.name = other.getName();
        this.price = other.getPrice();
        this.productID = other.getProductID();
    }
    private String name;

    private int productID;
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }
}
