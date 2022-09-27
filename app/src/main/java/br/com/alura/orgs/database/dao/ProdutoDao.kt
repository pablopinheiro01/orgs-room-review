package br.com.alura.orgs.database.dao

import androidx.room.*
import br.com.alura.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY valor ASC ")
    suspend fun buscaValorAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC ")
    suspend fun buscaValorDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome ASC ")
    suspend fun buscaNomeAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome DESC ")
    suspend fun buscaNomeDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC ")
    suspend fun buscaDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC ")
    suspend fun buscaDescricaoDesc(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Delete
    suspend fun delete(vararg  produto: Produto)

    //substituido pelo onConflictStrategy do room
//    @Update
//    fun atualiza(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id ")
    suspend fun buscaPorId(id: Long) : Produto?

}