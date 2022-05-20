public class CheckoutCommand implements ICommand {
    private Checkout checkout = Checkout.getInstance();

    public CheckoutCommand(Checkout checkout, Action action, ProductBase product) {
        this.checkout = checkout;
        this.action = action;
        this.product = product;
    }

    public enum Action {
        Add, Remove;
    }
    private ProductBase product;
    private Action action;

    @Override
    public void Call() {
        switch(action) {
            case Add -> {
                checkout.addProductToCart(product);
                break;
            }
            case Remove -> {
                checkout.removeProductFromCart(product);
                break;
            }
        }
    }

    @Override
    public void Undo() {
        switch(action) {
            case Add -> {
                checkout.removeProductFromCart(product);
                break;
            }
            case Remove -> {
                checkout.addProductToCart(product);
                break;
            }
        }
    }
}
