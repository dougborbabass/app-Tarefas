package br.com.douglas.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.douglas.ceep.R;
import br.com.douglas.ceep.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity {

    public static final String CHAVE_NOTA = "nota";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_formulario_nota_ic_salva) {
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsert = new Intent();
        resultadoInsert.putExtra(CHAVE_NOTA, nota);
        setResult(Activity.RESULT_OK, resultadoInsert);
    }

    @NonNull
    private Nota criaNota() {

        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);

        return new Nota(titulo.getText().toString(),
                descricao.getText().toString());
    }
}