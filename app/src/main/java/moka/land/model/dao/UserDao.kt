package moka.land.model.dao

import androidx.room.*
import moka.land.model.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getById(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User): Int

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun delete(id: String)

    @Delete
    suspend fun delete(user: User)

}
