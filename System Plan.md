# Short summary
SuperMarket shop client demo for college 
made by Balázs Makai

# Planned use
The user starts the application and is greeted by a login/register form.

After registering or having logged in, they will be shown the store's catalog of items for purchasing.

The user can select items, add them to their cart, and once they are ready to order, they will be asked to provide their location for delivery, (and if they don't have enough on their account), to upload balance to their account (for the sake of simplicity, their location will not be stored, and uploading the necessary balance required will simply be a click of a button that always adds enough).

If the user is a manager, they can perform CRUD operations on the product database, effectively allowing them to alter/delete pre-existing product's names or prices, or add entirely new ones. They will also be able to create discounts on either a specific product, or product category, and set how long it should last.

# Development Environment
**XAMPP** - MYSQL

**IntelliJ** - JAVA

**Github** - GIT
 

# Database plan
- **users:**            Table for storing registered users
  - *userID*
  - *isAdmin*
  - *firstname*
  - *lastname*
  - *password*
  - *username*
  - *balance*

- **products:**         Table for storing products
  - *productID:*
  - *name:* 
  - *price:* 

- **categories:**       Table for storing categories
  - *categoryID:*
  - *category:*  

- **product_category:** Utility table for many-many connection
  - *productID:*
  - *categoryID:*  
