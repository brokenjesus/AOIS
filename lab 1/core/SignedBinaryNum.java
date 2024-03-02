package by.lupach.binarynum.core;

public class SignedBinaryNum extends BinaryNum {
    private String signMagnitudeValue;
    private String onesComplementValue;

    private String twosComplementValue;
    public SignedBinaryNum(int decimalValue) {
        super(decimalValue);
        signMagnitudeValue = decimalToSignMagnitude(decimalValue);
        onesComplementValue = decimalToOnesComplement(decimalValue);
        twosComplementValue = decimalToTwosComplement(decimalValue);
    }

    public String getSignMagnitudeValue() {
        return signMagnitudeValue;
    }

    public String getOnesComplementValue() {
        return onesComplementValue;
    }

    public String getTwosComplementValue() {
        return twosComplementValue;
    }

    @Override
    public void print() {
        System.out.println("Decimal: \n" + decimalValue);
        System.out.println("magnitude (прямой): \n" + signMagnitudeValue);
        System.out.println("ones (обратный): \n" + onesComplementValue);
        System.out.println("twos (дополнительный): \n" + twosComplementValue);
        System.out.println("\n\n");
    }

    public String decimalToSignMagnitude(int decimalValue) {
        int sign = (decimalValue < 0) ? 1 : 0;
        return sign + decimalToUnsignedBinary(decimalValue);
    }

    public String invertBitsExceptSign(String num) {
        char sign = num.charAt(0);
        StringBuilder result = new StringBuilder(num.substring(1));

        for (int i = 0; i < result.length(); i++) {
            result.setCharAt(i, (result.charAt(i) == '0') ? '1' : '0');
        }

        return sign + result.toString();
    }

    public String decimalToOnesComplement(int decimalValue) {
        int sign = (decimalValue < 0) ? 1 : 0;

        if (sign == 1) {
            return invertBitsExceptSign(decimalToSignMagnitude(decimalValue));
        }

        return decimalToSignMagnitude(decimalValue);
    }

    public String signMagnitudeToOnesComplement(String signMagnitude) {
        int sign = (signMagnitude.charAt(0) == '1') ? 1 : 0;

        if (sign == 1) {
            return invertBitsExceptSign(signMagnitude);
        }

        return signMagnitude;
    }

    public String decimalToTwosComplement(int decimalValue) {
        int sign = (decimalValue < 0) ? 1 : 0;
        StringBuilder result = new StringBuilder(decimalToUnsignedBinary(decimalValue));

        if (sign == 1) {
            boolean foundFirstOne = false;
            for (int i = result.length() - 1; i >= 0; i--) {
                if (foundFirstOne) {
                    result.setCharAt(i, (result.charAt(i) == '0') ? '1' : '0');
                }
                if (result.charAt(i) == '1') {
                    foundFirstOne = true;
                }
            }
        }

        return sign + result.toString();
    }

    public String signedMagnitudeToTwosComplement(String signMagnitudeValue) {
        char sign = (signMagnitudeValue.charAt(0) == '1') ? '1' : '0';
        StringBuilder result = new StringBuilder(signMagnitudeValue.substring(1));

        if (sign == '1') {
            boolean foundFirstOne = false;
            for (int i = result.length() - 1; i >= 0; i--) {
                if (foundFirstOne) {
                    result.setCharAt(i, (result.charAt(i) == '0') ? '1' : '0');
                }
                if (result.charAt(i) == '1') {
                    foundFirstOne = true;
                }
            }
        }

        return sign + result.toString();
    }

    public int signMagnitudeToDecimal(String signMagnitudeValue) {
        int sign = (signMagnitudeValue.charAt(0) == '0') ? 1 : -1;

        int result = 0;
        for (int i = 0; i < signMagnitudeValue.length() - 1; i++) {
            int pow = (int) (Math.pow(2, i));
            int value = Character.getNumericValue(signMagnitudeValue.charAt(signMagnitudeValue.length() - i - 1));
            result += pow * value;
        }
        return sign * result;
    }

    public void updateBinaryNumValuesViaSignMagnitude(String signMagnitudeValue) {
        this.signMagnitudeValue = signMagnitudeValue;
        this.onesComplementValue = signMagnitudeToOnesComplement(this.signMagnitudeValue);
        this.twosComplementValue = signedMagnitudeToTwosComplement(this.signMagnitudeValue);
        this.decimalValue = signMagnitudeToDecimal(this.signMagnitudeValue);
    }

