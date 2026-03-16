package org.tigerbank.finance.command;

public interface Command {

    public String getName();

    public void execute();
}
