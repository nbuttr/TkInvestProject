package nbuttr.finance_bot.service;

import nbuttr.finance_bot.exception.ServiceException;
import org.jvnet.hk2.annotations.Service;


public interface ExchangeRatesService {
    String getUSDExchangeRate() throws ServiceException;
    String getEURExchangeRate() throws ServiceException;
    String getAEDExchangeRate() throws ServiceException;
    String getCNYExchangeRate() throws ServiceException;
}
