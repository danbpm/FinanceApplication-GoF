package org.tigerbank.finance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса BankAccount")
class BankAccountTest {

    @Test
    @DisplayName("Создание счёта с валидными данными")
    void createAccountWithValidData() {
        BankAccount account = BankAccount.create("Основной счёт", new BigDecimal("1000.00"));

        assertNotNull(account.getId());
        assertEquals("Основной счёт", account.getName());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    @DisplayName("Создание счёта с null именем")
    void createAccountWithNullName() {
        assertThrows(IllegalArgumentException.class, () ->
                BankAccount.create(null, new BigDecimal("1000.00"))
        );
    }

    @Test
    @DisplayName("Создание счёта с пустым именем")
    void createAccountWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
                BankAccount.create("   ", new BigDecimal("1000.00"))
        );
    }

    @Test
    @DisplayName("Создание счёта с именем > 100 символов")
    void createAccountWithLongName() {
        String longName = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () ->
                BankAccount.create(longName, new BigDecimal("1000.00"))
        );
    }

    @Test
    @DisplayName("Создание счёта с null балансом")
    void createAccountWithNullBalance() {
        assertThrows(IllegalArgumentException.class, () ->
                BankAccount.create("Счёт", null)
        );
    }

    @Test
    @DisplayName("Создание счёта с отрицательным балансом")
    void createAccountWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () ->
                BankAccount.create("Счёт", new BigDecimal("-100.00"))
        );
    }

    @Test
    @DisplayName("Пополнение счёта на положительную сумму")
    void depositPositiveAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        account.deposit(new BigDecimal("500.00"));

        assertEquals(new BigDecimal("1500.00"), account.getBalance());
    }

    @Test
    @DisplayName("Пополнение счёта на null сумму")
    void depositNullAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        assertThrows(IllegalArgumentException.class, () ->
                account.deposit(null)
        );
    }

    @Test
    @DisplayName("Пополнение счёта на отрицательную сумму")
    void depositNegativeAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        assertThrows(IllegalArgumentException.class, () ->
                account.deposit(new BigDecimal("-100.00"))
        );
    }

    @Test
    @DisplayName("Пополнение счёта на нулевую сумму")
    void depositZeroAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        assertThrows(IllegalArgumentException.class, () ->
                account.deposit(BigDecimal.ZERO)
        );
    }

    @Test
    @DisplayName("Снятие со счёта достаточной суммы")
    void withdrawSufficientAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        account.withdraw(new BigDecimal("500.00"));

        assertEquals(new BigDecimal("500.00"), account.getBalance());
    }

    @Test
    @DisplayName("Снятие со счёта недостаточной суммы")
    void withdrawInsufficientAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("100.00"));
        assertThrows(IllegalArgumentException.class, () ->
                account.withdraw(new BigDecimal("500.00"))
        );
    }

    @Test
    @DisplayName("Снятие со счёта null суммы")
    void withdrawNullAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        assertThrows(IllegalArgumentException.class, () ->
                account.withdraw(null)
        );
    }

    @Test
    @DisplayName("Снятие всей суммы со счёта")
    void withdrawAllAmount() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("500.00"));
        account.withdraw(new BigDecimal("500.00"));

        assertEquals(BigDecimal.ZERO.setScale(2), account.getBalance());
    }

    @Test
    @DisplayName("Переименование счёта")
    void renameAccount() {
        BankAccount account = BankAccount.create("Старое имя", new BigDecimal("1000.00"));
        account.rename("Новое имя");

        assertEquals("Новое имя", account.getName());
    }

    @Test
    @DisplayName("Округление баланса до 2 знаков")
    void balanceRounding() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.125"));
        assertEquals(new BigDecimal("1000.13"), account.getBalance());
    }
}