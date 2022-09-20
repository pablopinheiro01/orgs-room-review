package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListaProdutosActivityBinding
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "ListaProdutosActivity"

class ListaProdutosActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosActivityBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.getInstance(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            Log.i(TAG,"onCreate: runblocking init")
            repeat(100){
                launch {
                    Log.i(TAG, "onCreate: launch start $it")
//                    Thread.sleep(10000L) //bloqueia a thread
                    //vai executar somente no final de tudo
                    delay(10000L) //não bloqueia a main thread e permite a execução do restante do codigo
                    Log.i(TAG, "onCreate: launch finish $it")
                }
            }
            Log.i(TAG, "onCreate: runBlocking Finish")
        }
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

    }

    override fun onResume() {
        super.onResume()
        adapter.atualiza(produtoDao.buscaTodos())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.nome_asc -> {
                adapter.atualiza(produtoDao.buscaNomeAsc())
            }
            R.id.nome_desc -> {

                adapter.atualiza(produtoDao.buscaNomeDesc())
            }
            R.id.descricao_asc -> {
                adapter.atualiza(produtoDao.buscaDescricaoAsc())
            }
            R.id.descricao_desc -> {
                adapter.atualiza(produtoDao.buscaDescricaoDesc())
            }
            R.id.valor_asc -> {
                adapter.atualiza(produtoDao.buscaValorAsc())
            }
            R.id.valor_desc -> {
                adapter.atualiza(produtoDao.buscaValorDesc())
            }
            R.id.sem_ordenacao -> {
                adapter.atualiza(produtoDao.buscaTodos())
            }
        }

        return super.onOptionsItemSelected(item)
    }



    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

     private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this,
                DetalhesProdutoActivity::class.java
            ).apply {
                putExtra(ID_PRODUTO, it.id)
            }
            startActivity(intent)
        }
        adapter.quandoClicaEmEditar = { produto ->
            Intent(this, FormularioProdutoActivity::class.java).apply{
                putExtra(ID_PRODUTO, produto.id)
                startActivity(this)
            }
        }
        adapter.quandoClicaemRemover = { produto ->
             produtoDao.delete(produto)
            adapter.atualiza(produtoDao.buscaTodos())
        }

    }

}