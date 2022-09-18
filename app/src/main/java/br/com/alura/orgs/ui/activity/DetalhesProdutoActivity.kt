package br.com.alura.orgs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityDetalhesProdutoBinding
import br.com.alura.orgs.extensions.formataParaMoedaBrasileira
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto

class DetalhesProdutoActivity : AppCompatActivity() {

    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        AppDatabase.getInstance(this).produtoDao()
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
            tentaCarregarProduto()
        }

        override fun onResume() {
            super.onResume()
            buscaProduto()
        }

    private fun buscaProduto() {
        produto = produtoDao.buscaPorId(produtoId)
        produto?.let { produtoCarregado ->
            preencheCampos(produtoCarregado)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_detalhes_produto, menu)

            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            if(::produto.isInitialized)
            when(item.itemId){
                R.id.menu_detalhes_produto_remover -> {
                    produto?.let { produtoDao.delete(it) }
                    finish()
                }
                R.id.menu_detalhes_produto_editar ->{
                    Intent(this, FormularioProdutoActivity::class.java).apply{
                        putExtra(ID_PRODUTO, produtoId)
                        startActivity(this)
                    }
                }
            }

            return super.onOptionsItemSelected(item)
        }

        private fun tentaCarregarProduto() {
//            intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)?.let { produtoCarregado ->
////                produto = produtoCarregado
//                produtoId = produtoCarregado.id
//            } ?: finish()

            produtoId = intent.getLongExtra(ID_PRODUTO, 0L)

        }

        private fun preencheCampos(produtoCarregado: Produto) {
            with(binding) {
                activityDetalhesProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
                activityDetalhesProdutoNome.text = produtoCarregado.nome
                activityDetalhesProdutoDescricao.text = produtoCarregado.descricao
                activityDetalhesProdutoValor.text =
                    produtoCarregado.valor.formataParaMoedaBrasileira()
            }
        }

}