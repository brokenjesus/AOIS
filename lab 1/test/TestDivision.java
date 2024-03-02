import by.lupach.binarynum.core.FixedPointerBinaryNum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDivision {
    @Test
    public void TestDivisionPositiveNums(){
        FixedPointerBinaryNum num1 = new FixedPointerBinaryNum(253F);
        FixedPointerBinaryNum num2 = new FixedPointerBinaryNum(3F);

        Assertions.assertEquals(num1.divide(num2),"01010100.01010");
    }

    @Test
    public void TestDivisionNegativeNums(){
        FixedPointerBinaryNum num1 = new FixedPointerBinaryNum(-253F);
        FixedPointerBinaryNum num2 = new FixedPointerBinaryNum(-3F);

        Assertions.assertEquals(num1.divide(num2),"01010100.01010");
    }

    @Test
    public void TestDivisionFirstNegativeSecondPositive(){
        FixedPointerBinaryNum num1 = new FixedPointerBinaryNum(-253F);
        FixedPointerBinaryNum num2 = new FixedPointerBinaryNum(3F);

        Assertions.assertEquals(num1.divide(num2),"11010100.01010");
    }

    @Test
    public void TestDivisionFirstPositiveSecondNegative(){
        FixedPointerBinaryNum num1 = new FixedPointerBinaryNum(-253F);
        FixedPointerBinaryNum num2 = new FixedPointerBinaryNum(3F);

        Assertions.assertEquals(num1.divide(num2),"11010100.01010");
    }
}
