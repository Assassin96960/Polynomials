
import java.util.*;
import java.util.Scanner;
public class Polynomial {
    public static String solve(String expression)
    {
        String solutions;
        double a=0;
        double b=0;
        double c=0;
        double d;
        String constant=expression;
        for(int i=0;i<expression.length();i++)
        {
            if(expression.charAt(i)=='x')
            {

                    int j=i-1;
                    if(j==-1)
                    {
                        if(expression.charAt(i+1)=='^')
                        {
                            a=a+1;
                        }
                        else
                        {
                            b=b+1;
                        }
                    }
                    else {
                        while (j > 0) {
                            if (expression.charAt(j) == '-' || expression.charAt(j) == '+')
                            {
                                d = Evaluate.eval(expression.substring(j, i));
                                if (expression.charAt(i + 1) == '^')
                                {
                                    a = a + d;
                                }
                                else
                                {
                                    b = b + d;
                                    expression=expression.substring(0, j)+expression.substring(i+1, expression.length());
                                }
                            }
                            j--;
                            if(j==0)
                            {
                                d = Evaluate.eval(expression.substring(j, i));
                                if (expression.charAt(i + 1) == '^')
                                {
                                    a = a + d;
                                    //Taking the new string
                                    expression=expression.substring(i+3, constant.length());
                                    i=-1;
                                }
                                else
                                {
                                    b = b + d;
                                    expression=expression.substring(i+1, constant.length());
                                    i=-1;
                                }
                            }

                        }

                    }
            }
            if(expression.charAt(i)=='=')
            {
                c=Evaluate.eval(expression.substring(0, i));
                break;
            }
        }
        String as=Double.toString(a);
        String.format("%.2f", as);
        String bs=Double.toString(a);
        String.format("%.2f", as);
        String cs=Double.toString(a);
        String.format("%.2f", as);
        solutions=as+" "+bs+" "+ cs;
        return solutions;
    }

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        String expression =sc.nextLine();
        String sol=solve(expression);
    }
}
