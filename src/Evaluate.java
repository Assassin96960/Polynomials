import java.util.*;

public class Evaluate {
    //  all mathematical signs
    final static String SIGNS = "+-*/^";
    //  function that removes the brackets
    public static String simplify(String expression) {
//      if there is no brackets in the expression, just evaluates it
        if (!expression.contains("(") && !expression.contains(")")) {
            return String.valueOf(eval(expression));
        } else {
//          while the expression contains brackets
            while (expression.contains("(") && expression.contains(")")) {
//              the position of the first opening bracket
                int o_pos = expression.indexOf('(');
//              position of the corresponding closing bracket
                int c_pos = 0;
//              opening brackets count
                int oc = 0;
//              closing brackets count
                int cc = 0;
//              iterate through the entire string and count the brackets
                for (int i = expression.indexOf('('); i < expression.length(); i++) {
//                  counts the opening brackets
                    if (expression.charAt(i) == '(') {
                        oc++;
                    }
//                  counts the closing brackets
                    if (expression.charAt(i) == ')') {
                        cc++;
                    }
//                  checks if the opening and closing brackets number are equal, and if so, break the loop
                    if (oc == cc) {
                        c_pos = i;
                        break;
                    }

                }
//              if there are only one pair of brackets, then evaluate the expression in the brackets and replace it with the result
                if (oc == 1) {
                    expression = expression.replace(expression.substring(o_pos, c_pos + 1), simplify(expression.substring(o_pos + 1, c_pos)));
                }
/*
              if there are other brackets inside the main brackets, evaluate the expression in the brackets and replace it with the result.
              Do the same recursively until there are only one pair of brackets and execute the if statement above
*/
                else {
                    expression = expression.replace(expression.substring(o_pos + 1, c_pos), simplify(expression.substring(o_pos + 1, c_pos)));
                }
            }

        }
//      there is no brackets, return the expression
        return expression;
    }

    public static double eval(String expression) {
//      simplifies the expression if there is brackets
        if (expression.contains("(")) {
            expression = simplify(expression);
        }
//      using the getToken method to split the expression by + and -
        /*      example 3^2+4*2^3-7*8/2 will be split into
         *       3^2, 4*2^3, 7*8/2   */
        List<String> lower = getTokens(expression, "+-");
        List<String>[] higher = new ArrayList[lower.size()];
//      gets the sequence of the + and -
        List<Character> lower_del = getDelimiters(expression, "+-");
        List<Character>[] higher_del = new ArrayList[lower.size()];
//      splits every element of lower by * and / and gets the sequence of * and /
        for (int i = 0; i < higher.length; i++) {
            higher[i] = getTokens(lower.get(i), "*/");
            higher_del[i] = getDelimiters(lower.get(i), "*/");
        }
//      iterates through all the equations
        for (int i = 0; i < higher.length; i++) {
            for (int j = 0; j < higher[i].size(); j++) {
                if (higher[i].get(j).contains("^")) {
//                  gets the powers in reversed order (because they are evaluated that way)
                    Stack<String> powers = new Stack<>();
                    powers.addAll(getTokens(higher[i].get(j), "^"));
                    while (powers.size() != 1) {
                        double p1 = Double.parseDouble(powers.pop());
                        double p = Double.parseDouble(powers.pop());
                        powers.add(String.valueOf(Math.pow(p, p1)));
                    }
//                  changes the expression at the beginning with the result
                    higher[i].set(j, powers.remove(0));
                }
            }
//          equates the multiplication and division
            while (higher[i].size() != 1) {
                char action = higher_del[i].remove(0);
                if (action == '*') {
                    higher[i].add(0, String.valueOf(Double.parseDouble(higher[i].remove(0)) * Double.parseDouble(higher[i].remove(0))));
                } else if (action == '/') {
                    higher[i].add(0, String.valueOf(Double.parseDouble(higher[i].remove(0)) / Double.parseDouble(higher[i].remove(0))));
                }
            }
//          changes the expression with the result
            lower.set(i, higher[i].get(0));
        }
//      equates the addition and subtraction
        while (lower.size() > 1) {
            char action = lower_del.remove(0);
            if (action == '+') {
                lower.add(0, String.valueOf(Double.parseDouble(lower.remove(0)) + Double.parseDouble(lower.remove(0))));
            } else if (action == '-') {
                lower.add(0, String.valueOf(Double.parseDouble(lower.remove(0)) - Double.parseDouble(lower.remove(0))));
            }
        }
//      parses the result to double and sets precision 6 digits
        Double result = Double.valueOf(lower.get(0));
        String s = String.format("%.6f", result);
        return Double.parseDouble(s);
    }
    //  method that gets the numbers from the string
    private static List<String> getTokens(String expression, String delimiter) {
        if(expression.isEmpty()) return new ArrayList<>();
        String buff = "";
        int is_first_sign = 0;
//      if the first char is + or -
        if (expression.charAt(0) == '+' || expression.charAt(0) == '-' /*&&(delimiter.contains("+") || delimiter.contains("-"))*/) {
            buff += expression.charAt(0);
            is_first_sign = 1;
        }
        List<String> res = new ArrayList<>();
//      variable that keeps whether the previous symbol is sign
        boolean was_previous_sign = false;
        for (int i = is_first_sign; i < expression.length(); i++) {
//          checks if the current symbol is delimiter
            if (!delimiter.contains("" + expression.charAt(i))) {
                buff += expression.charAt(i);
//              in the case where + or - is not in the delimiters
                was_previous_sign = SIGNS.contains("" + expression.charAt(i));
            } else {
//              if the previous wasn't a sign, then add the buffer to the numbers
                if (!was_previous_sign) {
                    res.add(buff);
                    buff = "";
                    was_previous_sign = true;
//              eif the previous was a sign, the that is negative number or number with + in front of
                } else {
                    buff += expression.charAt(i);
                }
            }
        }
//      finally adds the final number and returns the result
        if(!buff.isEmpty())
            res.add(buff);
        return res;
    }

    private static List<Character> getDelimiters(String expression, String delimiter) {
        List<Character> res = new ArrayList<>();
        if(expression.isEmpty()) return new ArrayList<>();
        int is_first_sign = 0;
//      checks if the first is + or -
        if (expression.charAt(0) == '+' || expression.charAt(0) == '-') {
            is_first_sign = 1;
        }
//      variable that keeps whether the previous symbol is sign
        boolean was_previous_sign = false;
        for (int i = is_first_sign; i < expression.length(); i++) {
//          checks if the current char is delimiter
            if (delimiter.contains("" + expression.charAt(i))) {
//              adds it only if the previous wasn't sign, otherwise ignores it
                if (!was_previous_sign) {
                    res.add(expression.charAt(i));
                    was_previous_sign = true;
                }
            } else {
//              check if previous was a sign
                was_previous_sign = SIGNS.contains("" + expression.charAt(i));
            }
        }
        return res;
    }

}
