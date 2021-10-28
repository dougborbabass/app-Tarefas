package br.com.douglas.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import br.com.douglas.ceep.dao.NotaDAO;
import br.com.douglas.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallBack(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(marcacoesDeArrastar,marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        int posInicial = viewHolder.getBindingAdapterPosition();
        int posFinal = target.getBindingAdapterPosition();

        new NotaDAO().troca(posInicial, posFinal);
        adapter.troca(posInicial, posFinal);

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getBindingAdapterPosition();
        new NotaDAO().remove(posicaoDaNotaDeslizada);
        adapter.remove(posicaoDaNotaDeslizada);
    }
}
