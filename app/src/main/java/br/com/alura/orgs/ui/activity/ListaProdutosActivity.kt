package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListaProdutosActivityBinding
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

private const val TAG = "ListaProdutosActivity"

class ListaProdutosActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosActivityBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.getInstance(this).produtoDao()
    }

    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

        lifecycleScope.launch{
            produtoDao.buscaTodos().collect{ listaProdutos ->
                adapter.atualiza(listaProdutos)
            }
        }

        //exemplo simples de uso do Flow
//        val numberFlow = flow<Int> {
//            repeat(100) {
//                emit(it)
//                delay(1000)
//            }
//        }
//
//        lifecycleScope.launch{
//            numberFlow.collect{ number ->
//                Log.i(TAG, "numero recebido: $number ")
//            }
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        lifecycleScope.launch{

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
//                    adapter.atualiza(produtoDao.buscaTodos())
                    produtoDao.buscaTodos().collect{
                        adapter.atualiza(it)
                    }
                }
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
            lifecycleScope.launch {
                produtoDao.delete(produto)
                //nao e necessario atualizar a busca devido o uso do Flow estar observando as atualizacoes no banco
//                adapter.atualiza(produtoDao.buscaTodos())
            }
        }
    }

}