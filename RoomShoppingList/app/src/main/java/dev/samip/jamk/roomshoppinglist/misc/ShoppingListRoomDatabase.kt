package dev.samip.jamk.roomshoppinglist.misc

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.samip.jamk.roomshoppinglist.misc.ShoppingListDao
import dev.samip.jamk.roomshoppinglist.misc.ShoppingListItem

@Database(entities = [ShoppingListItem::class], version = 1)
abstract class ShoppingListRoomDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
}