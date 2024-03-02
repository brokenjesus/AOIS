package by.lupach.binarynum.core;

public class FixedPointerBinaryNum extends BinaryNum {

    String fixedPointBinaryNumValue;

    public FixedPointerBinaryNum(float decimalValue) {
        super(decimalValue);
        this.fixedPointBinaryNumValue = decimalToFixedPointerBinaryNum(decimalValue);
    }

    public String decimalToFixedPointerBinaryNum(float decimal) {
        char sign = (decimalValue < 0) ? '1' : '0';
        int intPart = (int) decimal;
        float fractionalPart = decimal - intPart;

        StringBuilder fractionalBinary = new StringBuilder();
        String integerBinary = decimalToUnsignedBinary(intPart);

        fractionalBinary.append('.');
        for (int i = 0; i < 5; i++) {
            fractionalPart *= 2;
            int bit = (int) fractionalPart;
            fractionalBinary.append(bit);
            fractionalPart -= bit;
        }

        return sign + integerBinary + fractionalBinary;
    }

    public char getSign() {
        return fixedPointBinaryNumValue.charAt(0);
    }

    @Override
    public void print() {
        System.out.println("Decimal: \n" + decimalValue);
        System.out.println("Unsigned: \n" + unsignedBinaryNumValue);
        System.out.println("Floating Pointer: \n" + fixedPointBinaryNumValue);
        System.out.println("\n\n");
    }

    private static char getDivideSignBit(FixedPointerBinaryNum num1, FixedPointerBinaryNum num2) {
        return (num1.getSign() == '1' ^ num2.getSign() == '1') ? '1' : '0';
    }

    public String divide(FixedPointerBinaryNum divisor) {
        char sign = getDivideSignBit(this, divisor);

        String unsignedDividendIntPart = this.fixedPointBinaryNumValue.substring(1, this.fixedPointBinaryNumValue.indexOf('.'))
                + this.fixedPointBinaryNumValue.substring(this.fixedPointBinaryNumValue.indexOf('.') + 1);

        String unsignedDivisorIntPart = divisor.fixedPointBinaryNumValue.substring(1, divisor.fixedPointBinaryNumValue.indexOf('.'))
                + divisor.fixedPointBinaryNumValue.substring(divisor.fixedPointBinaryNumValue.indexOf('.') + 1);



        fixedPointBinaryNumValue = sign+performDivision(unsignedDividendIntPart, unsignedDivisorIntPart);
        updateValues(fixedPointBinaryNumValue);
        return fixedPointBinaryNumValue;
    }

    private String performDivision(String unsignedDividendIntPart, String unsignedDivisorIntPart){
        StringBuilder result = new StringBuilder();
        String remainder = "0";

        for (int i = 0; i < unsignedDividendIntPart.length(); i++) {
            remainder = remainder+unsignedDividendIntPart.charAt(i);
            if (isGreaterOrEqual(remainder, unsignedDivisorIntPart)){
                result.append("1");
                remainder = subtractBinary(remainder, unsignedDivisorIntPart);
            }else {
                result.append("0");
            }
        }

        result.append(".");

        for (int i = 0; i < 5; i++) {
            remainder = remainder+"0";
            if (isGreaterOrEqual(remainder, unsignedDivisorIntPart)){
                result.append("1");
                remainder = subtractBinary(remainder, unsignedDivisorIntPart);
            }else {
                result.append("0");
            }
        }

        return removeLeadingZeroes(result.toString());
    }

    private float fixedPointerBinaryNumToDecimal(String fixedPointBinaryNumValue){
        String intPart =  fixedPointBinaryNumValue.substring(1, this.fixedPointBinaryNumValue.indexOf('.'));
        String fractionalPart =  fixedPointBinaryNumValue.substring(this.fixedPointBinaryNumValue.indexOf('.') + 1);
        String result = signedBinaryToDecimal(intPart) + "." + signedBinaryToDecimal(fractionalPart);

        System.out.println(signedBinaryToDecimal(fractionalPart));

        return Float.parseFloat(result);
    }

    private int signedBinaryToDecimal(String binary) {
        int sign = (binary.charAt(0)=='1')?-1:1;

        int result = 0;
        for (int i = 0; i < binary.length(); i++) {
            int bit = Character.getNumericValue(binary.charAt(i));
            result = result * 2 + bit;
        }
        return sign*result;
    }

    private String removeLeadingZeroes(String binaryString) {
        int firstNonZeroIndex = 0;

        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1') {
                firstNonZeroIndex = i;
                break;
            }
        }

        if (firstNonZeroIndex == 0) {
            return "0";
        }

        return binaryString.substring(firstNonZeroIndex);
    }

    public String subtractBinary(String binary1, String binary2) {
        int maxLength = Math.max(binary1.length(), binary2.length());
        binary1 = alignLength(binary1, maxLength);
        binary2 = alignLength(binary2, maxLength);

        int borrow = 0;
        StringBuilder result = new StringBuilder();

        for (int i = maxLength - 1; i >= 0; i--) {
            int bit1 = Character.getNumericValue(binary1.charAt(i));
            int bit2 = Character.getNumericValue(binary2.charAt(i));

            int difference = bit1 - bit2 - borrow;

            if (difference < 0) {
                difference += 2;
                borrow = 1;
            } else {
                borrow = 0;
            }

            result.insert(0, difference);
        }

        return result.toString();
    }

    private void updateValues(String fixedPointBinaryNumValue){
        this.unsignedBinaryNumValue = fixedPointBinaryNumValue.substring(1,
                fixedPointBinaryNumValue.indexOf('.'));
        this.decimalValue = fixedPointerBinaryNumToDecimal(fixedPointBinaryNumValue);
    }

}