    public void updateBinaryNumValuesViaTwosComplement(String twosComplementValue) {
        this.signMagnitudeValue = signedMagnitudeToTwosComplement(twosComplementValue);
        this.onesComplementValue = signMagnitudeToOnesComplement(this.signMagnitudeValue);
        this.twosComplementValue = twosComplementValue;
        this.decimalValue = signMagnitudeToDecimal(this.signMagnitudeValue);
    }


    @Override
    protected String alignLength(String binary, int targetLength) {
        StringBuilder alignedBinary = new StringBuilder(binary);

        while (alignedBinary.length() < targetLength) {
            char signBit = (binary.charAt(0) == '0') ? '0' : '1';
            alignedBinary.insert(1, signBit);
        }

        return alignedBinary.toString();
    }

    public String add(SignedBinaryNum num2) {
        String twosComplement1 = this.twosComplementValue;
        String twosComplement2 = num2.twosComplementValue;

        int maxLength = Math.max(twosComplement1.length(), twosComplement2.length());
        twosComplement1 = alignLength(twosComplement1, maxLength+1);
        twosComplement2 = alignLength(twosComplement2, maxLength+1);

        StringBuilder result = new StringBuilder();

        for (int i = maxLength, carry = 0; i >= 0; i--) {
            int bit1 = Character.getNumericValue(twosComplement1.charAt(i));
            int bit2 = Character.getNumericValue(twosComplement2.charAt(i));

            int sum = bit1 + bit2 + carry;
            result.insert(0, sum % 2);
            carry = sum / 2;
        }

        updateBinaryNumValuesViaTwosComplement(removeTwosComplementNonSignificantBits(result.toString()));
        return this.twosComplementValue;
    }

    private String removeTwosComplementNonSignificantBits(String twosComplementValue) {
        char signBit = twosComplementValue.charAt(0);

        int firstNonSignificantBitIndex = 1;
        while (firstNonSignificantBitIndex < twosComplementValue.length()-1 &&
                twosComplementValue.charAt(firstNonSignificantBitIndex) == signBit) {
            firstNonSignificantBitIndex++;
        }

        return signBit+twosComplementValue.substring(firstNonSignificantBitIndex);
    }


    public String subtract(SignedBinaryNum num2) {
        SignedBinaryNum subtracted = new SignedBinaryNum((int) num2.decimalValue * (-1));
        updateBinaryNumValuesViaTwosComplement(subtracted.add(this));
        return this.twosComplementValue;
    }

    private static String composeMultiplyResult(int[] products, SignedBinaryNum num1, SignedBinaryNum num2) {
        StringBuilder result = new StringBuilder();

        int firstNonZero = 0;
        while (firstNonZero < products.length && products[firstNonZero] == 0) {
            firstNonZero++;
        }

        for (int i = firstNonZero; i < products.length; i++) {
            result.append(products[i]);
        }

        result.insert(0, getMultiplySignBit(num1, num2));
        return result.toString();
    }

    private static char getMultiplySignBit(SignedBinaryNum num1, SignedBinaryNum num2) {
        return (num1.signMagnitudeValue.charAt(0) == '1' ^ num2.signMagnitudeValue.charAt(0) == '1') ? '1' : '0';
    }

    public String multiply(SignedBinaryNum num2) {
        String binary1 = this.signMagnitudeValue.substring(1);
        String binary2 = num2.signMagnitudeValue.substring(1);

        int length1 = binary1.length();
        int length2 = binary2.length();

        int[] products = new int[length1 + length2];

        for (int i = length1 - 1; i >= 0; i--) {
            int carry = 0;

            for (int j = length2 - 1; j >= 0; j--) {
                int bit1 = Character.getNumericValue(binary1.charAt(i));
                int bit2 = Character.getNumericValue(binary2.charAt(j));

                products[i + j + 1] += (bit1 * bit2 + carry);
                carry = products[i + j + 1] / 2;
                products[i + j + 1] %= 2;
            }

            products[i] += carry;
        }

        String result = composeMultiplyResult(products, this, num2);

        this.updateBinaryNumValuesViaSignMagnitude(result);
        return result;
    }

}
