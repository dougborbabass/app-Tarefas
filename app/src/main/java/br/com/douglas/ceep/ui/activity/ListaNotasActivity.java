package br.com.douglas.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.douglas.ceep.R;
import br.com.douglas.ceep.dao.NotaDAO;
import br.com.douglas.ceep.model.Nota;
import br.com.douglas.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.douglas.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallBack;

import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.CHAVE_NOTA;
import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.CHAVE_POSICAO;
import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.POSICAO_INVALIDA;
import static br.com.douglas.ceep.ui.activity.NotasActivityConstantes.TITULO_APPBAR;

public class ListaNotasActivity extends AppCompatActivity {


    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBtnInsereNota();
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
//        for (int i = 0; i < 10; i++) {
//            dao.insere(new Nota("Titulo " + (i + 1), "Descricao " + (i + 1)));
//        }
        return dao.todos();
    }

    private void configuraBtnInsereNota() {
        TextView btnIsereNota = findViewById(R.id.lista_notas_insere_nota);
        btnIsereNota.setOnClickListener(v -> {
            vaiParaFormularioNotaActivityInsere();
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota = new Intent(this, FormularioNotaActivity.class);
        formularioActivityResultLauncher.launch(iniciaFormularioNota);
    }

    ActivityResultLauncher<Intent> formularioActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = verificaSeOActivityResultTemData(result);
                    Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                    if (data.hasExtra(CHAVE_POSICAO)) {
                        int posicao = data.getIntExtra(CHAVE_POSICAO, -1);
                        if (validaSeEhUmaPosicaoValida(posicao)) {
                            alteraNotaENotificaAdapter(posicao, notaRecebida);
                        }
                    } else {
                        insereNotaENotificaAdapter(notaRecebida);
                    }
                }
            });

    private Intent verificaSeOActivityResultTemData(ActivityResult result) {
        Intent data = result.getData();
        assert data != null;
        return data;
    }

    private void alteraNotaENotificaAdapter(int posicao, Nota notaRecebida) {
        new NotaDAO().altera(posicao, notaRecebida);
        adapter.altera(posicao, notaRecebida);
    }

    private boolean validaSeEhUmaPosicaoValida(int posicao) {
        return posicao > POSICAO_INVALIDA;
    }

    private void insereNotaENotificaAdapter(Nota notaRecebida) {
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotasRecyclerView = findViewById(R.id.lista_notas_recyclerview);

        configuraAdapter(todasNotas, listaNotasRecyclerView);
        configuraItemTouchHelper(listaNotasRecyclerView);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotasRecyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallBack(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotasRecyclerView);
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
}