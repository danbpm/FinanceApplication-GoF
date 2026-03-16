package org.tigerbank.finance.command.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.command.Command;
import org.tigerbank.finance.facade.*;
import org.tigerbank.finance.facade.impl.ClearRepositoryFacade;
import org.tigerbank.finance.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component("ShowStatisticsCommand")
@Scope("prototype")
public class ShowStatisticsCommand implements Command {
    private final IOperationFacade operationFacade;
    private final ICategoryFacade categoryFacade;
    private final IAccountFacade accountFacade;
    private final IAnalyticsFacade analyticsFacade;
    private final ClearRepositoryFacade clearRepositoryFacade;

    public ShowStatisticsCommand(IAccountFacade accountFacade,
                                 ICategoryFacade categoryFacade,
                                 IOperationFacade operationFacade,
                                 IAnalyticsFacade analyticsFacade,
                                 ClearRepositoryFacade clearRepositoryFacade) {
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
        this.analyticsFacade = analyticsFacade;
        this.clearRepositoryFacade = clearRepositoryFacade;
    }

    @Override
    public String getName() {
        return "ShowStatisticsCommand";
    }

    @Override
    public void execute() {
        // Создадим два счёта пользователя
        BankAccount mainAccount = accountFacade.createAccount(
                BankAccount.create("Основной счет", new BigDecimal("175870.0"))
        );

        BankAccount reserveAccount = accountFacade.createAccount(
                BankAccount.create("Резервный счет", new BigDecimal("37897.56"))
        );


        // Создадим отслеживаемые категории
        Category salary = categoryFacade.createCategory(
                Category.create("Зарплата", CategoryType.INCOME)
        );
        Category fastfood = categoryFacade.createCategory(
                Category.create("Фастфуд", CategoryType.EXPENSE)
        );
        Category transferIn = categoryFacade.createCategory(
                Category.create("Перевод(вх.)", CategoryType.INCOME)
        );
        Category transferOut = categoryFacade.createCategory(
                Category.create("Перевод(исх.)", CategoryType.EXPENSE)
        );

        // Пришел аванс
        operationFacade.createOperation(
                Operation.create(
                        OperationType.INCOME,
                        mainAccount.getId(),
                        new BigDecimal("33543.34"),
                        "Аванс",
                        salary.getId())
        );

        // Сразу пошли обедать
        operationFacade.createOperation(
                Operation.create(
                        OperationType.EXPENSE,
                        mainAccount.getId(),
                        new BigDecimal("750.00"),
                        "Oбед",
                        fastfood.getId())
        );

        // Перевод на резервный счет с главного
        operationFacade.createOperation(
                Operation.create(
                        OperationType.EXPENSE,
                        mainAccount.getId(),
                        new BigDecimal("20000.00"),
                        "Перевод на резервный счет",
                        transferOut.getId())
        );

        // Пополнение с главного счета
        operationFacade.createOperation(
                Operation.create(
                        OperationType.INCOME,
                        reserveAccount.getId(),
                        new BigDecimal("20000.00"),
                        "Пополнение с главного счета",
                        transferIn.getId())
        );

        System.out.println("Операции пользователя: ");
        for (Operation op : operationFacade.getAllOperations()) {
            System.out.println(op.toString());
        }
        System.out.println();

        // Выведем статистику по основному и резервному счета
        System.out.println("=".repeat(5) + "СТАТИСТИКА ПО ОСНОВНОМУ СЧЕТУ" + "=".repeat(5));
        analyticsFacade.showStatisticsByAccount(mainAccount.getId(), LocalDate.now(), LocalDate.now());
        System.out.println("ГРУППИРОВКА ПО КАТЕГОРИЯМ: ");
        System.out.println("--- Доход");
        analyticsFacade.showExpansesByCategory(mainAccount.getId(), OperationType.INCOME, LocalDate.now(), LocalDate.now());
        System.out.println("--- Расход");
        analyticsFacade.showExpansesByCategory(mainAccount.getId(), OperationType.EXPENSE, LocalDate.now(), LocalDate.now());

        System.out.println("=".repeat(5) + "СТАТИСТИКА ПО РЕЗЕРВНОМУ СЧЕТУ" + "=".repeat(5));
        analyticsFacade.showStatisticsByAccount(reserveAccount.getId(), LocalDate.now(), LocalDate.now());
        System.out.println("ГРУППИРОВКА ПО КАТЕГОРИЯМ: ");
        System.out.println("--- Доход");
        analyticsFacade.showExpansesByCategory(reserveAccount.getId(), OperationType.INCOME, LocalDate.now(), LocalDate.now());
        System.out.println("--- Расход");
        analyticsFacade.showExpansesByCategory(reserveAccount.getId(), OperationType.EXPENSE, LocalDate.now(), LocalDate.now());


        // Почистим всё
        clearRepositoryFacade.clearAll();

    }
}
