package br.com.alura.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.database.dao.ProdutoDao
import br.com.alura.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.dialog.FormularioImagemDialog
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private var url: String? = null

    var produtoId = 0L // se for zero é um novo produto

    private val produtoDao: ProdutoDao by lazy{
        val db = AppDatabase.getInstance(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar produto"
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this)
                .mostra(url) { imagem ->
                    url = imagem
                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
                }
        }
//        intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)?.let { produtoCarregado ->
//            produtoId = produtoCarregado.id
//            preencheCampos(produtoCarregado)
//        }
        tentaCarregarProduto()
    }

    override fun onResume() {
        super.onResume()
        tentaBuscarProduto()
    }

    private fun tentaBuscarProduto() {
        produtoDao.buscaPorId(produtoId)?.let {
            preencheCampos(it)
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(ID_PRODUTO, 0L)
    }

    private fun preencheCampos(produtoCarregado: Produto) {
        title = "Alterar produto"
        url = produtoCarregado.imagem
        binding.activityFormularioProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
        binding.activityFormularioProdutoNome.setText(produtoCarregado.nome)
        binding.activityFormularioProdutoDescricao.setText(produtoCarregado.descricao)
        binding.activityFormularioProdutoValor.setText(produtoCarregado.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
//        val dao = ProdutosDao()
        //configurando o builder!


        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
//            if(produtoId > 0 ){
//                produtoDao.atualiza(produtoNovo.copy(id=produtoId))
//            }else{
//                produtoDao.salva(produtoNovo)
//            }
            produtoDao.salva(produtoNovo)
//            dao.adiciona(produtoNovo)
            finish()
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

}