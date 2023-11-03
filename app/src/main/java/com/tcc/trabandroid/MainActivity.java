package com.tcc.trabandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private EditText questao;
    private Button resultado;
    private TextView resposta;
    private YesNoApi yesNoApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questao = findViewById(R.id.questao);
        resultado = findViewById(R.id.resultado);
        resposta = findViewById(R.id.resposta);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://yesno.wtf/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yesNoApi = retrofit.create(YesNoApi.class);

        resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questao.getText().toString();
                if (!question.isEmpty()) {
                    fetchAnswer(question);
                } else {
                    resposta.setText("Pergunta:");
                }
            }
        });
    }

    private void fetchAnswer(String question) {
        Call<AnswerResponse> call = yesNoApi.getAnswer(question);
        call.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                if (response.isSuccessful()) {
                    String answer = response.body().getAnswer();
                    resposta.setText(answer);
                } else {
                    resposta.setText("Erro");
                }
            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                resposta.setText("Erro");
            }
        });
    }

    interface YesNoApi {
        @GET("api")
        Call<AnswerResponse> getAnswer(@Query("question") String question);
    }

    class AnswerResponse {
        @SerializedName("answer")
        private String answer;

        public String getAnswer() {
            return answer;
        }
    }
}
