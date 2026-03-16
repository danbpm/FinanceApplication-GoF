package org.tigerbank.finance.command.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.command.Command;
import org.tigerbank.finance.facade.IAccountFacade;
import org.tigerbank.finance.facade.ICategoryFacade;
import org.tigerbank.finance.facade.IOperationFacade;
import org.tigerbank.finance.facade.impl.ClearRepositoryFacade;
import org.tigerbank.finance.model.*;
import org.tigerbank.finance.service.export.DataService;
import org.tigerbank.finance.service.export.factory.DataServiceFactory;

import java.io.IOException;
import java.math.BigDecimal;

@Component("ExportToFileCommand")
@Scope("prototype")
public class ExportToFileCommand implements Command {
    private final IOperationFacade operationFacade;
    private final ICategoryFacade categoryFacade;
    private final IAccountFacade accountFacade;
    private final DataServiceFactory dataServiceFactory;
    private final ClearRepositoryFacade clearRepositoryFacade;

    public ExportToFileCommand(IOperationFacade operationFacade,
                               ICategoryFacade categoryFacade,
                               IAccountFacade accountFacade,
                               DataServiceFactory dataServiceFactory,
                               ClearRepositoryFacade clearRepositoryFacade) {
        this.operationFacade = operationFacade;
        this.categoryFacade = categoryFacade;
        this.accountFacade = accountFacade;
        this.dataServiceFactory = dataServiceFactory;
        this.clearRepositoryFacade = clearRepositoryFacade;
    }

    @Override
    public String getName() {
        return "ExportToFileCommand";
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

        System.out.println("=".repeat(5) + "СОХРАНЕНИЕ В ФАЙЛЫ" + "=".repeat(5));
        DataService jsonDs = dataServiceFactory.getDataService("json");
        DataService yamlDs = dataServiceFactory.getDataService("yaml");
        DataService csvDs = dataServiceFactory.getDataService("csv");
        try {
            jsonDs.exportToFile("data/");
            yamlDs.exportToFile("data/");
            csvDs.exportToFile("data/");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Почистим всё
        clearRepositoryFacade.clearAll();
    }
}
