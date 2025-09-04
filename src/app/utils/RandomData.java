package app.utils;

import java.util.Random;


/**
 * Helper class that provides random data for the file generators.
 * It includes random names, surnames, product names, prices, and quantities.
 */
public class RandomData {
	
	private static final String[] NAMES = { "Victor", "Angie", "David", "Valery", "Brayan", "Diego", "Alexander", "Xiomara", "Carolina" };
    private static final String[] LASTNAMES = { "Rodriguez", "Rojas", "Gomez", "Diaz", "Forbes", "Bernal", "Martinez", "Garces", "Newball", "Vasquez" };
    private static final String[] PRODUCT_NAMES = { "Cocacola", "Speedmax", "Gatorade", "Pepsi", "Colombiana", "Ponymalta", "Redbull", "Electrolit", "Colapola" };

    private static final Random random = new Random();

    /**
     * Returns a random first name.
     */
    public static String getRandomName() {
        return NAMES[random.nextInt(NAMES.length)];
    }

    /**
     * Returns a random last name.
     */
    public static String getRandomLastName() {
        return LASTNAMES[random.nextInt(LASTNAMES.length)];
    }

    /**
     * Returns a random product name.
     */
    public static String getRandomProductName() {
        return PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
    }

    /**
     * Returns a random integer price between 1000 and 10000.
     */
    public static int getRandomPrice() {
        return 1000 + random.nextInt(9001); // 9001 = (10000 - 1000) + 1
    }

    /**
     * Returns a random quantity between 1 and 20.
     */
    public static int getRandomQuantity() {
        return 1 + random.nextInt(20);
    }

}
