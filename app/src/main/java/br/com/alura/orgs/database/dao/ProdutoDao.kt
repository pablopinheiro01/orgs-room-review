package br.com.alura.orgs.database.dao

import androidx.room.*
import br.com.alura.orgs.model.Produto

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor ASC ")
    fun buscaValorAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC ")
    fun buscaValorDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome ASC ")
    fun buscaNomeAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome DESC ")
    fun buscaNomeDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC ")
    fun buscaDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC ")
    fun buscaDescricaoDesc(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg produto: Produto)

    @Delete
    fun delete(vararg  produto: Produto)

    //substituido pelo onConflictStrategy do room
//    @Update
//    fun atualiza(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id ")
    fun buscaPorId(id: Long) : Produto?

}