package com.learning.bot;

import com.learning.core.enums.OutputFormat;
import com.learning.core.exceptions.CommandTemplateError;
import com.learning.core.exceptions.GeneratorError;
import com.learning.core.exceptions.JSONTemplateError;
import com.learning.core.exceptions.SchemaError;
import com.learning.core.generator.BaseGenerator;
import com.learning.core.generator.JSONGenerator;
import com.learning.core.generator.SQLGenerator;
import com.learning.core.parsers.CommandParser;
import com.learning.core.parsers.JSONParser;
import com.learning.core.schema.Schema;
import io.github.cdimascio.dotenv.Dotenv;
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
    private final static String TOKEN;
    private final static String USERNAME;

    static {
        Dotenv dotenv = Dotenv.load();
        TOKEN = dotenv.get("BOT_TOKEN");
        USERNAME = dotenv.get("BOT_USERNAME");
    }

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
        return Path.of(file.getAbsolutePath(), "temp", UUID.randomUUID().toString()).toString().replace("-", "");
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

    private Path generateFakeData(Path path) throws IOException {
        Schema schema = new JSONParser(Files.readString(path)).getSchema();
        return generateFakeData(schema);
    }

    private Path generateFakeData(String command) throws IOException {
        Schema schema = new CommandParser(command).getSchema();
        return generateFakeData(schema);
    }

    private Path generateFakeData(Schema schema) {
        BaseGenerator generator;
        if (schema.getFormat() == OutputFormat.SQL) {
            generator = new SQLGenerator(schema);
        } else {
            generator = new JSONGenerator(schema);
        }
        Path outputPath = Path.of(generatePath() + "." + schema.getFormat().name().toLowerCase());
        generator.save(outputPath);
        return outputPath;
    }

    private void sendResponseFile(Long chatId, Path outputPath) throws TelegramApiException {
        String ext = outputPath.toString().split("\\.")[1];
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(new InputFile(new File(outputPath.toString()), "output." + ext));
        execute(sendDocument);
    }

    private void sendTextMessage(Long chatId, String message) {
        try {
            execute(new SendMessage(chatId.toString(), message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        try {
            if (message.hasDocument() && message.getDocument().getFileName().endsWith(".json")) {
                String templatePath = downloadDocument(update.getMessage().getDocument());
                Path outputPath = generateFakeData(Path.of(templatePath));
                sendResponseFile(message.getChatId(), outputPath);
            } else if (message.hasText() && message.getText().equals("/start")) {
                execute(new SendMessage(message.getChatId().toString(), "Welcome! Bot for making fake data. For making data please send me template like json file"));
            } else if (message.hasText()) {
                Path outputPath = generateFakeData(message.getText());
                sendResponseFile(message.getChatId(), outputPath);
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        } catch (GeneratorError | JSONTemplateError | CommandTemplateError | SchemaError e) {
            sendTextMessage(message.getChatId(), e.getUserMessage());
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
