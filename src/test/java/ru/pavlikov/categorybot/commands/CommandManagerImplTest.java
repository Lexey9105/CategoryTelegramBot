package ru.pavlikov.categorybot.commands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.bot.TelegramBotImpl;
import ru.pavlikov.categorybot.commands.handlers.*;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandManagerImplTest {

    @Mock
    private TelegramBotImpl categoryBot;
    @Mock
    private CommandService commandService;
    @Mock
    private HelpCommand helpCommand;
    @Mock
    private AddElementCommand addElementCommand;
    @Mock
    private ViewTreeCommand viewTreeCommand;
    @Mock
    private RemoveElementCommand removeElement;
    @Mock
    private UploadCommand uploadCommand;
    @Mock
    private DownloadCommand downloadCommand;
    @Mock
    private SendMessageUtils sendMessageUtils;

    @InjectMocks
    private CommandManagerImpl commandManager;

    static Stream<Arguments> paramsForTest() {
        return Stream.of(
                Arguments.of("/removeAll", false, "removeAll"),
                Arguments.of("/viewTree", false, "viewTree"),
                Arguments.of("/upload", false, "upload"),
                Arguments.of("/download", false, "download"),
                Arguments.of("/help", false, "help"),
                Arguments.of("/start", false, "start")
        );
    }
    static Stream<Arguments> paramsForTest2() {
        return Stream.of(
                Arguments.of("/addElement <Корневая категория>", true, "addElement"),
                Arguments.of("/removeElement <Корневая категория>", true, "removeElement")
        );
    }


    @ParameterizedTest
    @MethodSource("paramsForTest")
    void updateParserAndPerformUserCommandTest(String commandText, boolean hasCaption, String expectedCommand) throws TelegramApiException, IOException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(commandText);

        Command command = new Command();
        command.setChatId(123L);
        command.setCommand(expectedCommand);

        when(commandService.addUserCommand(anyString(), anyLong())).thenReturn(command);



        commandManager.updateParserAndPerform(update);


        verify(commandService).addUserCommand(anyString(), anyLong());
        }

    @ParameterizedTest
    @MethodSource("paramsForTest2")
    void updateParserAndPerformAddAndRemoveTest(String commandText, boolean hasCaption, String expectedCommand) throws TelegramApiException, IOException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn(commandText);

        Command command = new Command();
        command.setChatId(123L);
        command.setCommand(expectedCommand);

        when(commandService.addUserCommand(anyString(), anyLong())).thenReturn(command);
        when(commandService.addParentAndChildren(any(), anyString())).thenReturn(command);


        commandManager.updateParserAndPerform(update);


        verify(commandService).addUserCommand(anyString(), anyLong());
        verify(commandService).addParentAndChildren(eq(command), anyString());
    }

    @Test
    void updateParserAndPerformUploadTest() throws TelegramApiException, IOException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(message.getCaption()).thenReturn("/upload");

        Command command = new Command();
        command.setChatId(123L);
        command.setCommand("/upload");
        command.setFile(new File("src/test/fileForTest/Categories.xlsx"));

        when(commandService.addUserCommand(any(Update.class), anyLong())).thenReturn(command);


        commandManager.updateParserAndPerform(update);


        verify(commandService).addUserCommand(any(Update.class), anyLong());
    }
    }


