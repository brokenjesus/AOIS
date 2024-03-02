package by.lupach.binarynum.core;

public class FloatingPointerBinaryNum extends BinaryNum{
    public static int SIZE_MANTISSA = 23;

    public static int SIZE_EXPONENT = 8;

    private String floatingPointerBinaryNumValue;

    public FloatingPointerBinaryNum(float decimalValue) {
        super(decimalValue);
        floatingPointerBinaryNumValue = decimalToIEEE754FloatingPointNumbers(decimalValue);
    }

    public char getSign() {
        return floatingPointerBinaryNumValue.charAt(0);
    }


    @Override
    public void print() {
        System.out.println("Decimal: \n" + decimalValue);
        System.out.println("Floating Pointer: \n" + floatingPointerBinaryNumValue);
        System.out.println("\n\n");
    }

    public String getFloatingPointerBinaryNumValue() {
        return floatingPointerBinaryNumValue;
    }

    public void updateBinaryNumValuesViaIEEE754() {
        this.decimalValue = this.convertIEEE754ToDecimal();
        floatingPointerBinaryNumValue = decimalToIEEE754FloatingPointNumbers(this.decimalValue);
    }


    protected int binaryToDecimal(String binary) {
        int result = 0;
        for (int i = 0; i < binary.length(); i++) {
            int bit = Character.getNumericValue(binary.charAt(i));
            result = result * 2 + bit;
        }
        return result;
    }
    public String getMantissaIEEE754() {
        return this.floatingPointerBinaryNumValue.substring(9);
    }

    public String getExponentIEEE754() {
        return this.floatingPointerBinaryNumValue.substring(1, 9);
    }

    public String add(FloatingPointerBinaryNum num2) {
        char sign1 = this.getSign();
        char sign2 = num2.getSign();

        String exponent1 = this.getExponentIEEE754();
        String exponent2 = num2.getExponentIEEE754();

        int num1ExponentDecimalValue = binaryToDecimal(exponent1);
        int num2ExponentDecimalValue = binaryToDecimal(exponent2);

        int exponentDifference = Math.abs(num1ExponentDecimalValue - num2ExponentDecimalValue);

        String alignedMantissa1 = this.getAlignedMantissa(num1ExponentDecimalValue, num2ExponentDecimalValue, exponentDifference);
        String alignedMantissa2 = num2.getAlignedMantissa(num2ExponentDecimalValue, num1ExponentDecimalValue, exponentDifference);

        char resultSign = getIEEE754SumResultSign(sign1, sign2, alignedMantissa1, alignedMantissa2);
        String sumMantissa = getIEEE754SumMantissa(sign1, sign2, alignedMantissa1, alignedMantissa2);
        String updatedExponent = updateExponent(exponent1, exponent2, alignedMantissa1, alignedMantissa2, sumMantissa);

        sumMantissa = sumMantissa.substring(1);

        this.floatingPointerBinaryNumValue = resultSign + updatedExponent + sumMantissa;
        this.updateBinaryNumValuesViaIEEE754();

        return this.floatingPointerBinaryNumValue;
    }

    private char getIEEE754SumResultSign(char sign1, char sign2, String alignedMantissa1, String alignedMantissa2) {
        if (sign1 == sign2) {
            return sign1;
        } else {
            boolean mantissa1GreaterOrEqualMantissa2 = isGreaterOrEqual(alignedMantissa1, alignedMantissa2);
            return mantissa1GreaterOrEqualMantissa2 ? sign1 : sign2;
        }
    }

    private String getIEEE754SumMantissa(char sign1, char sign2, String alignedMantissa1, String alignedMantissa2) {
        if (sign1 == sign2) {
            return sumMantissas(alignedMantissa1, alignedMantissa2);
        } else {
            boolean mantissa1GreaterOrEqualMantissa2 = isGreaterOrEqual(alignedMantissa1, alignedMantissa2);
            return mantissa1GreaterOrEqualMantissa2 ? subtractMantissa(alignedMantissa1, alignedMantissa2) :
                    subtractMantissa(alignedMantissa2, alignedMantissa1);
        }
    }

    private String subtractMantissa(String binary1, String binary2) {
        int maxLength = Math.max(binary1.length(), binary2.length());
        binary1 = alignLength(binary1, maxLength);
        binary2 = alignLength(binary2, maxLength);

        int borrow = 0;
        StringBuilder resultMantissa = new StringBuilder();

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

            resultMantissa.insert(0, difference);
        }

