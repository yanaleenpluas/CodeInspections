package mavenyana;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

@SuppressWarnings("PMD.UseUtilityClass")
public class DiningExperienceManager {
    private static final Logger LOGGER = Logger.getLogger(DiningExperienceManager.class.getName());

	/**.
    *COSTO BASE 
    */    
	private static final double BASE_COST = 5.0;
	/**.
    *Discounst meals 
    */     
	private static final double DISCOUNT_5_MEALS = 0.1;
	/**.
    *Discount 10 meals   
    */ 
	private static final double DISCOUNT_10_MEALS = 0.2;
	/**.
    *DISCOUNT_THRESHOLD_1
    */
	private static final double DISCOUNT_THRES_1 = 50.0;
	/**.
    *Discount Amount 1
    */    
	private static final double DISCOUNT_AMOUNT_1 = 10.0;
    /**.
     *DISCOUNT THRESHOLD 2
     */    
	private static final double DISCOUNT_THRES_2 = 100.0;
	/**.
	 *DISCOUNT AMOUNT 2
	 */    
	private static final double DISCOUNT_AMOUNT_2 = 25.0;
  /**.
   *MAX ORDER QUANTITY   
   */    
	private static final int MAX_ORDER_QUANTI = 100;

	//map de menu
    private static Map<String, Double> menu = new HashMap<String, Double>();

    //COnstructor 
    public static void main(String[] args) {
        initializeMenu();
        final Scanner scanner = new Scanner(System.in);
        scanner.close(); 
        final Map<String, Integer> order = getOrder(scanner);
        final double totalCost = calculateTotalCost(order);
        displayOrderDetails(order, totalCost);
        final int finalCost = processOrderConfirmation(scanner, totalCost);
        displayFinalCost(finalCost);

    }

    // Inicializa el menú
    private static void initializeMenu() {
        menu.put("Burger", 10.0);
        menu.put("Pizza", 12.0);
        menu.put("Pasta", 8.0);
        menu.put("Salad", 6.0);
        menu.put("Soup", 5.0);
    }
    
    // Obtiene la orden del usuario
    private static Map<String, Integer> getOrder(final Scanner scanner) {
        Map<String, Integer> order = new HashMap<String, Integer>();
        LOGGER.info("Welcome to the Dining Experience Manager");
        while (true) {
            displayMenu();
            LOGGER.info("Enter the meal you'd like to order (or type 'done' to finish): ");
            final String meal = scanner.nextLine();
            final String done = "done";

            if (meal.equalsIgnoreCase(done)) {
                break;
            }

            if (!menu.containsKey(meal)) {
                LOGGER.info("Invalid meal selected. Please select from the menu.");
                continue;
            }

            LOGGER.info("Enter the quantity for " + meal + ": ");
            final int quantity = getValidQuantity(scanner.nextLine());

            if (quantity == -1) {
                LOGGER.info("Please enter a valid quantity (a positive integer greater than zero).");
                continue;
            }

            if (order.getOrDefault(meal, 0) + quantity > MAX_ORDER_QUANTI) {
                LOGGER.info("Maximum order quantity exceeded for this meal.");
                continue;
            }

            order.put(meal, order.getOrDefault(meal, 0) + quantity);
        }
        return order;
    }
    // Obtiene una cantidad válida
    private static int getValidQuantity(final String input) {
        try {
            final int quantity = Integer.parseInt(input);
            return quantity > 0 ? quantity : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    // Calcula el costo total
    private static double calculateTotalCost(final Map<String, Integer> order) {
        int totalQuantity = 0;
        for (final int value : order.values()) {
        	totalQuantity += value;
        }

        double totalCost = BASE_COST;
        final boolean bool1 = totalQuantity > 5;
        final boolean bool2 = totalQuantity > 10;

        if (bool1) {
            totalCost *= (1 - DISCOUNT_5_MEALS);
        }

        if (bool2) {
            totalCost *= (1 - DISCOUNT_10_MEALS);
        }

         double mealCost = 0;
        for (final Map.Entry<String, Integer> entry : order.entrySet()) {
            mealCost += menu.get(entry.getKey()) * entry.getValue();
        }

        totalCost += mealCost;

        return totalCost;
    }
    // Muestra el menú
    private static void displayMenu() {
    	LOGGER.info("Menu:");
        for (final String item : menu.keySet()) {
           LOGGER.info(item + ": $" + menu.get(item));
        }
    }
    // Muestra los detalles del pedido

    private static void displayOrderDetails(final Map<String, Integer> order,final double totalCost) {
    	LOGGER.info("\nYour order:");
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
        	LOGGER.info(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Total cost: $" + totalCost);
    }
    // Procesa la confirmación del pedido
    private static int processOrderConfirmation(final Scanner scanner, final double totalCost) {
    	LOGGER.info("Confirm your order? (yes/no): ");
        String confirmation = scanner.nextLine().toLowerCase();
        final String yes = "yes";
        if (confirmation.equals(yes)) {
        	final int total  = applyDiscounts(totalCost);
            return total;
        } else {
            return -1;
        }
    }
    // Muestra el costo final
    private static int applyDiscounts(final double totalCost) {
        if (totalCost > DISCOUNT_THRES_2) {
            return (int) (totalCost - DISCOUNT_AMOUNT_2);
        } else if (totalCost > DISCOUNT_THRES_1) {
            return (int) (totalCost - DISCOUNT_AMOUNT_1);
        }
        return (int) totalCost;
    }

    private static void displayFinalCost(final int finalCost) {
        if (finalCost != -1) {
        	LOGGER.info("Total cost of the dining experience: $" + finalCost);
        } else {
        	LOGGER.info("Invalid input or order canceled.");
        }
    }
}

