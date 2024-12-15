package com.example.onlinecoachingmanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Calculator");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Coaching Management");
        }
    }
    TextView tvsec, tvMain;
    Button bac, bc, bbrac1, bbrac2, bsin, bcos, btan, blog, bln, bfact, bsquare, bsqrt, binv, b0, b9, b8, b7, b6, b5, b4, b3, b2, b1, bpi, bmul, bminus, bplus, bequal, bdot, bdiv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        tvsec = view.findViewById(R.id.idTVSecondary);
        tvMain = view.findViewById(R.id.idTVprimary);
        bac = view.findViewById(R.id.bac);
        bc = view.findViewById(R.id.bc);
        bbrac1 = view.findViewById(R.id.bbrac1);
        bbrac2 = view.findViewById(R.id.bbrac2);
        bsin = view.findViewById(R.id.bsin);
        bcos = view.findViewById(R.id.bcos);
        btan = view.findViewById(R.id.btan);
        blog = view.findViewById(R.id.blog);
        bln = view.findViewById(R.id.bln);
        bfact = view.findViewById(R.id.bfact);
        bsquare = view.findViewById(R.id.bsquare);
        bsqrt = view.findViewById(R.id.bsqrt);
        binv = view.findViewById(R.id.binv);
        b0 = view.findViewById(R.id.b0);
        b9 = view.findViewById(R.id.b9);
        b8 = view.findViewById(R.id.b8);
        b7 = view.findViewById(R.id.b7);
        b6 = view.findViewById(R.id.b6);
        b5 = view.findViewById(R.id.b5);
        b4 = view.findViewById(R.id.b4);
        b3 = view.findViewById(R.id.b3);
        b2 = view.findViewById(R.id.b2);
        b1 = view.findViewById(R.id.b1);
        bpi = view.findViewById(R.id.bpi);
        bmul = view.findViewById(R.id.bmul);
        bminus = view.findViewById(R.id.bminus);
        bplus = view.findViewById(R.id.bplus);
        bequal = view.findViewById(R.id.bequal);
        bdot = view.findViewById(R.id.bdot);
        bdiv = view.findViewById(R.id.bdiv);


        b1.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "1"));
        b2.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "2"));
        b3.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "3"));
        b4.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "4"));
        b5.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "5"));
        b6.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "6"));
        b7.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "7"));
        b8.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "8"));
        b9.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "9"));
        b0.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "0"));
        bdot.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "."));
        bplus.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "+"));
        bdiv.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "/"));
        bbrac1.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "("));
        bbrac2.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + ")"));
        bpi.setOnClickListener(v -> {
            tvMain.setText(tvMain.getText().toString() + "3.142");
            tvsec.setText(bpi.getText().toString());
        });
        bsin.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "sin"));
        bcos.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "cos"));
        btan.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "tan"));
        binv.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "^" + "(-1)"));
        bln.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "ln"));
        blog.setOnClickListener(v -> tvMain.setText(tvMain.getText().toString() + "log"));

        bminus.setOnClickListener(v -> {
            String str = tvMain.getText().toString();
            if (!str.endsWith("-")) {
                tvMain.setText(tvMain.getText().toString() + "-");
            }
        });

        bmul.setOnClickListener(v -> {
            String str = tvMain.getText().toString();
            if (!str.endsWith("*")) {
                tvMain.setText(tvMain.getText().toString() + "*");
            }
        });

        bsqrt.setOnClickListener(v -> {
            if (tvMain.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a valid number..", Toast.LENGTH_SHORT).show();
            } else {
                String str = tvMain.getText().toString();
                double r = Math.sqrt(Double.parseDouble(str));
                tvMain.setText(String.valueOf(r));
            }
        });

        bequal.setOnClickListener(v -> {
            String str = tvMain.getText().toString();
            double result = evaluate(str);
            tvMain.setText(String.valueOf(result));
            tvsec.setText(str);
        });

        bac.setOnClickListener(v -> {
            tvMain.setText("");
            tvsec.setText("");
        });

        bc.setOnClickListener(v -> {
            String str = tvMain.getText().toString();
            if (!str.equals("")) {
                str = str.substring(0, str.length() - 1);
                tvMain.setText(str);
            }
        });

        bsquare.setOnClickListener(v -> {
            if (tvMain.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a valid number..", Toast.LENGTH_SHORT).show();
            } else {
                double d = Double.parseDouble(tvMain.getText().toString());
                double square = d * d;
                tvMain.setText(String.valueOf(square));
                tvsec.setText(d + "²");
            }
        });

        bfact.setOnClickListener(v -> {
            if (tvMain.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a valid number..", Toast.LENGTH_SHORT).show();
            } else {
                int value = Integer.parseInt(tvMain.getText().toString());
                int fact = factorial(value);
                tvMain.setText(String.valueOf(fact));
                tvsec.setText(value + "!");
            }
        });
       return view;

    }
    private int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }

    private double evaluate(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("log")) x = Math.log10(x);
                    else if (func.equals("ln")) x = Math.log(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }.parse();
    }
}