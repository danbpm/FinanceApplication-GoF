package org.tigerbank.finance.invoker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.command.Command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandInvokerTest {

    private CommandInvoker invoker;

    @BeforeEach
    void setUp() {
        invoker = new CommandInvoker();
    }

    @Test
    void runCommandSuccessfulExecution() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("Тестовая команда");
        doNothing().when(command).execute();

        assertDoesNotThrow(() -> invoker.runCommand(command));

        verify(command).execute();
        verify(command, times(2)).getName();
    }

    @Test
    void runCommandThrowsException() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("Команда с ошибкой");
        doThrow(new RuntimeException("Ошибка выполнения")).when(command).execute();

        assertDoesNotThrow(() -> invoker.runCommand(command));

        verify(command).execute();
        verify(command, times(3)).getName();
    }

    @Test
    void runCommandWithNullCommand() {
        assertThrows(NullPointerException.class, () ->
                invoker.runCommand(null)
        );
    }

    @Test
    void runCommandMultipleTimes() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("Многократная команда");
        doNothing().when(command).execute();

        invoker.runCommand(command);
        invoker.runCommand(command);
        invoker.runCommand(command);

        verify(command, times(3)).execute();
        verify(command, times(6)).getName();
    }
}