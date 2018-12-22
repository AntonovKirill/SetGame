package antonovkirill.setgame;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

import antonovkirill.setgame.MainActivity.Card;

public class ServerInteractor {

    public int token;


    public class RegisterQuery {
        String action = "register";
        String nickname;


        RegisterQuery() {}
    }


    public class RegisterAnswer {
        String status;
        int token;


        RegisterAnswer() {}
    }


    public class NewGameQuery {
        String action = "fetch_cards";
        int token;


        NewGameQuery() {}
    }


    public class NewGameAnswer {
        String status;
        Card[] cards;


        NewGameAnswer() {}
    }


    public class ChooseCardsQuery {
        String action = "take_set";
        int token;
        Card[] cards;


        ChooseCardsQuery() {}
    }


    public class ChooseCardsAnswer {
        String status;
        int cards_left;
        int point;


        ChooseCardsAnswer() {}
    }



    public String server_url;


    public ServerInteractor(String url) {
        server_url = url;
    }


    public String sendQuery(String data) throws ExecutionException, InterruptedException {
        SendQueryTask sendQueryTask = new SendQueryTask();
        sendQueryTask.execute(data, server_url);
        return sendQueryTask.get();
    }


    public int register(String nickname) throws ExecutionException, InterruptedException {
        RegisterQuery query = new RegisterQuery();
        query.nickname = nickname;

        Gson gson = new GsonBuilder().create();
        String data = gson.toJson(query);

        String get = sendQuery(data);
        if (get.equals("")) {
            return -2;
        }

        RegisterAnswer answer = gson.fromJson(get, RegisterAnswer.class);
        token = answer.token;

        return answer.token;
    }


    public Card[] fetch() throws ExecutionException, InterruptedException {
        NewGameQuery query = new NewGameQuery();
        query.token = token;

        Gson gson = new GsonBuilder().create();
        String data = gson.toJson(query);

        Log.d("fetch query", "query\n" + data);

        String get = sendQuery(data);
        Log.d("fetch answer", "answer\n" + get);
        NewGameAnswer answer = gson.fromJson(get, NewGameAnswer.class);
        return answer.cards;
    }


    public void chooseCards(Card[] cards) throws ExecutionException, InterruptedException {
        ChooseCardsQuery query = new ChooseCardsQuery();
        query.token = token;
        query.cards = cards;

        Gson gson = new GsonBuilder().create();
        String data = gson.toJson(query);
        Log.d("choose query", "query\n" + data);

        String get = sendQuery(data);
    }

}
