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
    // ITEM_NAMES[i] is priced at ITEM_PRICES[i]
    static final String[] ITEM_NAMES = {"Burger", "Pizza", "Pasta", "Sandwich", "Milk Tea"};
    static final double[] ITEM_PRICES = {80.00, 120.00, 100.00, 70.00, 90.00};

    // ===== Order rules =====
    static final int MIN_QUANTITY = 1;
    static final int MAX_QUANTITY = 10;
    static final double BULK_THRESHOLD = 500.00;

    // ===== Discount rates =====
    static final double STUDENT_BULK_RATE = 0.15;
    static final double STUDENT_RATE = 0.10;
    static final double BULK_RATE = 0.05;
    static final double NO_DISCOUNT_RATE = 0.00;

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

            double subtotal = ITEM_PRICES[itemNumber - 1] * quantity;
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
        for (int i = 0; i < ITEM_NAMES.length; i++) {
            System.out.printf("%d. %s - $%.2f%n", i + 1, ITEM_NAMES[i], ITEM_PRICES[i]);
        }
        System.out.println();
    }

    /**
     * Checks whether the order is valid.
     * The item number must match a menu choice and the quantity must be
     * at least MIN_QUANTITY but no more than MAX_QUANTITY.
     */
    public static boolean isValidOrder(int itemNumber, int quantity) {
        boolean validItem = (itemNumber >= 1 && itemNumber <= ITEM_NAMES.length);
        boolean validQuantity = (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY);
        return validItem && validQuantity;
    }

    /**
     * Returns the discount rate that applies to one order.
     * A student reaching the bulk threshold gets the highest rate, so that
     * condition is checked first and the customer never receives two discounts.
     */
    public static double getDiscountRate(boolean isStudent, double subtotal) {
        if (isStudent && subtotal >= BULK_THRESHOLD) {
            return STUDENT_BULK_RATE;
        } else if (isStudent) {
            return STUDENT_RATE;
        } else if (subtotal >= BULK_THRESHOLD) {
            return BULK_RATE;
        } else {
            return NO_DISCOUNT_RATE;
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
