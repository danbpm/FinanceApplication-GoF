package org.tigerbank.finance.service.export.factory;


import org.tigerbank.finance.service.export.DataService;

public interface DataServiceFactory {
    DataService getDataService(String type);
}
