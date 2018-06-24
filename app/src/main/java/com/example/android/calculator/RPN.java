package com.example.android.calculator;

import java.util.LinkedList;

public class RPN {
    static boolean isDelimiter(char c) { // тру если пробел
        return c == ' ';
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
    }

    static int priority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    static void processOperator(LinkedList<Double> st, char op) {
        if (st.size() < 2){
            st.add(0.0);
        }else {
        double r = st.removeLast();
        double l = st.removeLast();
        switch (op) {
            case '+':
                st.add(l + r);
                break;
            case '-':
                st.add(l - r);
                break;
            case '*':
                st.add(l * r);
                break;
            case '/':
                st.add(l / r);
                break;
            case '%':
                l = l / 100;
                st.add(l * r);
                break;
            case '^':
                st.add(Math.pow(l, r));
                break;
        }
        }
    }

    public static String eval(String s)  {
        LinkedList<Double> st = new LinkedList<Double>();
        LinkedList<Character> op = new LinkedList<Character>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isDelimiter(c))
                continue;
            if (c == '(')
                op.add('(');
            else if (c == ')') {
                    while (op.getLast() != '(')
                        processOperator(st, op.removeLast());
                    op.removeLast();
            } else if (isOperator(c)) {
                while (!op.isEmpty() && priority(op.getLast()) >= priority(c))
                    processOperator(st, op.removeLast());
                op.add(c);
            } else {
                String operand = "";
                boolean isContainDot = false;
                while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
                    if (s.charAt(i) == '.') {
                        if (isContainDot) ;
                        else isContainDot = true;
                    }
                    operand += s.charAt(i++);
                }
                --i;
                st.add(Double.parseDouble(operand));
            }
        }
        while (!op.isEmpty())
            processOperator(st, op.removeLast());
        return st.get(0).toString();
    }
}
