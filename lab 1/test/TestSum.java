import by.lupach.binarynum.core.SignedBinaryNum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSum {
    @Test
    public void TestSumPositiveNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(250);
        SignedBinaryNum num2 = new SignedBinaryNum(1);

        Assertions.assertEquals(num1.add(num2),"011111011");
    }

    @Test
    public void TestSumNegativeNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(-250);
        SignedBinaryNum num2 = new SignedBinaryNum(-249);

        Assertions.assertEquals(num1.add(num2),"1000001101");
    }

    @Test
    public void TestSumFirstNegativeSecondPositiveNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(-250);
        SignedBinaryNum num2 = new SignedBinaryNum(249);

        Assertions.assertEquals(num1.add(num2),"11");
    }

    @Test
    public void TestSumFirstPositiveSecondNegativeNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(250);
        SignedBinaryNum num2 = new SignedBinaryNum(-249);

        Assertions.assertEquals(num1.add(num2),"01");
    }
}