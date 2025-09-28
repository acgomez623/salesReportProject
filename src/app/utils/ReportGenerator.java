package app.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Product;
import models.Sale;
import models.Salesman;

/**
 * ReportGenerator is responsible for creating the required CSV reports:
 * 1. report_salesmen.csv
 * 2. report_products.csv
 */
public class ReportGenerator {

	/**
     * Generates report_salesmen.csv
     * Each line: DocType;DocNumber;FullName;TotalSold
     */
    public static void generateSalesmenReport(
            String outputPath,
            Map<Salesman, List<Sale>> salesData,
            Map<Integer, Product> products) throws IOException {

        // Map for total per salesman
        Map<Salesman, Integer> totals = new HashMap<>();

        for (Map.Entry<Salesman, List<Sale>> entry : salesData.entrySet()) {
            Salesman salesman = entry.getKey();
            List<Sale> sales = entry.getValue();

            int total = 0;
            for (Sale s : sales) {
                Product product = products.get(s.getProductId());
                if (product != null) {
                    total += product.getPrice() * s.getQuantity();
                }
            }
            totals.put(salesman, total);
        }

        // Sort by total descending
        List<Map.Entry<Salesman, Integer>> sorted = new ArrayList<>(totals.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Write CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("TipoDocumento;NumeroDocumento;NombreCompleto;TotalVendido");
            writer.newLine();

            for (Map.Entry<Salesman, Integer> entry : sorted) {
                Salesman s = entry.getKey();
                int total = entry.getValue();

                writer.write(s.getDocType() + ";" +
                        s.getDocNumber() + ";" +
                        s.getFirstName() + " " + s.getLastName() + ";" +
                        total);
                writer.newLine();
            }
        }
        System.out.println("Generated " + outputPath);
    }

    /**
     * Generates report_products.csv
     * Each line: ID;ProductName;Price;quantitySold
     */
    public static void generateProductsReport(
            String outputPath,
            Map<Salesman, List<Sale>> salesData,
            Map<Integer, Product> products) throws IOException {

        // Map for total quantity per productId
        Map<Integer, Integer> totals = new HashMap<>();

        for (List<Sale> sales : salesData.values()) {
            for (Sale s : sales) {
                totals.merge(s.getProductId(), s.getQuantity(), Integer::sum);
            }
        }

        // Sort products by quantity sold descending
        List<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(totals.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Write CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("IDProducto;NombreProducto;PrecioUnitario;CantidadVendida");
            writer.newLine();

            for (Map.Entry<Integer, Integer> entry : sorted) {
                int productId = entry.getKey();
                int quantity = entry.getValue();
                Product product = products.get(productId);

                if (product != null) {
                    writer.write(product.getId() + ";" +
                            product.getName() + ";" +
                            product.getPrice() + ";" +
                            quantity);
                    writer.newLine();
                }
            }
        }
        System.out.println("Generated " + outputPath);
    }
	
}
