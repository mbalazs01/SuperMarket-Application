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
    public Map<ProductBase, Integer> listItems() {
        return productsInCart;
    }
    public void addProductToCart(ProductBase product) {
        int amount = 1;
        ProductBase deleteMe = null;

        for (ProductBase pb: productsInCart.keySet()) {
            if(pb.getName() == product.getName()) {
                amount = productsInCart.get(pb) + 1;
                deleteMe = pb;
            }
        }
        if(amount != 1) {
            productsInCart.remove(deleteMe);
        }
        /*
        if(productsInCart.containsKey(product)) {
            amount = productsInCart.get(product) + 1;
            productsInCart.remove(product);
        } */
        productsInCart.put(product, amount);
        System.out.println(product.getName() + " - " + product.getPrice() + " - " + productsInCart.get(product) + " Has been added to the cart");

    }
    public void removeProductFromCart(ProductBase product) {

        ProductBase deleteMe = null;
        int amount = 0;

        for (ProductBase pb: productsInCart.keySet()) {
            if(pb.getName() == product.getName() && productsInCart.get(pb) > 1) {
                amount = productsInCart.get(pb) - 1;
                System.out.println("Found it!");
                deleteMe = pb;
            } else if(pb.getName() == product.getName()) {
                System.out.println("None here");
                deleteMe = pb;
            }
        }

        if(amount > 0) {
            System.out.println(product.getName() + " - " + product.getPrice() + " - " + (amount + 1) + " Has been removed from the cart");
            productsInCart.remove(deleteMe);
            productsInCart.put(deleteMe, amount);
        } else {
            System.out.println(product.getName() + " - " + product.getPrice() + " - " + (amount + 1) + " Has been removed from the cart");
            productsInCart.remove(deleteMe);
        }


        /*
        if(productsInCart.containsKey(product) && productsInCart.get(product) > 1) {
            int amount = productsInCart.get(product);
            productsInCart.remove(product);

            productsInCart.put(product, amount - 1);
            System.out.println(product.getName() + " - " + product.getPrice() + " - " + productsInCart.get(product) + " Has been removed from the cart");
        } else {
            System.out.println(product.getName() + " - " + product.getPrice() + " - " + productsInCart.get(product) + " Has been removed from the cart");
            productsInCart.remove(product);
        }
         */
    }
}
