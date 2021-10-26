package br.com.douglas.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.douglas.ceep.R;
import br.com.douglas.ceep.dao.NotaDAO;
import br.com.douglas.ceep.model.Nota;
import br.com.douglas.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = new NotaDAO().todos();
        configuraRecyclerView(todasNotas);

        configuraBtnInsereNota();
    }

    private void configuraBtnInsereNota() {
        TextView btnIsereNota = findViewById(R.id.lista_notas_insere_nota);
        btnIsereNota.setOnClickListener(v -> {
            vaiParaFormularioNotaActivity();
        });
    }

    private void vaiParaFormularioNotaActivity() {
        Intent iniciaFormularioNota = new Intent(this, FormularioNotaActivity.class);
        formularioActivityResultLauncher.launch(iniciaFormularioNota);
    }

    ActivityResultLauncher<Intent> formularioActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    insereNotaENotificaAdapter(data);
                }
            });

    private void insereNotaENotificaAdapter(Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
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
    }
}