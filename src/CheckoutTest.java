import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class CheckoutTest {

    @Test
    void addingSameProductShouldIncreaseAmount() {
        Checkout checkout = Checkout.getInstance();

        ProductBase testProduct = new ProductBase("Test", 250, 1);
        ProductBase testProduct2 = new ProductBase("Test2", 350, 2);

        checkout.addProductToCart(testProduct);
        checkout.addProductToCart(testProduct);

        Map<ProductBase, Integer> expectedMap = new HashMap<>();
        expectedMap.put(testProduct, 2);

        assertEquals(expectedMap, checkout.listItems());

        checkout.addProductToCart(testProduct2);
        checkout.addProductToCart(testProduct2);

        expectedMap.put(testProduct2, 2);

        assertEquals(expectedMap, checkout.listItems());
    }

    @Test
    void removingSameProductShouldDecrementAmount() {
        Checkout checkout = Checkout.getInstance();

        ProductBase testProduct = new ProductBase("Test", 250, 1);
        ProductBase testProduct2 = new ProductBase("Test2", 350, 2);

        checkout.addProductToCart(testProduct);
        checkout.addProductToCart(testProduct);

        checkout.removeProductFromCart(testProduct);

        Map<ProductBase, Integer> expectedMap = new HashMap<>();
        expectedMap.put(testProduct, 1);

        assertEquals(expectedMap, checkout.listItems());

        checkout.addProductToCart(testProduct);
        checkout.addProductToCart(testProduct2);
        checkout.addProductToCart(testProduct2);

        checkout.removeProductFromCart(testProduct);
        checkout.removeProductFromCart(testProduct2);

        expectedMap.put(testProduct2, 1);

        assertEquals(expectedMap, checkout.listItems());
    }
}