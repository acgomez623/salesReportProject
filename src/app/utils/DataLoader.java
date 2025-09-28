package app.utils;

import models.Product;
import models.Sale;
import models.Salesman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * DataLoader is responsible for reading input files (products, salesmen, sales).
 * It validates data consistency and ignores incoherent records with warnings.
 */
public class DataLoader {

    /**
     * Loads products from a file.
     * Format per line: ID;ProductName;Price
     */
    public static Map<Integer, Product> loadProducts(String filePath) throws IOException {
        Map<Integer, Product> products = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length != 3) {
                    System.err.println("Invalid product line skipped: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int price = Integer.parseInt(parts[2].trim());

                    products.put(id, new Product(id, name, price));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid product data skipped: " + line);
                }
            }
        }
        return products;
    }

    /**
     * Loads salesmen from a file.
     * Format per line: DocType;DocNumber;FirstName;LastName
     *
     * Note: Salesman model expects docNumber as long, so we parse it here.
     * The returned map uses the key "DocType_docNumber" (string) to match sales filenames.
     */
    public static Map<String, Salesman> loadSalesmen(String filePath) throws IOException {
        Map<String, Salesman> salesmen = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length != 4) {
                    System.err.println("Invalid salesman line skipped: " + line);
                    continue;
                }

                String docType = parts[0].trim();
                String docNumberStr = parts[1].trim();
                String firstName = parts[2].trim();
                String lastName = parts[3].trim();

                long docNumber;
                try {
                    docNumber = Long.parseLong(docNumberStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid doc number skipped: " + line);
                    continue;
                }

                Salesman salesman = new Salesman(docType, docNumber, firstName, lastName);
                String key = docType + "_" + docNumber; // same format as sales file names
                salesmen.put(key, salesman);
            }
        }
        return salesmen;
    }

    /**
     * Loads sales for all vendors.
     * Each vendor has its own file named DocType_DocNumber.txt inside salesDir.
     * Format of each file (per line): ProductID;Quantity
     *
     * Returns a map Salesman -> list of Sale objects.
     */
    public static Map<Salesman, List<Sale>> loadSales(
            String salesDir,
            Map<String, Salesman> salesmen,
            Map<Integer, Product> products) throws IOException {

        Map<Salesman, List<Sale>> salesData = new HashMap<>();
        File folder = new File(salesDir);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("Sales directory not found: " + salesDir);
        }

        File[] files = folder.listFiles();
        if (files == null) return salesData;

        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".txt")) continue;

            // Extract salesman key from file name: CC_12345678.txt → CC_12345678
            String fileName = file.getName();
            String fileNameNoExt = fileName.substring(0, fileName.length() - 4);
            String[] nameParts = fileNameNoExt.split("_");
            if (nameParts.length != 2) {
                System.err.println("Invalid sales file name skipped: " + fileName);
                continue;
            }
            String key = nameParts[0] + "_" + nameParts[1];

            Salesman salesman = salesmen.get(key);
            if (salesman == null) {
                System.err.println("Sales file ignored, salesman not found: " + fileName);
                continue;
            }

            List<Sale> salesList = salesData.computeIfAbsent(salesman, k -> new ArrayList<>());

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] saleParts = line.split(";");
                    if (saleParts.length < 2) {
                        System.err.println("Invalid sale line skipped in " + fileName + ": " + line);
                        continue;
                    }

                    try {
                        int productId = Integer.parseInt(saleParts[0].trim());
                        int quantity = Integer.parseInt(saleParts[1].trim());

                        Product product = products.get(productId);
                        if (product == null) {
                            System.err.println("Sale ignored, product not found: " + productId + " in " + fileName);
                            continue;
                        }
                        if (quantity <= 0) {
                            System.err.println("Sale ignored, invalid quantity: " + line + " in " + fileName);
                            continue;
                        }

                        // IMPORTANT: Sale model expects (int productId, int quantity)
                        salesList.add(new Sale(productId, quantity));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid sale data skipped in " + fileName + ": " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading sales file " + fileName + ": " + e.getMessage());
            }
        }

        return salesData;
    }
}
