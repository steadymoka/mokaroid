package moka.land.model.dao

import androidx.room.*
import moka.land.model.entity.User

/**
 * Data Access Object for the user table.
 */
@Dao
interface UserDao {

    /**
     * Select a user by id.
     *
     * @param userId the user id.
     * @return the user with userId.
     */
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getById(id: String): User?

    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * Update a user.
     *
     * @param user user to be updated
     * @return the number of users updated. This should always be 1.
     */
    @Update
    suspend fun update(user: User): Int

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun delete(id: String)

    @Delete
    suspend fun delete(user: User)

}
