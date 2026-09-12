import java.util.Scanner;

/**
 * Laboratory Activity 1 - Canteen Ordering System
 * OOP 101 - Object-oriented Programming
 *
 * Sir Jairus Matthew M. Baldesco
 * CS-2103
 */

public class Canteen {

    // ===== Menu data, stored as parallel arrays =====
    // itemNames[i] is priced at itemPrices[i]
    static final String[] itemNames = {"Burger", "Pizza", "Pasta", "Sandwich", "Milk Tea"};
    static final double[] itemPrices = {80.00, 120.00, 100.00, 70.00, 90.00};

    // ===== Order rules =====
    static final int minQuantity = 1;
    static final int maxQuantity = 10;
    static final double bulkThreshold = 500.00;

    // ===== Discount rates =====
    static final double studentBulkRate = 0.15;
    static final double studentRate = 0.10;
    static final double bulkRate = 0.05;
    static final double noDiscountRate = 0.00;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Running totals for the whole transaction
        int totalQuantity = 0;
        double totalBeforeDiscount = 0.00;
        double totalDiscount = 0.00;

        displayMenu();

        while (true) {
            System.out.print("Enter item number: ");
            int itemNumber = input.nextInt();

            System.out.print("Enter quantity: ");
            int quantity = input.nextInt();

            // Requirement 3 and 6: reject the order, keep it out of the totals,
            // and skip the rest of the processing for this attempt.
            if (!isValidOrder(itemNumber, quantity)) {
                System.out.println("Invalid order! Please enter a valid item and quantity.");
                if (!wantsToOrderAgain(input)) {
                    break;
                }
                continue;
            }

            System.out.print("Are you a student? (Y/N): ");
            String studentAnswer = input.next();
            boolean isStudent = studentAnswer.equalsIgnoreCase("Y");

            double subtotal = itemPrices[itemNumber - 1] * quantity;
            double discountRate = getDiscountRate(isStudent, subtotal);
            double discount = subtotal * discountRate;
            double orderTotal = subtotal - discount;

            System.out.printf("Subtotal: $%.2f%n", subtotal);
            System.out.printf("Discount: $%.2f%n", discount);
            System.out.printf("Order total: $%.2f%n", orderTotal);

            // Add this order to the running totals for the final summary
            totalQuantity += quantity;
            totalBeforeDiscount += subtotal;
            totalDiscount += discount;

            // Requirement 7: stop taking orders the moment the customer answers N.
            if (!wantsToOrderAgain(input)) {
                break;
            }
        }

        displaySummary(totalQuantity, totalBeforeDiscount, totalDiscount);
        input.close();
    }

    /**
     * Prints the canteen menu with each item number, name, and price.
     */
    public static void displayMenu() {
        System.out.println("===== M E N U =====");
        for (int i = 0; i < itemNames.length; i++) {
            System.out.printf("%d. %s - $%.2f%n", i + 1, itemNames[i], itemPrices[i]);
        }
        System.out.println();
    }

    /**
     * Checks whether the order is valid.
     * The item number must match a menu choice and the quantity must be
     * at least minQuantity but no more than maxQuantity.
     */
    public static boolean isValidOrder(int itemNumber, int quantity) {
        boolean validItem = (itemNumber >= 1 && itemNumber <= itemNames.length);
        boolean validQuantity = (quantity >= minQuantity && quantity <= maxQuantity);
        return validItem && validQuantity;
    }

    /**
     * Returns the discount rate that applies to one order.
     * A student reaching the bulk threshold gets the highest rate, so that
     * condition is checked first and the customer never receives two discounts.
     */
    public static double getDiscountRate(boolean isStudent, double subtotal) {
        if (isStudent && subtotal >= bulkThreshold) {
            return studentBulkRate;
        } else if (isStudent) {
            return studentRate;
        } else if (subtotal >= bulkThreshold) {
            return bulkRate;
        } else {
            return noDiscountRate;
        }
    }

    /**
     * Asks the customer whether to place another order.
     * Returns true only when the answer is Y.
     */
    public static boolean wantsToOrderAgain(Scanner input) {
        System.out.print("Do you want to order again? (Y/N): ");
        String answer = input.next();
        System.out.println();
        return answer.equalsIgnoreCase("Y");
    }

    /**
     * Prints the totals for the whole transaction.
     */
    public static void displaySummary(int totalQuantity, double totalBeforeDiscount, double totalDiscount) {
        double finalAmount = totalBeforeDiscount - totalDiscount;

        System.out.println("===== ORDER SUMMARY =====");
        System.out.println("Total items: " + totalQuantity);
        System.out.printf("Total before discount: $%.2f%n", totalBeforeDiscount);
        System.out.printf("Total discount: $%.2f%n", totalDiscount);
        System.out.printf("Final amount: $%.2f%n", finalAmount);
        System.out.println("Thank you for ordering!");
    }
}
