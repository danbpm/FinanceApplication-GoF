package org.tigerbank.finance.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class BankAccount {

    private UUID id;
    private String name;
    private BigDecimal balance;

    private BankAccount() {}

    public static BankAccount create(String name, BigDecimal balance) {
        BankAccount account = new BankAccount();

        account.id = UUID.randomUUID();
        account.name = account.validateName(name);
        account.balance = account.validateBalance(balance);

        return account;
    }

    /*  =====ГЕТТЕРЫ=====  */
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void rename(String newName) {
        this.name = validateName(newName);
    }


    /*     ==== БИЗНЕС ЛОГИКА СУЩНОСТИ ====   */
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма зачисления должна быть положительной");
        }
        this.balance = this.balance.add(amount).setScale(2, RoundingMode.HALF_UP);
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма списания должна быть положительной");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств для списания. Баланс: " + balance);
        }
        this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalanceUnsafe(BigDecimal newBalance) {
        this.balance = validateBalance(newBalance);
    }

    private String validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Название не может быть null");
        }
        String t = name.trim();

        if(t.isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }

        if (t.length() > 100) {
            throw new IllegalArgumentException("Название больше 100 символов");
        }

        return t;
    }


    private BigDecimal validateBalance(BigDecimal balance) {
        if (balance == null) {
            throw  new IllegalArgumentException("Баланс не может быть null");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Отрицательный баланс");
        }

        return balance.setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public String toString() {
        return "BankAccount{id=" + id + ", name=" + name + ", balance=" + balance + " у.е.}";
    }






}



