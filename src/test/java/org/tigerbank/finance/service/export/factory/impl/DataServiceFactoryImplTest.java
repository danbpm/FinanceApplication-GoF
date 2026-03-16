package org.tigerbank.finance.service.export.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.service.export.DataService;
import org.tigerbank.finance.service.export.impl.JsonDataService;
import org.tigerbank.finance.service.export.impl.CsvDataService;
import org.tigerbank.finance.service.export.impl.YamlDataService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataServiceFactoryImplTest {

    private IBankAccountRepository accountRepo;
    private ICategoryRepository categoryRepo;
    private IOperationRepository operationRepo;
    private DataServiceFactoryImpl factory;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        operationRepo = mock(IOperationRepository.class);
        factory = new DataServiceFactoryImpl(accountRepo, categoryRepo, operationRepo);
    }

    @Test
    void getDataServiceJson() {
        DataService service = factory.getDataService("json");

        assertNotNull(service);
        assertTrue(service instanceof JsonDataService);
    }

    @Test
    void getDataServiceYaml() {
        DataService service = factory.getDataService("yaml");

        assertNotNull(service);
        assertTrue(service instanceof YamlDataService);
    }

    @Test
    void getDataServiceCsv() {
        DataService service = factory.getDataService("csv");

        assertNotNull(service);
        assertTrue(service instanceof CsvDataService);
    }

    @Test
    void getDataServiceCaseInsensitive() {
        DataService service1 = factory.getDataService("JSON");
        DataService service2 = factory.getDataService("Json");
        DataService service3 = factory.getDataService("json");

        assertNotNull(service1);
        assertNotNull(service2);
        assertNotNull(service3);
        assertTrue(service1 instanceof JsonDataService);
        assertTrue(service2 instanceof JsonDataService);
        assertTrue(service3 instanceof JsonDataService);
    }

    @Test
    void getDataServiceUnsupportedType() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.getDataService("xml")
        );
    }

    @Test
    void getDataServiceNullType() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.getDataService(null)
        );
    }

    @Test
    void getDataServiceEmptyType() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.getDataService("")
        );
    }

    @Test
    void getDataServiceReturnsNewInstanceEachTime() {
        DataService service1 = factory.getDataService("json");
        DataService service2 = factory.getDataService("json");

        assertNotSame(service1, service2);
    }
}