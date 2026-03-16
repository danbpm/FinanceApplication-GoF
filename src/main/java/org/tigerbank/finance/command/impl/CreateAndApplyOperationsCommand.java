package org.tigerbank.finance.command.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.command.Command;
import org.tigerbank.finance.facade.IAccountFacade;
import org.tigerbank.finance.facade.ICategoryFacade;
import org.tigerbank.finance.facade.IOperationFacade;
import org.tigerbank.finance.facade.impl.ClearRepositoryFacade;
import org.tigerbank.finance.model.*;

import java.math.BigDecimal;

@Component("CreateAndApplyOperationsCommand")
@Scope("prototype")
public class CreateAndApplyOperationsCommand implements Command {
    private final IOperationFacade operationFacade;
    private final ICategoryFacade categoryFacade;
    private final IAccountFacade accountFacade;
    private final ClearRepositoryFacade clearRepositoryFacade;

    public CreateAndApplyOperationsCommand(IAccountFacade accountFacade,
                                           ICategoryFacade categoryFacade,
                                           IOperationFacade operationFacade,
                                           ClearRepositoryFacade clearRepositoryFacade) {
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
        this.clearRepositoryFacade = clearRepositoryFacade;
    }


    @Override
    public String getName() {
        return "CreateAndApplyOperationsCommand";
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

        // Выведем все счета пользователя и отслеживаемые категории
        System.out.println("Банковские счета ДО ОПЕРАЦИЙ: ");
        for (BankAccount account : accountFacade.getAllAccounts()) {
            System.out.println(account.toString());
        }
        System.out.println();

        // Пришел аванс
        Operation prepaidOp = operationFacade.createOperation(
                Operation.create(
                OperationType.INCOME,
                mainAccount.getId(),
                new BigDecimal("33543.34"),
                "Аванс",
                salary.getId())
        );

        // Сразу пошли обедать
        Operation dinnerOp = operationFacade.createOperation(
                Operation.create(
                OperationType.EXPENSE,
                mainAccount.getId(),
                new BigDecimal("750.00"),
                "Oбед",
                fastfood.getId())
        );

        // Перевод на резервный счет с главного
        Operation transferOutOp = operationFacade.createOperation(
                Operation.create(
                OperationType.EXPENSE,
                mainAccount.getId(),
                new BigDecimal("20000.00"),
                "Перевод на резервный счет",
                transferOut.getId())
        );

        // Пополнение с главного счета
        Operation transferInOp = operationFacade.createOperation(
                Operation.create(
                        OperationType.INCOME,
                        reserveAccount.getId(),
                        new BigDecimal("20000.00"),
                        "Пополнение с главного счета",
                        transferIn.getId())
        );

        System.out.println("Операции пользовaтеля: ");
        for (Operation op : operationFacade.getAllOperations()) {
            System.out.println(op.toString());
        }
        System.out.println();
        // Выведем все счета пользователя и отслеживаемые категории
        System.out.println("Банковские счета  ПОСЛЕ ОПЕРАЦИЙ: ");
        for (BankAccount account : accountFacade.getAllAccounts()) {
            System.out.println(account.toString());
        }
        System.out.println();

        // Почистим репозитории перед следующим сценарием
        clearRepositoryFacade.clearAll();
    }
}
