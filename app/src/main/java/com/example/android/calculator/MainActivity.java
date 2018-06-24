package com.example.android.calculator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    StringBuilder StrFormula = new StringBuilder("");
    int numberOfBracket = 0;
    RPN rpn;

    TextView text_result, text_formula;
    int[] numbersBtnId = {R.id.Btn_0,R.id.Btn_1,R.id.Btn_2,R.id.Btn_3,R.id.Btn_4,R.id.Btn_5,R.id.Btn_6,R.id.Btn_7,R.id.Btn_8,R.id.Btn_9};
    ImageButton Erase;
    Button multiplication, equal, division, plus, minus, clean, Openbracket,Closebraket, dot, percent, exponent;

    RecyclerView RVstory;
    Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Item> story;

    private Dbhelper dbhelper;
    private SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelper = new Dbhelper(this);
        sqLiteDatabase = dbhelper.getWritableDatabase();

        RVstory = findViewById(R.id.story);
        RVstory.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        RVstory.setLayoutManager(layoutManager);

        story = new ArrayList<>();
        adapter = new Adapter(story);
        RVstory.setAdapter(adapter);

        Cursor cursor = sqLiteDatabase.query(Dbhelper.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int NameIndex = cursor.getColumnIndex(Dbhelper.KEY_FORMULA);
            int DescriptionIndex= cursor.getColumnIndex(Dbhelper.KEY_RESULT);

            do {
                Log.d("mLog","Мы добавляем в русулт");
                story.add(new Item(cursor.getString(NameIndex).toString(),cursor.getString(DescriptionIndex).toString()));
            }while (cursor.moveToNext());
        }
        cursor.close();

        text_result = (TextView) findViewById(R.id.text_result);
        text_formula = (TextView) findViewById(R.id.text_formula);

        Erase = findViewById(R.id.EraseBtn);
        Erase.setOnClickListener(this);

        for (int i = 0; i < 10; i++) {
            Button numberBtn = findViewById(numbersBtnId[i]);
            numberBtn.setOnClickListener(this);
        }

        multiplication = findViewById(R.id.Btn_multiplication);
        multiplication.setOnClickListener(this);

        equal = findViewById(R.id.Btn_equal);
        equal.setOnClickListener(this);

        division = findViewById(R.id.Btn_division);
        division.setOnClickListener(this);

        plus = findViewById(R.id.Btn_plus);
        plus.setOnClickListener(this);

        minus = findViewById(R.id.Btn_minus);
        minus.setOnClickListener(this);

        clean = findViewById(R.id.Btn_clean);
        clean.setOnClickListener(this);

        Openbracket = findViewById(R.id.Btn_Openbracket);
        Openbracket.setOnClickListener(this);

        Closebraket = findViewById(R.id.Btn_Closebracket);
        Closebraket.setOnClickListener(this);

        dot = findViewById(R.id.Btn_dot);
        dot.setOnClickListener(this);

        percent = findViewById(R.id.Btn_percent);
        percent.setOnClickListener(this);

        exponent = findViewById(R.id.Btn_exponent);
        exponent.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_clean:
                if (text_result.getText().toString()  == "") {
                    dbhelper.onUpgrade(sqLiteDatabase, 1, 1);
                    Log.v("TAG","База данных обновилась");
                    story.clear();
                    Log.v("TAG","Arraylist =" + story);
                    for (int position = 0; story.size() > position; position++) {
                        story.remove(position);
                        RVstory.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, story.size());
                    } adapter.notifyDataSetChanged();
                    break;
                }
                    StrFormula = new StringBuilder("");
                    text_result.setText("");
                    text_formula.setText("");
                    numberOfBracket = 0;
                    break;
            case R.id.EraseBtn:
                Log.v("TAG","ПРОВЕРКА");
                Erase();
                break;
            case R.id.Btn_multiplication:
                act("*");
                break;
            case R.id.Btn_division:
                act("/");
                break;
            case R.id.Btn_plus:
                act("+");
                break;
            case R.id.Btn_minus:
                act("-");
                break;
            case R.id.Btn_Openbracket:
                setValue("(");
                numberOfBracket++;
                break;
            case R.id.Btn_Closebracket:
                setValue(")");
                numberOfBracket++;
                break;
            case R.id.Btn_dot:
                act(".");
                break;
            case R.id.Btn_percent:
                act("%");
                break;
            case R.id.Btn_exponent:
                act("^");
                break;
            case R.id.Btn_equal:
                equals();
                break;
            case R.id.Btn_0:
                setValue("0");
                break;
            case R.id.Btn_1:
                setValue("1");
                break;
            case R.id.Btn_2:
                setValue("2");
                break;
            case R.id.Btn_3:
                setValue("3");
                break;
            case R.id.Btn_4:
                setValue("4");
                break;
            case R.id.Btn_5:
                setValue("5");
                break;
            case R.id.Btn_6:
                setValue("6");
                break;
            case R.id.Btn_7:
                setValue("7");
                break;
            case R.id.Btn_8:
                setValue("8");
                break;
            case R.id.Btn_9:
                setValue("9");
                break;
        }
    }

    private void setValue(String value) {
        StrFormula.append(value);
        text_result.setText(StrFormula);
    }

    private void act(String value) {
        int a = StrFormula.length() - 1;
        if ((StrFormula.toString() != "") && thisCharIsNOTOperration(StrFormula.charAt(a))) {
            setValue(value);
        }
    }

    private void equals() {
        if (readyForCalculation()) {
            rpn = new RPN();
            text_formula.setText(StrFormula);
            text_result.setText(rpn.eval(StrFormula.toString()));
            // TODO Вынести запись в БД в новый поток
            ContentValues contentValues = new ContentValues();
            contentValues.put(Dbhelper.KEY_FORMULA, StrFormula.toString());
            contentValues.put(Dbhelper.KEY_RESULT, rpn.eval(StrFormula.toString()));
            sqLiteDatabase.insert(Dbhelper.TABLE_CONTACTS, null, contentValues);
            story.add(0, new Item(StrFormula.toString(),rpn.eval(StrFormula.toString())));
            adapter.notifyDataSetChanged();
            adapter.notifyItemInserted(0);

            StrFormula = new StringBuilder("");
        }
    }
    private void Erase() {
        if (StrFormula != null && StrFormula.length() > 0) {
            StrFormula.deleteCharAt(StrFormula.length() - 1);
            text_result.setText(StrFormula);
        }
    }

    private boolean readyForCalculation() {
        int a = StrFormula.length() - 1;
        String s = StrFormula.toString();
        if (StrFormula.charAt(0) == '-') {
            StrFormula = new StringBuilder("0" + s);
            return true;
        }
        if (!s.equals("") && numberOfBracket % 2 == 0 && thisCharIsNOTOperration(StrFormula.charAt(0)) && thisCharIsNOTOperration(StrFormula.charAt(a))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean thisCharIsNOTOperration(char a) {
        if (a == '-'
                || a == '+'
                || a == '*'
                || a == '/'
                || a == '^'
                || a == '.') {
            return false;
        } else {
            return true;
        }
    }
}

