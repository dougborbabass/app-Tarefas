package br.com.douglas.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.douglas.ceep.R;
import br.com.douglas.ceep.dao.NotaDAO;
import br.com.douglas.ceep.model.Nota;
import br.com.douglas.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.CHAVE_NOTA;
import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.CHAVE_POSICAO;
import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBtnInsereNota();
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 0; i < 10; i++) {
            dao.insere(new Nota("Titulo " + (i + 1), "Descricao " + (i + 1)));
        }
        return dao.todos();
    }

    private void configuraBtnInsereNota() {
        TextView btnIsereNota = findViewById(R.id.lista_notas_insere_nota);
        btnIsereNota.setOnClickListener(v -> {
            vaiParaFormularioNotaActivityInsere();
        });
    }

    ActivityResultLauncher<Intent> formularioActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = verificaSeOActivityResultTemData(result);
                    if (data.hasExtra(CHAVE_POSICAO)) {
                        alteraNotaENotificaAdapter(data);
                    }
                    insereNotaENotificaAdapter(data);
                }
            });

    private Intent verificaSeOActivityResultTemData(ActivityResult result) {
        Intent data = result.getData();
        assert data != null;
        return data;
    }

    private void alteraNotaENotificaAdapter(Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        int posicao = data.getIntExtra(CHAVE_POSICAO, -1);
        if (validaSeEhUmaPosicaoValida(posicao)) {
            new NotaDAO().altera(posicao, notaRecebida);
            adapter.altera(posicao, notaRecebida);
        } else {
            Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validaSeEhUmaPosicaoValida(int posicao) {
        return posicao > POSICAO_INVALIDA;
    }

    private void insereNotaENotificaAdapter(Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(this::vaiParaFormularioNotaActivityAltera);
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        formularioActivityResultLauncher.launch(abreFormularioComNota);
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota = new Intent(this, FormularioNotaActivity.class);
        formularioActivityResultLauncher.launch(iniciaFormularioNota);
    }
}