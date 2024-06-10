package nbuttr.finance_bot.config;

import nbuttr.finance_bot.bot.FinanceBot;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class FinanceBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(FinanceBot financeBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(financeBot);
        return api;
    }
    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }
}