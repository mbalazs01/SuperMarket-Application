import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Edited version of DashboardForm from https://www.youtube.com/watch?v=gCdj9OPAeT8
public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;
    private JList listProducts;
    private JList listCheckout;
    private JButton btnAddToCheckout;
    private JButton btnUndo;
    private JButton btnOrder;

    private Checkout checkout = Checkout.getInstance();
    public DashboardForm() throws SQLException, ClassNotFoundException {
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final Database db;
        try {
            db = Database.getInstance();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        String productLine = "";
        DefaultListModel dlm = new DefaultListModel();
        DefaultListModel dlmCheckout = new DefaultListModel();

        if (db.getProducts() != null) {
            for (Product product : db.getProducts().values()) {
                productLine += product.getProductID();
                productLine += " -- " + product.getName();
                productLine += " -- " + product.getPrice();

                for(Category category: product.getCategories()) {
                    productLine += " -- " + category.getName();
                }

                dlm.addElement(productLine);
                productLine = "";
            }
            listProducts.setModel(dlm);
        }

        boolean hasRegisteredUsers = Database.UserCRUD.hasRegisteredUsers();
        if (hasRegisteredUsers) {
            //show Login form
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null) {
                lbAdmin.setText("User: " + user.getUsername());
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        else {
            //show Registration form
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null) {
                lbAdmin.setText("Balance: " + user.getBalance());
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }

        listProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1) {
                    JList target = (JList)me.getSource();
                    int index = target.locationToIndex(me.getPoint());
                    if (index >= 0) {
                        Object item = target.getModel().getElementAt(index);
                        selectedItem = item.toString();
                        System.out.println(selectedItem + " Has been selected");
                        btnAddToCheckout.setEnabled(true);
                    }
                }
            }
        });

        btnUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckoutCommand lastCommand = commands.get(commands.size() - 1);
                lastCommand.Undo();
                commands.remove(lastCommand);

                updateCheckout(dlmCheckout, db);
            }
        });
        btnAddToCheckout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int key = Integer.parseInt(selectedItem.substring(0, 1));

                temp1 = new ProductBase(db.getProducts().get(key));

                commands.add(new CheckoutCommand(checkout, CheckoutCommand.Action.Add, new ProductBase(temp1)));
                CheckoutCommand current = commands.get(commands.size() - 1);
                current.Call();

                updateCheckout(dlmCheckout, db);
            }
        });
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user: " + user.getUsername(),
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    ProductBase temp1 = new ProductBase("1KG of Potatoes", 420, 1);
    List<CheckoutCommand> commands = new ArrayList<CheckoutCommand>();
    private void updateCheckout(DefaultListModel dlmCheckout, Database db) {
        dlmCheckout.clear();
        String productLine = "";

        if (checkout.listItems() != null) {
            for (ProductBase product : checkout.listItems().keySet()) {
                productLine += product.getProductID();
                productLine += " -- " + product.getName();
                productLine += " -- " + product.getPrice();
                productLine += " -- " + checkout.listItems().get(product);

                dlmCheckout.addElement(productLine);
                productLine = "";
            }
            listCheckout.setModel(dlmCheckout);
        }
    }
    private String selectedItem;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DashboardForm myForm = new DashboardForm();
    }
}
