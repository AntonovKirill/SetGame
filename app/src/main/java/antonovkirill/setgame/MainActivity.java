package antonovkirill.setgame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public EditText nicknameEditText;
    public TextView alert;
    public TextView score;
    public CardsView cardsView;
    public DBHelper dbHelper;
    public ServerInteractor set;


    public static class Card {
        int count;
        int color;
        int shape;
        int fill;

        @Override
        public boolean equals(Object o) {
            if (null == o)
                return false;
            if(this == o)
                return true;
            if (!(o instanceof Card))
                return false;
            Card c = (Card) o;

            return count == c.count && color == c.color && shape == c.shape && fill == c.fill;
        }

        public Card() {}
    }


    public void register(View view) throws ExecutionException, InterruptedException {
        String nickname = nicknameEditText.getText().toString();
        SQLiteDatabase db = dbHelper.db;
        Cursor c = db.rawQuery("SELECT token FROM " + DBHelper.TABLE_NAME +
                " WHERE nickname = '" + nickname + "';", null);

        int token;

        if (c.moveToFirst()) {
            int answer = c.getInt(0);
            c.close();
            token = answer;
        }
        else {
            token = set.register(nickname);

            if (token == -1) {
                alert.setText(R.string.wrong_nickname);
            } else if (token == -2) {
                alert.setText(R.string.connection_problem);
            } else {
                dbHelper.addNicknameToken(nickname, token);
            }
        }

        setContentView(R.layout.start_game);
        set.token = token;
        score = findViewById(R.id.score);
        cardsView = findViewById(R.id.cards);

        newGame();
    }


    public void newGame() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                cardsView.status[i][j] = 0;
            }
        }
        score.setText("0");

        fetch();
    }


    public void findSet(View view) throws ExecutionException, InterruptedException {
        int n = 12;
        Card[] cardsList = new Card[n];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                cardsList[4 * i + j] = cardsView.cards[i][j];
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = j + 1; k < n; ++k) {
                    Card[] cards = new Card[3];
                    cards[0] = cardsList[i];
                    cards[1] = cardsList[j];
                    cards[2] = cardsList[k];

                    HashSet<Integer> count = new HashSet<>(), color = new HashSet<>(), shape = new HashSet<>(), fill = new HashSet<>();
                    for (int t = 0; t < 3; ++t) {
                        count.add(cards[t].count);
                        color.add(cards[t].color);
                        shape.add(cards[t].shape);
                        fill.add(cards[t].fill);
                    }

                    if (!(count.size() == 2 || color.size() == 2 || shape.size() == 2 || fill.size() == 2)) {
                        for (int i1 = 0; i1 < 3; ++i1) {
                            for (int j1 = 0; j1 < 4; ++j1) {
                                cardsView.status[i1][j1] = 0;
                            }
                        }

                        cardsView.status[i / 4][i % 4] = 2;
                        cardsView.status[j / 4][j % 4] = 2;
                        cardsView.status[k / 4][k % 4] = 2;

                        cardsView.invalidate();
                        return;
                    }
                }
            }
        }

        // set was not found
    }


    public void chooseCards(Card[] cards) throws ExecutionException, InterruptedException {
        set.chooseCards(cards);
        fetch();
    }


    public void checkSet(View view) throws ExecutionException, InterruptedException {
        ArrayList<Card> cardsList = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (cardsView.status[i][j] == 1) {
                    cardsList.add(cardsView.cards[i][j]);
                }
                if (cardsView.status[i][j] == 2) {
                    cardsView.status[i][j] = 0;
                }
            }
        }


        if (cardsList.size() < 3) {
            return;
        }

        while (cardsList.size() > 3) {
            cardsList.remove(cardsList.size() - 1);
        }

        Card[] cards = new Card[3];
        cards[0] = cardsList.get(0);
        cards[1] = cardsList.get(1);
        cards[2] = cardsList.get(2);

        HashSet<Integer> count = new HashSet<>(), color = new HashSet<>(), shape = new HashSet<>(), fill = new HashSet<>();
        for (int i = 0; i < 3; ++i) {
            count.add(cards[i].count);
            color.add(cards[i].color);
            shape.add(cards[i].shape);
            fill.add(cards[i].fill);
        }

        if (count.size() == 2 || color.size() == 2 || shape.size() == 2 || fill.size() == 2) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 4; ++j) {
                    cardsView.status[i][j] = 0;
                }
            }

            cardsView.invalidate();
        }
        else {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 4; ++j) {
                    cardsView.status[i][j] = 0;
                }
            }

            chooseCards(cards.clone());

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 4; ++j) {
                    if (cardsView.cards[i][j].equals(cards[0])) {
                        cardsView.cards[i][j].count = 0;
                    }
                }
            }
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 4; ++j) {
                    if (cardsView.cards[i][j].equals(cards[1])) {
                        cardsView.cards[i][j].count = 0;
                    }
                }
            }
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 4; ++j) {
                    if (cardsView.cards[i][j].equals(cards[2])) {
                        cardsView.cards[i][j].count = 0;
                    }
                }
            }

            Integer scores = Integer.parseInt(score.getText().toString());
            scores += 3;
            score.setText(scores.toString());
            cardsView.invalidate();
        }
    }


    public void fetch() throws ExecutionException, InterruptedException {
        Card[] cards = set.fetch();

        Log.d("after fetch", "" + cards.length);
        for (int k = 0; k < cards.length; ++k) {
            int i = k / 4;
            int j = k % 4;
            cardsView.cards[i][j] = cards[k];
        }

        cardsView.invalidate();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set = new ServerInteractor("http://194.176.114.21:8050/");

        nicknameEditText = findViewById(R.id.nickname);
        alert = findViewById(R.id.alert);
        dbHelper = new DBHelper(this);
    }

}



//package antonovkirill.setgame;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import java.util.concurrent.ExecutionException;
//
//public class MainActivity extends AppCompatActivity {
//
//    public EditText nicknameEditText;
//    public TextView alert;
//    public DBHelper dbHelper;
//    public ServerInteractor set;
//
//
//    public static class Card {
//        int count;
//        int color;
//        int shape;
//        int fill;
//
//        public Card() {}
//    }
//
//
//    public void register(View view) throws ExecutionException, InterruptedException {
//        String nickname = nicknameEditText.getText().toString();
//        SQLiteDatabase db = dbHelper.db;
//        Cursor c = db.rawQuery("SELECT token FROM " + DBHelper.TABLE_NAME +
//                " WHERE nickname = '" + nickname + "';", null);
//
//        int token;
//
//        if (c.moveToFirst()) {
//            int answer = c.getInt(0);
//            c.close();
//            token = answer;
//        }
//        else {
//            token = set.register(nickname);
//
//            if (token == -1) {
//                alert.setText(R.string.wrong_nickname);
//            } else if (token == -2) {
//                alert.setText(R.string.connection_problem);
//            } else {
//                dbHelper.addNicknameToken(nickname, token);
//            }
//        }
//
//
//        set.token = token;
//
//        Intent intent = new Intent(this, SecondActivity.class);
//        intent.putExtra("token", set.token);
//        startActivity(intent);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        set = new ServerInteractor("http://194.176.114.21:8050/");
//
//        nicknameEditText = findViewById(R.id.nickname);
//        alert = findViewById(R.id.alert);
//        dbHelper = new DBHelper(this);
//    }
//
//}
