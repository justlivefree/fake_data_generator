package com.learning.bot;

import com.learning.core.enums.OutputFormat;
import com.learning.core.exceptions.InvalidTemplateFile;
import com.learning.core.generator.BaseGenerator;
import com.learning.core.generator.JSONGenerator;
import com.learning.core.generator.SQLGenerator;
import com.learning.core.parsers.JSONTemplateParser;
import com.learning.core.schema.Schema;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String TOKEN = "6302242715:AAFZP3my-tjBHt1cvxG6kBCszZqUWI_qWeE";
    private static final String USERNAME = "@dcolapebot";

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    public String generatePath() {
        File file = new File("");
        return Path.of(file.getAbsolutePath(), "temp", UUID.randomUUID().toString()).toString();
    }

    public String downloadDocument(Document document) {
        try {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(document.getFileId());
            org.telegram.telegrambots.meta.api.objects.File tgFile = execute(getFileMethod);
            File file = downloadFile(tgFile.getFilePath());
            return file.getAbsolutePath();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private Path generateFakeData(String templatePath) throws IOException {
        JSONTemplateParser jsonParser = new JSONTemplateParser(Files.readString(Path.of(templatePath)));
        Schema schema = jsonParser.getSchema();
        BaseGenerator generator;
        if (schema.getFormat() == OutputFormat.SQL) {
            generator = new SQLGenerator(schema);
        } else {
            generator = new JSONGenerator(schema);
        }
        Path outputPath = Path.of(generatePath() + schema.getFormat().name().toLowerCase());
        generator.save(outputPath);
        return outputPath;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        try {
            if (message.hasDocument() && message.getDocument().getFileName().endsWith(".json")) {
                String templatePath = downloadDocument(update.getMessage().getDocument());
                Path outputPath = generateFakeData(templatePath);
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(message.getChatId());
                sendDocument.setDocument(new InputFile(new File(outputPath.toString())));
                execute(sendDocument);
            } else if (message.hasText() && message.getText().equals("/start")) {
                execute(
                        new SendMessage(
                                message.getChatId().toString(),
                                "Welcome! Bot for making fake data. For making data please send me template like json file"
                        )
                );
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        } catch (InvalidTemplateFile e) {
            try {
                execute(new SendMessage(
                        message.getChatId().toString(),
                        "Invalid json file"
                ));
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            TelegramBot mySuperBot = new TelegramBot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(mySuperBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
