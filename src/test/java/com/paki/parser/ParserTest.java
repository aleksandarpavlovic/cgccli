package com.paki.parser;

import com.paki.command.Assignment;
import com.paki.command.OperationCommand;
import com.paki.command.Operation;
import com.paki.command.Option;
import org.testng.Assert;
import org.testng.annotations.*;

public class ParserTest {

    private Parser parser;

    @BeforeMethod
    public void setUp() {
        parser = new Parser();
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(dataProvider = "WellStructuredCommands")
    public void testParseCommand(String[] tokens, OperationCommand expected) throws CGCParseException{
        OperationCommand command = (OperationCommand) parser.parseCommand(tokens);
        Assert.assertEquals(command, expected);
    }

    @DataProvider(name = "WellStructuredCommands")
    public Object[][] wellStructuredCommands() {
        String[] tokens1 = {"--optionName", "optionValue", "resourceName", "actionName"};
        Option option1 = new Option("optionName", "optionValue");
        Operation.Builder operationBuilder1 = new Operation.Builder();
        operationBuilder1.withResource("resourceName").withAction("actionName").build();
        OperationCommand.Builder commandBuilder1 = new OperationCommand.Builder();
        OperationCommand expected1 = commandBuilder1.withOption(option1).withOperation(operationBuilder1.build()).build();

        String[] tokens2 = {"--optionName1", "optionValue1", "resourceName", "actionName", "--optionName2", "optionValue2", "key=value", "--optionName3", "optionValue3"};
        Option option2_1 = new Option("optionName1", "optionValue1");
        Option option2_2 = new Option("optionName2", "optionValue2");
        Option option2_3 = new Option("optionName3", "optionValue3");
        Assignment assignment2 = new Assignment("key", "value");
        Operation.Builder operationBuilder2 = new Operation.Builder();
        operationBuilder2.withResource("resourceName").withAction("actionName").withOption(option2_2).withOption(option2_3).withAssignment(assignment2).build();
        OperationCommand.Builder commandBuilder2 = new OperationCommand.Builder();
        OperationCommand expected2 = commandBuilder2.withOption(option2_1).withOperation(operationBuilder2.build()).build();

        String[] tokens3 = {"resourceName", "actionName", "path=/path/to/dir/"};
        Assignment assignment3 = new Assignment("path", "/path/to/dir/");
        Operation.Builder operationBuilder3 = new Operation.Builder();
        operationBuilder3.withResource("resourceName").withAction("actionName").withAssignment(assignment3).build();
        OperationCommand.Builder commandBuilder3 = new OperationCommand.Builder();
        OperationCommand expected3 = commandBuilder3.withOperation(operationBuilder3.build()).build();

        String[] tokens4 = {"--token", "acd123", "files", "update", "--file", "file_id", "name=foo.bar", "path=/user/home/foo.bar", "metadata.foo=bar"};
        Option option4_1 = new Option("token", "acd123");
        Option option4_2 = new Option("file", "file_id");
        Assignment assignment4_1 = new Assignment("name", "foo.bar");
        Assignment assignment4_2 = new Assignment("path", "/user/home/foo.bar");
        Assignment assignment4_3 = new Assignment("metadata.foo", "bar");
        Operation.Builder operationBuilder4 = new Operation.Builder();
        operationBuilder4.withResource("files").withAction("update").withOption(option4_2).withAssignment(assignment4_1).withAssignment(assignment4_2).withAssignment(assignment4_3).build();
        OperationCommand.Builder commandBuilder4 = new OperationCommand.Builder();
        OperationCommand expected4 = commandBuilder4.withOption(option4_1).withOperation(operationBuilder4.build()).build();

        return new Object[][] {
                {tokens1, expected1},
                {tokens2, expected2},
                {tokens3, expected3},
                {tokens4, expected4}
        };
    }


}