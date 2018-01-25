package com.github.nija123098.evelyn.exception;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class InsufficientFundsException extends UserIssueException {
    public InsufficientFundsException(double cost, double owned, String quantityName) {
        super("You require " + cost + " but have " + owned + " " + quantityName);
    }
}
