package org.tigerbank.finance.command.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.command.Command;
import org.tigerbank.finance.facade.IAccountFacade;
import org.tigerbank.finance.facade.ICategoryFacade;
import org.tigerbank.finance.facade.impl.ClearRepositoryFacade;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;

import java.math.BigDecimal;

@Component("InitializeUserFinanceCommand")
@Scope("prototype")
public class InitializeUserFinanceCommand implements Command {

    private final IAccountFacade accountFacade;
    private final ICategoryFacade categoryFacade;
    private final ClearRepositoryFacade clearRepositoryFacade;

    public InitializeUserFinanceCommand(IAccountFacade accountFacade,
                                        ICategoryFacade categoryFacade,
                                        ClearRepositoryFacade clearRepositoryFacade)
                                        {
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.clearRepositoryFacade = clearRepositoryFacade;
    }

    @Override
    public String getName() {
        return "InitializeUserFinanceCommand";
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
        System.out.println("Банковские счета: ");
        for (BankAccount account : accountFacade.getAllAccounts()) {
            System.out.println(account.toString());
        }

        System.out.println("Отслеживаемые категории: ");
        for (Category cat : categoryFacade.getAllCategories()) {
            System.out.println(cat.toString());
        }

        // Почистим репозитории перед следующим сценарием
        clearRepositoryFacade.clearAll();
    }
}
