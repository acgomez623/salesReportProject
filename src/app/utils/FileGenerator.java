package app.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Utility class responsible for creating the pseudo-random input files.
 * 
 * Required files:
 *  - products.txt : List of products with ID, name, and price.
 *  - salesmen.txt : List of salesmen with ID and full name.
 *  - TipoDoc_NumeroDoc.txt : Sales file for a given salesman.
 */

public class FileGenerator {
	
	private static final Random random = new Random();

    /** Stores the last generated docType for each salesman ID (doc number). */
    private static final Map<Long, String> DOC_TYPE_BY_ID = new HashMap<>();

    /** Tracks the last number of products generated to keep IDs coherent (1..N). */
    private static int lastProductsCount = 0;

    /**
     * Creates a product file with pseudo-random data.
     * Format per line: ID;ProductName;Price
     *
     * @param productsCount Number of products to generate
     */
    public static void createProductsFile(int productsCount) {
        ensureFolders();
        lastProductsCount = Math.max(1, productsCount);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.PRODUCTS_FILE))) {
            for (int i = 1; i <= productsCount; i++) {
                String line = i + Constants.SEPARATOR
                        + RandomData.getRandomProductName() + Constants.SEPARATOR
                        + RandomData.getRandomPrice();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error creating products file: " + e.getMessage());
        }
    }

    /**
     * Creates a salesman info file with pseudo-random data.
     * Also creates one sales file per generated salesman.
     *
     * salesmen.txt format per line:
     *   DocType;DocNumber;FirstName;LastName
     *
     * For each generated salesman, this method calls createSalesMenFile(...)
     * to generate its sales file inside data/sales/.
     *
     * @param salesmanCount Number of salesmen to generate
     */
    public static void createSalesManInfoFile(int salesmanCount) {
        ensureFolders();

        // Keep document numbers unique within this generation pass.
        Set<Long> usedDocNumbers = new HashSet<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SALESMEN_FILE))) {
            for (int i = 0; i < salesmanCount; i++) {
                String docType = Constants.DOCUMENT_TYPES[random.nextInt(Constants.DOCUMENT_TYPES.length)];

                long docNumber;
                do {
                    docNumber = 10000000L + random.nextInt(90000000); // 8-digit-ish
                } while (usedDocNumbers.contains(docNumber));
                usedDocNumbers.add(docNumber);

                String firstName = RandomData.getRandomName();
                String lastName  = RandomData.getRandomLastName();

                // Persist docType mapping for coherent sales filename later
                DOC_TYPE_BY_ID.put(docNumber, docType);

                // Write salesman info line
                String infoLine = docType + Constants.SEPARATOR
                                + docNumber + Constants.SEPARATOR
                                + firstName + Constants.SEPARATOR
                                + lastName;
                writer.write(infoLine);
                writer.newLine();

                // Generate a sales file with a random number of sales (5..15)
                int salesLines = 5 + random.nextInt(11);
                createSalesMenFile(salesLines, firstName + " " + lastName, docNumber);
            }
        } catch (IOException e) {
            System.err.println("Error creating salesmen file: " + e.getMessage());
        }
    }

    /**
     * Creates a sales file for a single salesman with pseudo-random sales.
     *
     * File name: data/sales/DocType_DocNumber.txt  (DocType inferred from the registry
     * created by createSalesManInfoFile; if missing, defaults to "CC")
     *
     * Content (each line):
     *   ProductID;Quantity
     *
     * @param randomSalesCount Number of sales to generate
     * @param name Salesman name (for logs/debug only, not written to file)
     * @param id Salesman document number (used in the file name)
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) {
        ensureFolders();

        // Try to use the docType assigned during salesmen generation.
        String docType = DOC_TYPE_BY_ID.getOrDefault(id, "CC");

        String fileName = Constants.SALES_FOLDER + docType + "_" + id + Constants.FILE_EXTENSION;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < randomSalesCount; i++) {
                int maxProducts = (lastProductsCount > 0) ? lastProductsCount : 10;
                int productId = 1 + random.nextInt(maxProducts);
                int quantity = RandomData.getRandomQuantity();

                String line = productId + Constants.SEPARATOR + quantity;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error creating sales file for " + name + " (" + id + "): " + e.getMessage());
        }
    }

    /** Ensures base folders exist. */
    private static void ensureFolders() {
        new File(Constants.DATA_FOLDER).mkdirs();
        new File(Constants.SALES_FOLDER).mkdirs();
    }
}
