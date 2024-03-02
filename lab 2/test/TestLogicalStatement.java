import by.lupach.logicalstatements.core.LogicalStatement;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalStatementTest {
    @Test
    void testReversePolishNotation(){
        LogicalStatement logicalStatement = new LogicalStatement("!x|(y&!z|x)&z");
        assertEquals("x!yz!&x|z&|",logicalStatement.getReversePolishNotation());
    }

    @Test
    void testExpressionEvaluation() {
        LogicalStatement logicalStatement = new LogicalStatement("A->B");
        logicalStatement.setVariableValue('A', false);
        logicalStatement.setVariableValue('B', true);
        boolean result = logicalStatement.evaluate();
        assertTrue(result);
    }

    @Test
    void testTruthTableGeneration() {
        LogicalStatement logicalStatement = new LogicalStatement("A~B");
        assertEquals(4, logicalStatement.getTruthTable().size());
    }


    @Test
    void testSOPFormGeneration() {
        // Arrange
        LogicalStatement logicalStatement = new LogicalStatement("!x|(y&!z|x)&z");

        // Act
        String sopForm = logicalStatement.generateSOPForm();

        // Assert
        assertEquals("(!x & !y & !z) | (!x & !y & z) | (!x & y & !z) | (!x & y & z) | (x & !y & z) | (x & y & z)", sopForm);
    }

    @Test
    void testPOSFormGeneration() {
        LogicalStatement logicalStatement = new LogicalStatement("!x|(y&!z|x)&z");
        String posForm = logicalStatement.generatePOSForm();
        assertEquals("(!x | y | z) & (!x | !y | z)", posForm);
    }

    @Test
    void testNumericSOP() {
        LogicalStatement logicalStatement = new LogicalStatement("!x|(y&!z|x)&z");
        String numericSOP = logicalStatement.getNumericSOP();
        assertEquals("0 1 2 3 5 7 ", numericSOP);
    }

    @Test
    void testNumericPOS() {
        LogicalStatement logicalStatement = new LogicalStatement("!x|(y&!z|x)&z");
        String numericPOS = logicalStatement.getNumericPOS();
        assertEquals("4 6 ", numericPOS);
    }

    @Test
    void testIndexFunctionForm() {
        LogicalStatement logicalStatement = new LogicalStatement("A~B");
        int index = logicalStatement.getIndexFunctionForm();
        assertEquals(9, index);
    }

    @Test
    void testPrintTruthTable() {
        LogicalStatement logicalStatement = new LogicalStatement("a&b");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        logicalStatement.printTruthTable();
        String printedTable = outputStream.toString().trim().replaceAll("\\R", "\n");

        String expectedTable = """
                Truth Table:
                a\t\tb\t\t| Result
                -------------------------
                false\tfalse\tfalse\t
                false\ttrue\tfalse\t
                true\tfalse\tfalse\t
                true\ttrue\ttrue""";

        assertEquals(expectedTable, printedTable);
    }
}
