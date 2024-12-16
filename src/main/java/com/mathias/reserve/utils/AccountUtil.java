package com.mathias.reserve.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

public class AccountUtil {

    // Set of generated tickets to ensure uniqueness
    private static final Set<String> GENERATED_TICKETS = new HashSet<>();
    private static final Set<String> GENERATED_BOOKINGS = new HashSet<>();

    // Character pool for generating random letters
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Secure random instance for better randomness
    private static final SecureRandom RANDOM = new SecureRandom();


    /**
     * Generate a 10-character unique ticket number.
     * The format is: 4 uppercase letters followed by 6 digits.
     *
     * @return A unique 10-character ticket number.
     */
    public static String generateTicketNumber() {
        String ticketNumber;
        do {
            ticketNumber = generateRandomLetters(4) + generateRandomDigits(6);
        } while (!GENERATED_TICKETS.add(ticketNumber)); // Ensure the ticket number is unique
        return ticketNumber;
    }

    /**
     * Generate an 8-character unique booking number.
     * The format is: 6 digits followed by 2 uppercase letters.
     *
     * @return A unique 8-character booking number.
     */
    public static String generateBookingNumber() {
        String bookingNumber;
        do {
            bookingNumber = generateRandomDigits(6) + generateRandomLetters(2);
        } while (!GENERATED_BOOKINGS.add(bookingNumber)); // Ensure the booking number is unique
        return bookingNumber;
    }
    

    /**
     * Generate random uppercase letters.
     *
     * @param count Number of letters to generate.
     * @return A string of random uppercase letters.
     */
    private static String generateRandomLetters(int count) {
        StringBuilder letters = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = RANDOM.nextInt(LETTERS.length());
            letters.append(LETTERS.charAt(index));
        }
        return letters.toString();
    }

    /**
     * Generate random digits.
     *
     * @param count Number of digits to generate.
     * @return A string of random digits.
     */
    private static String generateRandomDigits(int count) {
        StringBuilder digits = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            digits.append(RANDOM.nextInt(10)); // Random number between 0 and 9
        }
        return digits.toString();
    }


    public static void main(String[] args) {
        // Test the ticket generation
        for (int i = 0; i < 10; i++) {
            System.out.println(generateTicketNumber());
        }
    }

}
