package org.tigerbank.finance.command.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.command.Command;
import org.tigerbank.finance.facade.IAccountFacade;
import org.tigerbank.finance.facade.ICategoryFacade;
import org.tigerbank.finance.facade.IOperationFacade;
import org.tigerbank.finance.facade.impl.ClearRepositoryFacade;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.service.export.DataService;
import org.tigerbank.finance.service.export.factory.DataServiceFactory;

import java.io.IOException;

@Component("RepairFromFileCommand")
@Scope("prototype")
public class RepairFromFileCommand implements Command {
   private final IAccountFacade accountFacade;
   private final IOperationFacade operationFacade;
   private final ICategoryFacade categoryFacade;
   private final DataServiceFactory dataServiceFactory;
   private final ClearRepositoryFacade clearRepositoryFacade;

   public RepairFromFileCommand(IAccountFacade accountFacade,
                                IOperationFacade operationFacade,
                                ICategoryFacade categoryFacade,
                                DataServiceFactory dataServiceFactory,
                                ClearRepositoryFacade clearRepositoryFacade) {
       this.accountFacade = accountFacade;
       this.operationFacade = operationFacade;
       this.categoryFacade = categoryFacade;
       this.dataServiceFactory = dataServiceFactory;
       this.clearRepositoryFacade = clearRepositoryFacade;
   }

    @Override
    public String getName() {
       return "RepairFromFileCommand";
    }

    @Override
    public void execute() {
        System.out.println("Количество счетов: " + accountFacade.getAllAccounts().size());
        System.out.println("Количество категорий: " + categoryFacade.getAllCategories().size());
        System.out.println("Количество операций: " + operationFacade.getAllOperations().size());

        System.out.println("\n" + "=".repeat(5) + "Восстановление из JSON" + "=".repeat(5));
        DataService jsonDs = dataServiceFactory.getDataService("json");
        try {
            jsonDs.importFromFile("data/");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nСОСТОЯНИЕ РЕПОЗИТОРИЕВ ПОСЛЕ ВОССТАНОВЛЕНИЯ");
        System.out.println("--- Счета ");
        for (BankAccount account : accountFacade.getAllAccounts())  {
            System.out.println(account.toString());
        }
        System.out.println("--- Категории ");
        for (Category category : categoryFacade.getAllCategories())  {
            System.out.println(category.toString());
        }
        System.out.println("--- Операции ");
        for (Operation op : operationFacade.getAllOperations())  {
            System.out.println(op.toString());
        }

        System.out.println("\nОЧИЩАЕМ РЕПОЗИТОРИИ");
        clearRepositoryFacade.clearAll();

        System.out.println("Количество счетов: " + accountFacade.getAllAccounts().size());
        System.out.println("Количество категорий: " + categoryFacade.getAllCategories().size());
        System.out.println("Количество операций: " + operationFacade.getAllOperations().size());

        // ================ Восстановление из YAML =====================
        System.out.println("\n" + "=".repeat(5) + "Восстановление из YAML" + "=".repeat(5));
        DataService yamlDs = dataServiceFactory.getDataService("yaml");
        try {
            yamlDs.importFromFile("data/");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\nСОСТОЯНИЕ РЕПОЗИТОРИЕВ ПОСЛЕ ВОССТАНОВЛЕНИЯ");
        System.out.println("--- Счета");
        for (BankAccount account : accountFacade.getAllAccounts())  {
            System.out.println(account.toString());
        }
        System.out.println("--- Категории");
        for (Category category : categoryFacade.getAllCategories())  {
            System.out.println(category.toString());
        }
        System.out.println("--- Операции");
        for (Operation op : operationFacade.getAllOperations())  {
            System.out.println(op.toString());
        }


        System.out.println("\nОЧИЩАЕМ РЕПОЗИТОРИИ");
        clearRepositoryFacade.clearAll();

        System.out.println("Количество счетов: " + accountFacade.getAllAccounts().size());
        System.out.println("Количество категорий: " + categoryFacade.getAllCategories().size());
        System.out.println("Количество операций: " + operationFacade.getAllOperations().size());

        // ================ Восстановление из CSV =====================

        System.out.println("\n" +"=".repeat(5) + "Восстановление из CSV" + "=".repeat(5));
        DataService csvDs = dataServiceFactory.getDataService("csv");
        try {
            csvDs.importFromFile("data/");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\nСОСТОЯНИЕ РЕПОЗИТОРИЕВ ПОСЛЕ ВОССТАНОВЛЕНИЯ");
        System.out.println("--- Счета");
        for (BankAccount account : accountFacade.getAllAccounts())  {
            System.out.println(account.toString());
        }
        System.out.println("--- Категории");
        for (Category category : categoryFacade.getAllCategories())  {
            System.out.println(category.toString());
        }
        System.out.println("--- Операции");
        for (Operation op : operationFacade.getAllOperations())  {
            System.out.println(op.toString());
        }
    }

}
