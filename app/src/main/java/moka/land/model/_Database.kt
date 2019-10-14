package moka.land.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import moka.land.model._Database.Companion.DB_VERSION
import moka.land.model.dao.UserDao
import moka.land.model.entity.User

@Database(
    entities = [
        User::class
    ],
    version = DB_VERSION
)
abstract class _Database : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    /**
     */

    companion object {

        const val DB_VERSION = 1
        const val DB_NAME = "dbname.db"

        @Volatile
        private var INSTANCE: _Database? = null

        fun getInstance(context: Context): _Database =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(context.applicationContext, _Database::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_TO_2)
                .build()

        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }

}

