import java.util.HashMap;
import java.util.Map;

public class Checkout {
    static Checkout instance;
    private Checkout() {

    }
    public static Checkout getInstance() {
        if (instance == null) {
            instance = new Checkout();
        }
        return instance;
    }
    private Map<ProductBase, Integer> productsInCart = new HashMap<>();
    public void listItems() {
        for(ProductBase pb: productsInCart.keySet()) {
            System.out.println(pb.getName() + " - " + pb.getPrice() + " - " + productsInCart.get(pb));
        }
    }
    public void addProductToCart(ProductBase product) {
        int amount = 1;

        if(productsInCart.containsKey(product)) {
            amount = productsInCart.get(product) + 1;
            productsInCart.remove(product);
        }
        productsInCart.put(product, amount);
    }
    public void removeProductFromCart(ProductBase product) {
        if(productsInCart.containsKey(product) && productsInCart.get(product) > 1) {
            int amount = productsInCart.get(product);
            productsInCart.remove(product);

            productsInCart.put(product, amount - 1);
        } else {
            productsInCart.remove(product);
        }
    }
}
