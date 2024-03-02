import by.lupach.binarynum.core.SignedBinaryNum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMultiply {
    @Test
    public void TestMultiplyPositiveNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(12345679);
        SignedBinaryNum num2 = new SignedBinaryNum(9);

        Assertions.assertEquals(num1.multiply(num2),"0110100111110110101111000111");
    }

    @Test
    public void TestMultiplyNegativeNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(9);
        SignedBinaryNum num2 = new SignedBinaryNum(12345679);

        Assertions.assertEquals(num1.multiply(num2),"0110100111110110101111000111");
    }

    @Test
    public void TestMultiplyFirstNegativeSecondPositiveNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(-12345679);
        SignedBinaryNum num2 = new SignedBinaryNum(9);

        Assertions.assertEquals(num1.multiply(num2),"1110100111110110101111000111");
    }

    @Test
    public void TestMultiplyFirstPositiveSecondNegativeNums(){
        SignedBinaryNum num1 = new SignedBinaryNum(12345679);
        SignedBinaryNum num2 = new SignedBinaryNum(-9);

        Assertions.assertEquals(num1.multiply(num2),"1110100111110110101111000111");
    }
}
