import by.lupach.binarynum.core.SignedBinaryNum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSubtract {
    @Test
    public void TestSubtractPositiveNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(250);
        SignedBinaryNum num2 = new SignedBinaryNum(249);

        Assertions.assertEquals(num1.subtract(num2),"01");
    }

    @Test
    public void TestSubtractNegativeNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(-250);
        SignedBinaryNum num2 = new SignedBinaryNum(-249);

        Assertions.assertEquals(num1.subtract(num2),"11");
    }

    @Test
    public void TestSubtractFirstNegativeSecondPositiveNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(-250);
        SignedBinaryNum num2 = new SignedBinaryNum(249);

        Assertions.assertEquals(num1.subtract(num2),"1000001101");
    }

    @Test
    public void TestSubtractFirstPositiveSecondNegativeNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(250);
        SignedBinaryNum num2 = new SignedBinaryNum(-249);

        Assertions.assertEquals(num1.subtract(num2),"0111110011");
    }
}
