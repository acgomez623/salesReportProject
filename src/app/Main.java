package app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.utils.DataLoader;
import app.utils.ReportGenerator;
import models.Product;
import models.Sale;
import models.Salesman;

/**
 * Main class to orchestrate loading data and generating reports.
 */
public class Main {

	public static void main(String[] args) {
		
		try {
            // Input paths (relative to project root)
            String productsFile = "data/products.txt";
            String salesmenFile = "data/salesmen.txt";
            String salesDir = "data/sales";

            // Output report paths (saved in project root)
            String reportSalesmen = "report_salesmen.csv";
            String reportProducts = "report_products.csv";

            // 1. Load products
            Map<Integer, Product> products = DataLoader.loadProducts(productsFile);

            // 2. Load salesmen
            Map<String, Salesman> salesmen = DataLoader.loadSalesmen(salesmenFile);

            // 3. Load sales
            Map<Salesman, List<Sale>> salesData = DataLoader.loadSales(salesDir, salesmen, products);

            // 4. Generate reports
            ReportGenerator.generateSalesmenReport(reportSalesmen, salesData, products);
            ReportGenerator.generateProductsReport(reportProducts, salesData, products);

            System.out.println("Reports generated successfully.");
        } catch (IOException e) {
            System.err.println("Error while processing data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
		
	}

}
