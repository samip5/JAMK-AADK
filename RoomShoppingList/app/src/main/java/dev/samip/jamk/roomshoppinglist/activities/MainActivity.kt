package dev.samip.jamk.roomshoppinglist.activities

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import dev.samip.jamk.roomshoppinglist.R
import dev.samip.jamk.roomshoppinglist.misc.ShoppingListItem
import dev.samip.jamk.roomshoppinglist.misc.ShoppingListRoomDatabase
import dev.samip.jamk.roomshoppinglist.adapters.ShoppingListAdapter
import dev.samip.jamk.roomshoppinglist.fragments.AskShoppingListItemDialogFragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), AskShoppingListItemDialogFragment.AddDialogListener {

    // Shopping list Items
    private var shoppingList: MutableList<ShoppingListItem> = ArrayList()
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var db: ShoppingListRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ShoppingListAdapter(shoppingList)
        recyclerView.adapter = adapter
        db = Room.databaseBuilder(applicationContext, ShoppingListRoomDatabase::class.java,
            "hs_db").build()
        loadShoppingListItems()

        fab.setOnClickListener { view ->
            // create and show dialog
            val dialog = AskShoppingListItemDialogFragment()
            dialog.show(supportFragmentManager, "AskNewItemDialogFragment")
        }
        initSwipe()
    }

    // Add a new shopping list item to db
    override fun onDialogPositiveClick(item: ShoppingListItem) {
        // Create a Handler Object
        val handler = Handler(Handler.Callback {
            // Toast message
            Toast.makeText(applicationContext,it.data.getString("message"), Toast.LENGTH_SHORT).show()
            // Notify adapter data change
            adapter.update(shoppingList)
            true
        })
        // Create a new Thread to insert data to database
        Thread(Runnable {
            // insert and get autoincrement id of the item
            val id = db.shoppingListDao().insert(item)
            // add to view
            item.id = id.toInt()
            shoppingList.add(item)
            val message = Message.obtain()
            message.data.putString("message","Item added to db!")
            handler.sendMessage(message)
        }).start()
    }

    private fun loadShoppingListItems() {
        // Create a Handler Object
        val handler = Handler(Handler.Callback {
            // Toast message
            Toast.makeText(applicationContext,it.data.getString("message"), Toast.LENGTH_SHORT).show()
            // Notify adapter data change
            adapter.update(shoppingList)
            true
        })

        Thread(Runnable {
            shoppingList = db.shoppingListDao().getAll()
            val message = Message.obtain()
            if(shoppingList.size > 0) {
                message.data.putString("message","All shopping list items read from db!")
            }else {
                message.data.putString("message","Shopping list is empty!")
                handler.sendMessage(message)
            }
        }).start()
    }

    private fun initSwipe() {
        // Touch Callback
        val simpleItemTouchCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val handler = Handler(Handler.Callback {
                    // Toast message
                    Toast.makeText(applicationContext,it.data.getString("message"), Toast.LENGTH_SHORT).show()
                    // Notify adapter data change
                    adapter.update(shoppingList)
                    true
                })
                val id = shoppingList[position].id
                shoppingList.removeAt(position)
                Thread(Runnable {
                    db.shoppingListDao().delete(id)
                    val message = Message.obtain()
                    message.data.putString("message", "Item deleted from database!")
                    handler.sendMessage(message)
                }).start()
            }
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
