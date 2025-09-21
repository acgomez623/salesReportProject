package models;

import java.util.Objects;

/**
 * Represents a single product/quantity pair from a sales file.
 * Example file line: "3;5;" -> productId = 3, quantity = 5
 */
public class Sale {

	private final int productId;
    private final int quantity;

    /**
     * Create a sale entry.
     *
     * @param productId product id (must be > 0)
     * @param quantity  quantity sold (must be >= 0)
     */
    public Sale(int productId, int quantity) {
        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be > 0");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Parse a sale from a line with the format "productId;quantity;" (trailing semicolon allowed).
     *
     * @param line input text line
     * @return Sale instance
     * @throws IllegalArgumentException if the line is malformed or numbers cannot be parsed
     */
    public static Sale parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("line is empty");
        }

        String[] parts = line.split(";");
        if (parts.length < 2) {
            throw new IllegalArgumentException("invalid sale line, expected 'productId;quantity;'");
        }

        try {
            int productId = Integer.parseInt(parts[0].trim());
            int quantity = Integer.parseInt(parts[1].trim());
            return new Sale(productId, quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid number in sale line: " + line, e);
        }
    }

    @Override
    public String toString() {
        return "Sale{" +
               "productId=" + productId +
               ", quantity=" + quantity +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sale)) return false;
        Sale sale = (Sale) o;
        return productId == sale.productId && quantity == sale.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
	
}
