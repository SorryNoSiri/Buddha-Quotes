package org.bandev.buddhaquotes

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_info_panel.*
import kotlinx.android.synthetic.main.content_scrolling.*

class ScrollingActivity : AppCompatActivity(), ScrollingAdapter.OnItemClickFinder {

    val scrollingList = generateDummyList(50)
    val adapter = ScrollingAdapter(scrollingList, this)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        var back = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24)
        var toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        var list = intent.getStringExtra("list")!!.toString()


        setSupportActionBar(findViewById(R.id.toolbar))
        window.statusBarColor = ContextCompat.getColor(this@ScrollingActivity, R.color.colorTop)
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = list

        (supportActionBar ?: return).setDisplayShowTitleEnabled(false)
        (supportActionBar ?: return).setDisplayHomeAsUpEnabled(true)

        if (intent.getBooleanExtra("safe", false)) {
            insertItem(intent.getStringExtra("quote")!!.toInt())
        }

        toolbar.navigationIcon = back;

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(false)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }


        val view = View(this)
        view.doOnLayout {
            view.windowInsetsController?.show(WindowInsets.Type.ime())
            // You can also access it from Window
            window.insetsController?.show(WindowInsets.Type.ime())
        }

        view.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    fun insertItem(num: Int) {
        val index = 0
        val quote = Quotes()
        val text = quote.random(num)
        val newItem = ExampleItem(
            text,
            R.drawable.like,
        )

        scrollingList.add(index, newItem)
        adapter.notifyItemInserted(index)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scroll_mode_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                val intent2 = Intent(this, AddList::class.java)
                this.startActivity(intent2)
                true
            }
            android.R.id.home -> {
                finish()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onLikeClick(position: Int) {
        val clickedItem = scrollingList[position]

        if(clickedItem.resource == R.drawable.heart_full_red){
            clickedItem.resource = R.drawable.like
        }else{
            clickedItem.resource = R.drawable.heart_full_red
        }

        vibratePhone()
        adapter.notifyItemChanged(position)
    }

    override fun onShareClick(position: Int) {
        val clickedItem = scrollingList[position]
        vibratePhone()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND

            val text = clickedItem.quote + "\n\n~Buddha"

            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onBinClick(position: Int) {
        vibratePhone()

        scrollingList.removeAt(position)
        adapter.notifyItemRemoved(position)

    }

    private fun generateDummyList(size: Int): ArrayList<ExampleItem> {

        val list = ArrayList<ExampleItem>()
        val quote = Quotes()

        var list_tmp = intent.getStringExtra("list")!!.toString()

        val pref = getSharedPreferences("List_system", 0)
        val pref_string = pref.getString(list_tmp, "Nothing Here")
        val pref_list = pref_string!!.split("//")

        for (text in pref_list) {
            val item = ExampleItem(text, R.drawable.like)
            list += item
        }
        return list
    }

    private fun vibratePhone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            vibrator.vibrate(200)
        }
    }


    override fun onSupportNavigateUp(): Boolean {

        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // add your animation

        finish()
    }
}
