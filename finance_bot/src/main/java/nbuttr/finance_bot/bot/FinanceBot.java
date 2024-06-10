package nbuttr.finance_bot.bot;

import com.google.gson.Gson;
import nbuttr.finance_bot.dto.PredictionDto;
import nbuttr.finance_bot.exception.ServiceException;
import nbuttr.finance_bot.service.ExchangeRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceBot extends TelegramLongPollingBot {
    public static final Logger LOG = LoggerFactory.getLogger(FinanceBot.class);
    private static final String START = "/start";
    private static boolean isFirstRun = true;

    @Autowired
    private ExchangeRatesService exchangeRatesService;
    public FinanceBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    private void sendInitialMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Что сделать:");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Выбрать ценную бумагу");
        row1.add("Посмотреть прогноз");
        keyboardMarkup.setKeyboard(List.of(row1));
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            if (isFirstRun) {
                sendInitialMessage(update.getMessage().getChatId());
                isFirstRun = false;
            }

            Message UserReply = update.getMessage();
            String text = UserReply.getText();
            Long chatId = update.getMessage().getChatId();
            switch (text){
                case "Выбрать ценную бумагу":
                    chooseStockOption(chatId);
                    break;
                case "Вернуться в главное меню":
                    sendInitialMessage(chatId);
                    break;
                case "Татнефть(TATN)":
                    sendStockForecast(chatId,"Прогноз на ближайшие дни: 740,25; 742,08; 738,64; 736,08");
                    break;
                case "Лукойл(LKOH)":
                    sendStockForecast(chatId,"Прогноз на ближайшие дни: 7655,12; 7655,25; 7700,65; 7750,12");
                    break;
                case "Сбербанк(SBER)":
                    sendStockForecast(chatId,"Прогноз на ближайшие дни: 319,17; 322,22; 321,12; 320,33");
                    break;
                case "Банк СПБ(BSPB)":
                    sendStockForecast(chatId,"Прогноз на ближайшие дни: 339,73; 341,42; 344,85; 343,34");
                    break;
                case "Магнит(MGNT)":
                    sendStockForecast(chatId, "Прогноз на ближайшие дни: 8444,00; 8510,21; 8560,12; 8567,66");
                    break;
                case "ТКС холдинг(TCSG)":
                    sendStockForecast(chatId, "Прогноз на ближайшие дни: 3100,00; 3110,21; 3130,41; 3120,14");
                    break;
                case "Посмотреть прогноз":
                    lengthOfRealForecast(chatId);
                    break;
                case "1 день":
                    getRealForecast(chatId, 1);
                    break;
                case "2 дня":
                    getRealForecast(chatId, 2);
                    break;
                case "3 дня":
                    getRealForecast(chatId, 3);
                    break;
                case "4 дня":
                    getRealForecast(chatId, 4);
                    break;
                case "5 дней":
                    getRealForecast(chatId, 5);
                    break;
                case START:
                    String userName = update.getMessage().getChat().getUserName();
                    startcommand(chatId, userName);
            }

        }
    }



    @Override
    public String getBotUsername() {
        return "My invest bot";
    }

    private void chooseStockOption(Long chatId){
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("Список доступных к прогнозу бумаг:");
        ReplyKeyboardMarkup keyboardMarkupP2 = new ReplyKeyboardMarkup();
        keyboardMarkupP2.setResizeKeyboard(true);
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Татнефть(TATN)");
        row3.add("Лукойл(LKOH)");
        row3.add("Сбербанк(SBER)");
        KeyboardRow row4 = new KeyboardRow();
        row4.add("Банк СПБ(BSPB)");
        row4.add("Магнит(MGNT)");
        row4.add("ТКС холдинг(TCSG)");
        KeyboardRow row5 = new KeyboardRow();
        row5.add("Вернуться в главное меню");
        keyboardMarkupP2.setKeyboard(List.of(row3, row4,row5));
        response.setReplyMarkup(keyboardMarkupP2);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendStockForecast(Long chatId, String forecast){
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(forecast);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }

    private void getRealForecast(Long chatId, Integer numberOfForecasts){
            SendMessage response = new SendMessage();
            response.setChatId(chatId.toString());
            RestTemplate restTemplate = new RestTemplate();
            //String urlDb = "http://localhost:8080/price/idsAndClosens";
            //List<?> resp = restTemplate.getForObject(urlDb,List.class);
            Map<String,Integer> json = new HashMap<>();
            json.put("input",numberOfForecasts);
            HttpEntity<Map<String,Integer>> requset = new HttpEntity<>(json);
            String urlModel = "http://0.0.0.0:8000/predict";
            String responsefrommodel = restTemplate.postForObject(urlModel,requset,String.class);
            Gson gson = new Gson();
            PredictionDto predictionDto = gson.fromJson(responsefrommodel, PredictionDto.class);

            response.setText("Прогноз: " + predictionDto.getPrediction());
            try {
                execute(response);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
    }

    private void lengthOfRealForecast(Long chatId){
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("На сколько дней сделать прогноз:");
        ReplyKeyboardMarkup keyboardMarkupP2 = new ReplyKeyboardMarkup();
        keyboardMarkupP2.setResizeKeyboard(true);
        KeyboardRow row3 = new KeyboardRow();
        row3.add("1 день");
        row3.add("2 дня");
        row3.add("3 дня");
        KeyboardRow row4 = new KeyboardRow();
        row4.add("4 дня");
        row4.add("5 дней");
        KeyboardRow row5 = new KeyboardRow();
        row5.add("Вернуться в главное меню");
        keyboardMarkupP2.setKeyboard(List.of(row3, row4,row5));
        response.setReplyMarkup(keyboardMarkupP2);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void startcommand(Long chatId, String userName){
        var text = """
                Добро пожаловать в бота, %s!
                Бот подскажет тебе куда пойдут акции(вверх, вниз или по кругу), не стесняйся, тыкай кнопки внизу:
                /menu
                
                Доп команды: 
                /help - получение справки
                . 
                .
                Under Construction :)
                """;
        var formattedText = String.format(text, userName);
        SendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesService.getUSDExchangeRate();
            var text = "Курс грязной зеленой бумажки на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch(ServiceException e){
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Неудалось получить курс";
        }
        SendMessage(chatId,formattedText);
    }
    private void eurCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesService.getEURExchangeRate();
            var text = "Курс грязной зеленой бумажки на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch(ServiceException e){
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Неудалось получить курс";
        }
        SendMessage(chatId,formattedText);
    }
    private void aedCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesService.getAEDExchangeRate();
            var text = "Курс грязной зеленой бумажки на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch(ServiceException e){
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Неудалось получить курс";
        }
        SendMessage(chatId,formattedText);
    }
    private void cnyCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesService.getCNYExchangeRate();
            var text = "Курс грязной зеленой бумажки на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch(ServiceException e){
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Неудалось получить курс";
        }
        SendMessage(chatId,formattedText);
    }
    private void unknownCommand(Long chatId){
        var text = "Не понял";
        SendMessage(chatId,text);
    }

    private void SendMessage(Long chatId, String text){
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try{
            execute(sendMessage);
        }catch(TelegramApiException e){
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