        return resultMantissa.toString();
    }

    private String alignMantissa(int exponentDifference, Boolean isSmallerExponent) {
        StringBuilder alignedMantissa = new StringBuilder(this.getMantissaIEEE754());

        if (isSmallerExponent) {
            alignedMantissa.insert(0, '1');
            for (int i = 0; i < exponentDifference; i++) {
                alignedMantissa.insert(0, '0');
            }
        } else {
            alignedMantissa.insert(0, '1');
            alignedMantissa.append("0".repeat(Math.max(0, exponentDifference)));
        }

        return alignedMantissa.toString();
    }

    private String getAlignedMantissa(int num1ExponentDecimalValue, int num2ExponentDecimalValue, int exponentDifference) {
        String alignedMantissa;
        if (num1ExponentDecimalValue > num2ExponentDecimalValue) {
            alignedMantissa = this.alignMantissa(exponentDifference, false);
        } else {
            alignedMantissa = this.alignMantissa(exponentDifference, true);
        }
        return alignedMantissa;
    }

    private String sumMantissas(String mantissa1, String mantissa2) {
        int maxLength = Math.max(mantissa1.length(), mantissa2.length());
        mantissa1 = alignLength(mantissa1, maxLength);
        mantissa2 = alignLength(mantissa2, maxLength);

        int carry = 0;
        StringBuilder sumMantissa = new StringBuilder();

        for (int i = maxLength - 1; i >= 0; i--) {
            int bit1 = Character.getNumericValue(mantissa1.charAt(i));
            int bit2 = Character.getNumericValue(mantissa2.charAt(i));

            int sum = bit1 + bit2 + carry;
            sumMantissa.insert(0, sum % 2);
            carry = sum / 2;
        }

        if (carry > 0) {
            sumMantissa.insert(0, '1');
        }

        return sumMantissa.toString();
    }

    private String updateExponent(String exponent1, String exponent2, String mantissa1, String mantissa2, String sumMantissa) {
        int intExponent1 = binaryToDecimal(exponent1);
        int intExponent2 = binaryToDecimal(exponent2);

        int updatedExponent = Math.max(intExponent1, intExponent2);

        int mantissaLengthDifference = Math.abs(sumMantissa.length() - Math.max(mantissa1.length(), mantissa2.length()));

        updatedExponent += mantissaLengthDifference;

        StringBuilder updatedExponentBinary = new StringBuilder(decimalToUnsignedBinary(updatedExponent));

        while (updatedExponentBinary.length() < SIZE_EXPONENT) {
            updatedExponentBinary.insert(0, "0");
        }

        return updatedExponentBinary.toString();
    }

    public String decimalToIEEE754FloatingPointNumbers(float decimalValue) {
        StringBuilder result = new StringBuilder();
        char sign = (decimalValue < 0) ? '1' : '0';

        String binaryIntegerPartOfFloat = decimalToUnsignedBinary((int) Math.abs(decimalValue));

        StringBuilder mantissa = new StringBuilder(binaryIntegerPartOfFloat.substring(1));
        mantissa.append(convertIEEE754FractionalPart(decimalValue));

        if (mantissa.length() > SIZE_MANTISSA) {
            mantissa = new StringBuilder(mantissa.substring(0, SIZE_MANTISSA));
        }

        while (mantissa.length() < SIZE_MANTISSA) {
            mantissa.append('0');
        }

        String exponent = calculateExponent(binaryIntegerPartOfFloat);

        result.append(sign).append(exponent).append(mantissa);

        return result.toString();

    }

    public String calculateExponent(String binaryIntegerPartOfFloat) {
        int degree = binaryIntegerPartOfFloat.length() - 1;
        return decimalToUnsignedBinary(127 + degree);
    }

    public String convertIEEE754FractionalPart(float decimalValue) {
        StringBuilder result = new StringBuilder();
        float fractionalPart = Math.abs(decimalValue % 1);

        for (int i = 0; i < SIZE_MANTISSA - 1; i++) {
            fractionalPart *= 2;

            int toAppend = (int) (fractionalPart - (fractionalPart) % 1);
            result.append(toAppend);
            fractionalPart %= 1;
        }

        return result.toString();
    }

    public float convertIEEE754ToDecimal() {
        char sign = this.getSign();
        String exponentBits = this.floatingPointerBinaryNumValue.substring(1, 9);
        String mantissaBits = this.floatingPointerBinaryNumValue.substring(9);

        int exponent = binaryToDecimal(exponentBits) - 127;
        double mantissa = calculateMantissa(mantissaBits);

        return (float) ((sign == '1' ? -1 : 1) * Math.pow(2, exponent) * mantissa);
    }

    private double calculateMantissa(String mantissaBits) {
        double mantissa = 1.0;
        for (int i = 0; i < mantissaBits.length(); i++) {
            int bit = Character.getNumericValue(mantissaBits.charAt(i));
            mantissa += bit * Math.pow(2, -(i + 1));
        }
        return mantissa;
    }

}
