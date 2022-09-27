package br.com.alura.orgs.database.dao

import androidx.room.*
import br.com.alura.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY valor ASC ")
    fun buscaValorAsc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY valor DESC ")
    fun buscaValorDesc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY nome ASC ")
    fun buscaNomeAsc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY nome DESC ")
    fun buscaNomeDesc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC ")
    fun buscaDescricaoAsc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC ")
    fun buscaDescricaoDesc(): Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Delete
    suspend fun delete(vararg  produto: Produto)

    //substituido pelo onConflictStrategy do room
//    @Update
//    fun atualiza(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id ")
    fun buscaPorId(id: Long) : Flow<Produto?>

}