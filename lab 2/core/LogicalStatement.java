package by.lupach.logicalstatements.core;

import java.util.*;

public class LogicalStatement {
    private final String expression;
    private final String reversePolishNotation;
    private Map<Character, Boolean> variableValues;
    List<Map<Character, Boolean>> truthTable;


    public String getExpression() {
        return expression;
    }

    public String getReversePolishNotation() {
        return reversePolishNotation;
    }


    public List<Map<Character, Boolean>> getTruthTable() {
        return (!truthTable.isEmpty()) ? truthTable : generateTruthTable();
    }

    public LogicalStatement(String expression) {
        this.expression = expression;
        this.reversePolishNotation = infixToPostfix();
        this.variableValues = new HashMap<>();
        setDefaultVariableValues();
        this.truthTable = new ArrayList<>();
        truthTable = generateTruthTable();
    }

    private void setDefaultVariableValues() {
        for (char ch : expression.toCharArray()) {
            if (Character.isLetter(ch)) {
                variableValues.put(ch, false);
            }
        }
    }


    private String infixToPostfix() {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                postfix.append(ch);
            } else if (isOperator(ch)) {
                while (!stack.isEmpty() && hasHigherPrecedence(stack.peek(), ch)) {
                    postfix.append(stack.pop());
                }
                stack.push(ch);
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }

        return postfix.toString();
    }

    private boolean isOperator(char ch) {
        return ch == '!' || ch == '&' || ch == '|' || ch == '>' || ch == '~';
    }

    private int getPrecedence(char operator) {
        return switch (operator) {
            case '!' -> 5;
            case '&' -> 4;
            case '|' -> 3;
            case '~' -> 2;
            case '>' -> 1;
            default -> 0;
        };
    }

    private boolean hasHigherPrecedence(char operator1, char operator2) {
        int precedence1 = getPrecedence(operator1);
        int precedence2 = getPrecedence(operator2);

        return precedence1 > precedence2;
    }

    public boolean evaluate() {
        Stack<Boolean> stack = new Stack<>();

        for (char ch : reversePolishNotation.toCharArray()) {
            if (Character.isLetter(ch)) {
                boolean variableValue = getVariableValue(ch);
                stack.push(variableValue);
            } else if (isOperator(ch)) {
                performOperation(ch, stack);
            }
        }

        return stack.pop();
    }

    private void performOperation(char operator, Stack<Boolean> stack) {
        switch (operator) {
            case '!' -> performNotOperation(stack);
            case '&' -> performAndOperation(stack);
            case '|' -> performOrOperation(stack);
            case '>' -> performImplicationOperation(stack);
            case '~' -> performEquivalenceOperation(stack);
        }
    }

    private void performNotOperation(Stack<Boolean> stack) {
        boolean operand = stack.pop();
        stack.push(!operand);
    }

    private void performAndOperation(Stack<Boolean> stack) {
        boolean operand2 = stack.pop();
        boolean operand1 = stack.pop();
        stack.push(operand1 && operand2);
    }

    private void performOrOperation(Stack<Boolean> stack) {
        boolean operand2 = stack.pop();
        boolean operand1 = stack.pop();
        stack.push(operand1 || operand2);
    }

    private void performImplicationOperation(Stack<Boolean> stack) {
        boolean operand2 = stack.pop();
        boolean operand1 = stack.pop();
        stack.push(!operand1 || operand2);
    }

    private void performEquivalenceOperation(Stack<Boolean> stack) {
        boolean operand2 = stack.pop();
        boolean operand1 = stack.pop();
        stack.push(operand1 == operand2);
    }

    public void setVariableValue(char variable, boolean value) {
        variableValues.put(variable, value);
    }

    public boolean getVariableValue(char variable) {
        return variableValues.getOrDefault(variable, false);
    }

    public List<Map<Character, Boolean>> generateTruthTable() {
        List<Character> variables = new ArrayList<>(variableValues.keySet());

        int totalCombinations = (int) Math.pow(2, variables.size());
        for (int i = 0; i < totalCombinations; i++) {
            Map<Character, Boolean> row = new LinkedHashMap<>();
            for (int j = 0, k = variables.size() - 1; j < variables.size(); j++, k--) {
                char variable = variables.get(j);
                boolean value = ((i >> k) & 1) == 1;
                setVariableValue(variable, value);
                row.put(variable, value);
            }

            boolean result = evaluate();
            row.put('R', result);
            truthTable.add(row);
        }


        return truthTable;
    }


    public void printTruthTable() {
        System.out.println("Truth Table:");
        for (char variable : variableValues.keySet()) {
            System.out.print(variable + "\t\t");
        }
        System.out.println("| Result");

        for (int i = 0; i < variableValues.size(); i++) {
            System.out.print("--------");
        }
        System.out.println("---------");

        for (Map<Character, Boolean> row : truthTable) {
            for (boolean value : row.values()) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    public String generateSOPForm() {       //sdnf


        List<Character> variables = new ArrayList<>(variableValues.keySet());
        StringBuilder sopForm = new StringBuilder();

        for (Map<Character, Boolean> row : truthTable) {
            boolean result = row.get('R');
            if (result) {
                if (!sopForm.isEmpty()) {
                    sopForm.append(" | ");
                }
                sopForm.append("(");
                for (Map.Entry<Character, Boolean> entry : row.entrySet()) {
                    char variable = entry.getKey();
                    boolean value = entry.getValue();
                    if (variable != 'R') {
                        sopForm.append(value ? variable : "!" + variable);
                    }
                    if (variable != 'R' && entry.getKey() != variables.get(variables.size() - 1)) {
                        sopForm.append(" & ");
                    }
                }
                sopForm.append(")");
            }
        }

        return sopForm.toString();
    }

    public String getNumericSOP() {


        StringBuilder numericSOP = new StringBuilder();
        for (int i = 0; i < truthTable.size(); i++) {
            Map<Character, Boolean> row = truthTable.get(i);
            boolean resultBool = row.get('R');
            if (resultBool) {
                numericSOP.append(i).append(" ");
            }
        }
        return numericSOP.toString();
    }

    public String generatePOSForm() {


        List<Character> variables = new ArrayList<>(variableValues.keySet());
        StringBuilder posForm = new StringBuilder();


        for (Map<Character, Boolean> row : truthTable) {
            boolean result = row.get('R');
            if (!result) {
                if (!posForm.isEmpty()) {
                    posForm.append(" & ");
                }
                posForm.append("(");
                for (Map.Entry<Character, Boolean> entry : row.entrySet()) {
                    char variable = entry.getKey();
                    boolean value = entry.getValue();
                    if (variable != 'R') {
                        posForm.append(value ? "!" + variable : variable);
                    }
                    if (variable != 'R' && entry.getKey() != variables.get(variables.size() - 1)) {
                        posForm.append(" | ");
                    }
                }
                posForm.append(")");
            }
        }

        return posForm.toString();
    }

    public String getNumericPOS() {


        StringBuilder numericSOP = new StringBuilder();
        for (int i = 0; i < truthTable.size(); i++) {
            Map<Character, Boolean> row = truthTable.get(i);
            boolean resultBool = row.get('R');
            if (!resultBool) {
                numericSOP.append(i).append(" ");
            }
        }
        return numericSOP.toString();
    }

    public int getIndexFunctionForm() {


        StringBuilder binary = new StringBuilder();

        for (Map<Character, Boolean> row : truthTable) {
            boolean result = row.get('R');
            binary.append((result) ? 1 : 0);
        }

        System.out.println(binary);

        return Integer.parseInt(binary.toString(), 2);
    }
}
