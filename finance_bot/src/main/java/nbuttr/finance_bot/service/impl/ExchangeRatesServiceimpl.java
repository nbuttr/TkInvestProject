package nbuttr.finance_bot.service.impl;

import nbuttr.finance_bot.client.FinanceClient;
import nbuttr.finance_bot.exception.ServiceException;
import nbuttr.finance_bot.service.ExchangeRatesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ExchangeRatesServiceimpl implements ExchangeRatesService {
    public static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    public static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    public static final String AED_XPATH = "/ValCurs//Valute[@ID='R01230']/Value";
    public static final String CNY_XPATH = "/ValCurs//Valute[@ID='R01375']/Value";

    @Autowired
    private FinanceClient client;
    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xml = client.getCurrentRatesXml();
        return ExtractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xml = client.getCurrentRatesXml();
        return ExtractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    @Override
    public String getAEDExchangeRate() throws ServiceException {
        var xml = client.getCurrentRatesXml();
        return ExtractCurrencyValueFromXML(xml, AED_XPATH);
    }

    @Override
    public String getCNYExchangeRate() throws ServiceException {
        var xml = client.getCurrentRatesXml();
        return ExtractCurrencyValueFromXML(xml, CNY_XPATH);
    }
    private static String ExtractCurrencyValueFromXML(String xml, String xpathExpression) throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        try{
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/",source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression,document);

        }catch(XPathExpressionException e){
            throw new ServiceException("Не удалось распарсить XML", e);

        }
    }
}
