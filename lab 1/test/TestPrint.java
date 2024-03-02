import by.lupach.binarynum.core.SignedBinaryNum;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPrint {
    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Test
    public void TestPrint(){
        System.setOut(new PrintStream(outputStream));

        SignedBinaryNum num1 = new SignedBinaryNum(1);

        num1.print();
        assertTrue(outputStream.toString().trim().contains(
                    "Decimal: \n" + num1.getDecimalValue()
                )
        );

        assertTrue(outputStream.toString().trim().contains(
                    "magnitude (прямой): \n"+num1.getSignMagnitudeValue()
                )
        );

        assertTrue(outputStream.toString().trim().contains(
                    "ones (обратный): \n"+num1.getOnesComplementValue()
                )
        );

        assertTrue(outputStream.toString().trim().contains(
                    "twos (дополнительный): \n"+num1.getTwosComplementValue()
                )
        );
    }
}
