//package antonovkirill.setgame;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.concurrent.ExecutionException;
//
//public class SecondActivity extends AppCompatActivity {
//
//    public TextView score;
//    public CardsView cardsView;
//    public ServerInteractor set;
//
//
//    public void newGame() throws ExecutionException, InterruptedException {
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 4; ++j) {
//                cardsView.active[i][j] = false;
//            }
//        }
//        score.setText("0");
//
//        MainActivity.Card[] cards = set.fetch();
//
//        for (int k = 0; k < cards.length; ++k) {
//            int i = k / 4;
//            int j = k % 4;
//            cardsView.cards[i][j] = cards[k];
//        }
//
//        cardsView.invalidate();
//    }
//
//
//    public void newGameClick(View view) throws ExecutionException, InterruptedException {
//        newGame();
//    }
//
//
//    public void chooseCards(MainActivity.Card[] cards) throws ExecutionException, InterruptedException {
//        MainActivity.Card[] newCards = set.chooseCards(cards);
//        for (int k = 0; k < cards.length; ++k) {
//            int i = k / 4;
//            int j = k % 4;
//            cardsView.cards[i][j] = cards[k];
//        }
//
//        cardsView.invalidate();
//    }
//
//
//    public void checkSet(View view) throws ExecutionException, InterruptedException {
//        Log.d("azazaz", "inside checkSet");
//        ArrayList<MainActivity.Card> cards = new ArrayList<>();
//        int activeCards = 0;
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 4; ++j) {
//                if (cardsView.active[i][j]) {
//                    ++activeCards;
//                    cards.add(cardsView.cards[i][j]);
//                }
//            }
//        }
//
//
//        if (cards.size() < 3) {
//            return;
//        }
//
//        while (cards.size() > 3) {
//            cards.remove(cards.size() - 1);
//        }
//
//        HashSet<Integer> count = new HashSet<>(), color = new HashSet<>(), shape = new HashSet<>(), fill = new HashSet<>();
//        for (int i = 0; i < 3; ++i) {
//            count.add(cards.get(i).count);
//            color.add(cards.get(i).color);
//            shape.add(cards.get(i).shape);
//            fill.add(cards.get(i).fill);
//        }
//
//        Log.d("azazaz", "" + activeCards);
//        if (count.size() == 2 || color.size() == 2 || shape.size() == 2 || fill.size() == 2) {
//            for (int i = 0; i < 3; ++i) {
//                for (int j = 0; j < 4; ++j) {
//                    cardsView.active[i][j] = false;
//                }
//            }
//
//
//            cardsView.invalidate();
//            return;
//        }
//
//        chooseCards(cards.toArray(new MainActivity.Card[3]));
//        Integer scores = Integer.parseInt(score.getText().toString());
//        scores += 3;
//        score.setText(scores.toString());
//    }
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.start_game);
//
//        score = findViewById(R.id.score);
//        cardsView = findViewById(R.id.cards);
//        try {
//            newGame();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Intent intent = getIntent();
//
//        set = new ServerInteractor("http://194.176.114.21:8050/");
//        set.token = intent.getIntExtra("token", 0);
//    }
//
//}
