package by.lupach.binarynum.core;

public class BinaryNum{
    protected float decimalValue;
    protected String unsignedBinaryNumValue;

    BinaryNum(float decimalValue){
        this.decimalValue = decimalValue;
        this.unsignedBinaryNumValue = decimalToUnsignedBinary((int)decimalValue);
    }

    public float getDecimalValue() {
        return decimalValue;
    }

    public void print() {
        System.out.println("Decimal: \n" + decimalValue);
        System.out.println("unsignedBinary: \n" + unsignedBinaryNumValue);
        System.out.println("\n\n");
    }

    public String decimalToUnsignedBinary(int decimal) {
        int dividend = Math.abs(decimal);

        StringBuilder binary = new StringBuilder();
        while (dividend > 0) {
            int remainder = dividend % 2;
            binary.insert(0, remainder);
            dividend /= 2;
        }

        return binary.toString();
    }

    protected String alignLength(String binary, int targetLength) {
        StringBuilder alignedBinary = new StringBuilder(binary);

        while (alignedBinary.length() < targetLength) {
            alignedBinary.insert(0, '0');
        }

        return alignedBinary.toString();
    }

    protected boolean isGreaterOrEqual(String binary1, String binary2) {
        if (binary1.length()>binary2.length()) {
            binary2 = alignLength(binary2, binary1.length());
        }else {
            binary1 = alignLength(binary1, binary2.length());
        }

        if (binary1.length()==binary2.length()){
            for (int i = 0; i < binary1.length(); i++) {
                int bit1 = Character.getNumericValue(binary1.charAt(i));
                int bit2 = Character.getNumericValue(binary2.charAt(i));

                if (bit1 > bit2) {
                    return true;
                } else if (bit1 < bit2) {
                    return false;
                }
            }
        }


        return true;
    }
}
