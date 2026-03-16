package org.tigerbank.finance.service.export;

import org.tigerbank.finance.dto.FinanceData;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;

import java.io.IOException;

public abstract class DataService {
    protected final IBankAccountRepository accountRepo;
    protected final ICategoryRepository categoryRepo;
    protected final IOperationRepository operationRepo;

    public DataService(IBankAccountRepository accountRepo,
                ICategoryRepository categoryRepo,
                IOperationRepository operationRepo) {
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.operationRepo = operationRepo;
    }

    public abstract void exportToFile(String filename) throws IOException;

    // Импорт - шаблонный метод
    public  void importFromFile(String filename) throws IOException {
        // очищаем репозитории перед импортом
        clearRepositories();

        // вызываем переопределенный метод
        FinanceData data = parseFromFile(filename);

        // сохраняем данные в репозитории
        saveDataToRepositories(data);

        System.out.println("Импорт завершён");
        System.out.println("   Счетов: " + data.accounts.size());
        System.out.println("   Категорий: " + data.categories.size());
        System.out.println("   Операций: " + data.operations.size());
    }

    public void clearRepositories() {
        accountRepo.findAll().forEach(a -> accountRepo.deleteById(a.getId()));
        categoryRepo.findAll().forEach(c -> categoryRepo.deleteById(c.getId()));
        operationRepo.findAll().forEach(o -> operationRepo.deleteById(o.getId()));
    }

    protected  abstract FinanceData parseFromFile(String filename) throws IOException;

    public void saveDataToRepositories(FinanceData data) {
        data.accounts.forEach(accountRepo::save);
        data.categories.forEach(categoryRepo::save);
        data.operations.forEach(operationRepo::save);
    }

}