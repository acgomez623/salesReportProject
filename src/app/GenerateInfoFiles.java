package app;

import app.utils.FileGenerator;



/**
 * This class generates input files (products, salesmen, and sales files).
 * 
 * The program must be executed without user interaction.
 * It will generate pseudo-random files that will be used later 
 * in the main class to generate sales reports.
 */
public class GenerateInfoFiles {

	public static void main(String[] args) {

		try {
            // 1) Generate products first so sales can reference valid IDs 1..N
            FileGenerator.createProductsFile(10);

            // 2) Generate salesmen and (inside) one sales file per salesman
            FileGenerator.createSalesManInfoFile(5);

            System.out.println("Files generated successfully!");
        } catch (Exception e) {
            System.err.println("Error while generating files: " + e.getMessage());
        }
	}
	

}
