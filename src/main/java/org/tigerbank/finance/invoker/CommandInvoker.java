package org.tigerbank.finance.invoker;
import org.springframework.stereotype.Component;
import org.tigerbank.finance.command.Command;

@Component
public class CommandInvoker {
    public void runCommand(Command command) {
        System.out.println("=".repeat(15) + "CЦЕНАРИЙ " + command.getName() + "=".repeat(15));
        long startTime = System.currentTimeMillis();
        try {
            System.out.println();
            command.execute();
        } catch (Exception e) {
            System.out.println("Ошибка выполнения команды " + command.getName() + ": " + e.getMessage());
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("-".repeat(10) + "Статистика" + "-".repeat(10));
            System.out.println("Время выполнения команды " + command.getName() + ": " + duration + "мс");
            System.out.println();
         }
    }
}
