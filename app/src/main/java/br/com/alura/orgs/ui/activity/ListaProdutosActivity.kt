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

    }

    override fun onResume(){
        super.onResume()
//        lifecycleScope.launch( CoroutineName("Primaria")) {
//            Log.i(TAG, "onResume: Contexto da coroutine ${coroutineContext}")
//            repeat(1000){
//                Log.i("onResume", "onResume: Coroutine está em execução $it")
//                delay(1000L)
//            }
//        }
        //por padrão a lifecycleScope é mainsafe e possui autocancelamento
        //tambem e possivel executar diretamente sem alterar o Dispatchers para IO
        lifecycleScope.launch{
            val produtos = produtoDao.buscaTodos()
            adapter.atualiza(produtos)
        }

    }

    fun testesDeCoroutineUtilizadoApenasComoExemplo() {

        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i(TAG, "Erro do Coroutine $throwable")
            Toast.makeText(this, "ocorreu um erro", Toast.LENGTH_LONG).show()
        }
        val scope = MainScope()

        scope.launch(job + handler + Dispatchers.IO + CoroutineName("Primaria")) {
            Log.i(TAG, "onResume: Contexto da coroutine ${coroutineContext}")
            repeat(1000){
                Log.i("onResume", "onResume: Coroutine está em execução $it")
                delay(1000L)
            }
        }

        scope.launch(job + handler){
//            throw Exception("erro qualquer dentro do launch")
            val produtos = withContext(Dispatchers.IO) {
                produtoDao.buscaTodos()
//                            throw Exception("erro qualquer dentro de IO")
            }
//                            throw Exception("erro qualquer depois do withcontext ")
            adapter.atualiza(produtos)
        }

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
                    adapter.atualiza(produtoDao.buscaTodos())
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
                adapter.atualiza(produtoDao.buscaTodos())
            }
        }

    }

    //transformando o retorno da busca em uma coroutine suspensa
    private suspend fun buscaTodosProdutos(): List<Produto> = withContext(Dispatchers.IO) { produtoDao.buscaTodos() }


}