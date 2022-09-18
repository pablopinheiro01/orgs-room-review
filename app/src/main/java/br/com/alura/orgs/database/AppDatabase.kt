package br.com.alura.orgs.database

import android.content.Context
import androidx.room.*
import br.com.alura.orgs.database.converter.Converters
import br.com.alura.orgs.database.dao.ProdutoDao
import br.com.alura.orgs.model.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun produtoDao(): ProdutoDao

    companion object{

        @Volatile //avisa todas as threads quando um valor é inserido no property
        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if(::db.isInitialized) return db
            return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "orgs.db"
                ).allowMainThreadQueries()
                    .build().also {
                        db = it
                }
        }
    }


}