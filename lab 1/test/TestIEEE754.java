import by.lupach.binarynum.core.FloatingPointerBinaryNum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestIEEE754 {
    @Test
    public void TestSumPositives(){
        FloatingPointerBinaryNum first = new FloatingPointerBinaryNum(258250.12F);
        FloatingPointerBinaryNum second = new FloatingPointerBinaryNum(298349.13F);
        first.add(second);
        Assertions.assertEquals(556599.25F, first.getDecimalValue());
    }

    @Test
    public void TestSumNegatives(){
        FloatingPointerBinaryNum first = new FloatingPointerBinaryNum(-12.34F);
        FloatingPointerBinaryNum second = new FloatingPointerBinaryNum(-56.78F);
        String result = first.add(second);
        Assertions.assertEquals("11000010100010100011110101110000", result);
    }

    @Test
    public void TestSumFirstNegativeSecondPositive(){
        FloatingPointerBinaryNum first = new FloatingPointerBinaryNum(-56.78F);
        FloatingPointerBinaryNum second = new FloatingPointerBinaryNum(12.34F);
        first.add(second);
        Assertions.assertEquals(-44.44F, first.getDecimalValue());
    }

    @Test
    public void TestSumFirstPositiveSecondNegative(){
        FloatingPointerBinaryNum first = new FloatingPointerBinaryNum(-12.34F);
        FloatingPointerBinaryNum second = new FloatingPointerBinaryNum(56.78F);
        first.add(second);
        Assertions.assertEquals("01000010001100011100001010001111", first.getFloatingPointerBinaryNumValue());
    }
}
