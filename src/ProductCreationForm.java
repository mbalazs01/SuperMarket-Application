import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductCreationForm extends JDialog {
    private JTextField nameField;
    private JTextField priceField;
    private JButton btnCreate;
    private JButton btnClose;
    private JList categoryList;
    private JButton btnAddCategory;
    private JButton btnReset;
    private static org.apache.log4j.Logger logger = Logger.getLogger(ProductCreationForm.class);
    private JPanel productCreationPanel;

    public ProductCreationForm(JFrame parent) {
        super(parent);
        setTitle("Create a new product");
        setContentPane(productCreationPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String categoryLine = "";
        DefaultListModel dlm = new DefaultListModel();

        final Database db;
        try {
            db = Database.getInstance();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        if (db.getProducts() != null) {
            for (Category category : db.getCategories().values()) {
                categoryLine += category.getName();

                dlm.addElement(categoryLine);
                categoryLine = "";
            }
            categoryList.setModel(dlm);
        }

        categoryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1) {
                    JList target = (JList)me.getSource();
                    int index = target.locationToIndex(me.getPoint());
                    if (index >= 0) {
                        Object item = target.getModel().getElementAt(index);
                        selectedItem = item.toString();
                        logger.info(selectedItem + " Was selected from Categories");
                        // System.out.println(selectedItem + " Has been selected");
                        btnAddCategory.setEnabled(true);
                    }
                }
            }
        });

        btnAddCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Category c: db.getCategories().values()) {
                    if(Objects.equals(c.getName(), selectedItem)) {
                        if(!productCategories.contains(c)) {
                            productCategories.add(c);
                            logger.info(selectedItem + " Was added as a Category");
                            btnReset.setEnabled(true);
                        } else {
                            logger.info(selectedItem + " Couldn't be added, Because it is already a selected Category");
                        }
                    }
                    else {
                        logger.info(selectedItem + " Couldn't be added as a Category");
                    }
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedItem = "";
                productCategories.clear();

                logger.info("Selection has been reset/Selected categories have been reset");
                btnReset.setEnabled(false);
                btnAddCategory.setEnabled(false);
            }
        });

        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createProduct();
                    db.reBuildDatabase();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void createProduct() throws SQLException, ClassNotFoundException {
        String productName = nameField.getText();
        int price = 0;
        if(!priceField.getText().isEmpty()) {
            price = Integer.parseInt(priceField.getText());
        }

        if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || productCategories.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database.ProductCRUD.Create(price, productName, productCategories);
        dispose();
    }

    private String selectedItem;
    private List<Category> productCategories = new ArrayList<Category>();
}
