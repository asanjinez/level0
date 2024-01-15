package org.solution;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

//At this time we have not taken into consideration the race conditions.
public class Account {
    private final Map<String, BigDecimal> accounts;
    private static final String accountsRoute = "src\\main\\resources\\accounts\\accounts.json";
    private final JsonParser<String,BigDecimal> jsonParser;
    public Account() {
        this.jsonParser = new JsonParser<>();
        this.accounts = this.loadAccounts();
        this.convertAllAmounts();

    }

    public boolean createAccount(String username, BigDecimal initialAmount){
        accounts.put(username,initialAmount);
        if(jsonParser.writeJson(new JSONObject(accounts),accountsRoute))
            return true;

        accounts.remove(username);
        return false;
    }

    private Map<String, BigDecimal> loadAccounts() {
     return jsonParser.loadJson(accountsRoute);

    }
    public BigDecimal getBalance(String username){
        return accounts.get(username);

    }
public boolean deposit(String username, BigDecimal amount) {
    BigDecimal oldBalance = accounts.get(username);
    BigDecimal newBalance = oldBalance.add(amount);
    try {
        if (newBalance.signum() < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        accounts.put(username, newBalance);
        if (!jsonParser.writeJson(new JSONObject(accounts), accountsRoute))
            throw new IOException("Error saving file");

        System.out.println("New Balance: " + newBalance);
        return true;

    } catch (Exception e) {
        System.out.println(e.getMessage());
        System.out.println("Transaction failed. Reverting changes.");
        accounts.put(username, oldBalance);
        jsonParser.writeJson(new JSONObject(accounts),accountsRoute);
        return false;
    }
}

    public boolean withdraw(String username ,BigDecimal amount){
        BigDecimal negativeAmount = amount.negate();
        return deposit(username,negativeAmount);
    }

    public boolean transfer(String fromUser, String toUser, BigDecimal amount) {
        try {
            if (amount.signum() <= 0 || !withdraw(fromUser, amount)) {
                throw new IllegalStateException("Transfer failed: Insufficient funds in the sender's account");
            }

            if (!deposit(toUser, amount)) {
                deposit(fromUser, amount);
                throw new IllegalStateException("Transfer failed: Unable to deposit into the receiver's account");
            }

            System.out.println("Transfer successful: " + amount + " transferred from "
                    + fromUser + " to " + toUser);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void convertAllAmounts() {
        for (Map.Entry<String, BigDecimal> entry : this.accounts.entrySet()) {
            this.accounts.put(entry.getKey(), convertToBigDecimal(entry.getValue()).setScale(2, RoundingMode.HALF_UP));
        }
    }

    private static BigDecimal convertToBigDecimal(Object value) {
        return new BigDecimal(value.toString());
    }

}

