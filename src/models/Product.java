package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a product available for sale.
 *
 */
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private final int id;
    private String name;
    private int price;

    /**
     * Create a new product.
     *
     * @param id    unique product id (must be > 0)
     * @param name  product name (non-null)
     * @param price unit price as integer (>= 0)
     */
    public Product(int id, String name, int price) {
        if (id <= 0) {
            throw new IllegalArgumentException("product id must be > 0");
        }
        Objects.requireNonNull(name, "name must not be null");
        if (price < 0) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /** Returns product id. */
    public int getId() {
        return id;
    }

    /** Returns product name. */
    public String getName() {
        return name;
    }

    /** Sets product name. */
    public void setName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        this.name = name;
    }

    /** Returns unit price. */
    public int getPrice() {
        return price;
    }

    /** Sets unit price. */
    public void setPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}