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
        this.convertAllAmounts();

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
        var vars = accounts.get(username);
        return vars;

    }
//TODO
//    Actualmente funciona sin embargo cuando sacamos de una cuenta y no es valido deberia cortar toda
//    la ejecucion, asi como esta, cancela el retiro de la cuenta from pero deposita en la cuenta to, porque no hay
//    problema en realidad
    public void deposit(String username ,BigDecimal amount){
        BigDecimal oldBalance = accounts.get(username);
        BigDecimal newBalance = oldBalance.add(amount);
        if(newBalance.signum() < 0){
            System.out.println("This amount is not valid");
            return;
        }
        accounts.put(username, newBalance);
        if(!jsonParser.writeJson(new JSONObject(accounts),accountsRoute)){
            accounts.put(username, oldBalance.add(amount));
            return;
        }

        System.out.println("new Balance: " + newBalance);
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

    private void convertAllAmounts() {
        for (Map.Entry<String, BigDecimal> entry : this.accounts.entrySet()) {
            this.accounts.put(entry.getKey(), this.convertToBigDecimal(entry.getValue()));
        }
    }

    private static BigDecimal convertToBigDecimal(Object value) {
        return new BigDecimal(value.toString());
    }

}

