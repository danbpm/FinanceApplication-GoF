package org.tigerbank.finance.console;

import org.springframework.beans.factory.annotation.Qualifier;
import org.tigerbank.finance.command.Command;
import org.tigerbank.finance.command.impl.CreateAndApplyOperationsCommand;
import org.tigerbank.finance.command.impl.InitializeUserFinanceCommand;
import org.tigerbank.finance.command.impl.ShowStatisticsCommand;
import org.tigerbank.finance.invoker.CommandInvoker;
import org.tigerbank.finance.model.*;
import org.tigerbank.finance.repository.*;
import org.tigerbank.finance.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.service.export.factory.DataServiceFactory;

import java.io.IOException;

@Component
public class FinanceRunner implements CommandLineRunner {
    private final CommandInvoker invoker;
    private final Command useCase1;
    private final Command useCase2;
    private final Command useCase3;
    private final Command useCase4;
    private final Command useCase5;

    public FinanceRunner(CommandInvoker invoker,
                         @Qualifier("InitializeUserFinanceCommand") Command useCase1,
                         @Qualifier("CreateAndApplyOperationsCommand") Command useCas2,
                         @Qualifier("ShowStatisticsCommand") Command useCase3,
                         @Qualifier("ExportToFileCommand") Command useCase4,
                         @Qualifier("RepairFromFileCommand") Command useCase5)  {

        this.invoker = invoker;
        this.useCase1 = useCase1;
        this.useCase2 = useCas2;
        this.useCase3 = useCase3;
        this.useCase4 = useCase4;
        this.useCase5 = useCase5;
    }

    @Override
    public void run(String... args) throws IOException {
        System.out.println("ТИГРБАНК: Модуль «Учет финансов»\n");

        System.out.println("Запуск сценариев...");
        invoker.runCommand(useCase1);
        invoker.runCommand(useCase2);
        invoker.runCommand(useCase3);
        invoker.runCommand(useCase4);
        invoker.runCommand(useCase5);

        System.exit(0);
    }
}