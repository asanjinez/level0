package org.solution;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

//At this time we have not taken into consideration the race conditions.
public class Account {
    private Map<String, BigDecimal> accounts;
    private String accountsRoute = "src\\main\\resources\\accounts\\accounts.json";
    private final JsonParser<String,BigDecimal> jsonParser;
    public Account() {
        this.jsonParser = new JsonParser<String,BigDecimal>();
        this.accounts = this.loadAccounts();

    }

    public boolean createAccount(String username, BigDecimal intialAmmount){
        accounts.put(username,intialAmmount);
        if(jsonParser.writeJson(new JSONObject(accounts),accountsRoute))
            return true;

        accounts.remove(username);
        return false;
    }

    private Map<String, BigDecimal> loadAccounts() {
     return jsonParser.loadJson(accountsRoute);

    }

    private boolean existsAccount(String username){
        return accounts.containsKey(username);
    }
    public BigDecimal getBalance(String username){
        return accounts.get(username);

    }

    public void deposit(String username ,BigDecimal amount){
        BigDecimal oldBalance = accounts.get(username);
        BigDecimal newBalance = oldBalance.add(amount);
        if(newBalance.signum() < 0){
            System.out.println("This amount is not valid");
            return;
        }
        accounts.put(username, oldBalance.add(newBalance));
        if(!jsonParser.writeJson(new JSONObject(accounts),accountsRoute)){
            accounts.put(username, oldBalance.add(amount));
            return;
        }

        System.out.println("new Balance: " + oldBalance.add(amount));
    }

    public void withdraw(String username ,BigDecimal amount){
        BigDecimal negativeAmount = amount.negate();
        deposit(username,negativeAmount);
    }

    public void transfer(String fromUser, String toUser, BigDecimal amount){
        if (amount.signum() < 0){
            System.out.println("This amount is not valid");
            return;
        }

        if (!this.existsAccount(toUser)){
            System.out.println("This is not valid user");
            return;
        }
//        I am not considering isolation level or atomicity
        this.withdraw(fromUser,amount);
        this.deposit(toUser,amount);

    }

}
