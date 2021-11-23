package kz.kakainchik.fourthwalllpicsum

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.kakainchik.fourthwalllpicsum.entities.Picture
import kz.kakainchik.fourthwalllpicsum.models.MainActivityViewModel
import kz.kakainchik.fourthwalllpicsum.models.PicsAdapter

class MainActivity : AppCompatActivity() {
    private val model by viewModels<MainActivityViewModel>()
    private val adapter = PicsAdapter { pic -> adapterOnClick(pic) }

    /**
     * Event handler on [PictureActivity] response.
     */
    private val openPictureCallback: ActivityResultLauncher<Picture> = registerForActivityResult(OpenPictureActivityContract()) {

    }

    private lateinit var picsList: RecyclerView
    private lateinit var progressIndicator: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        //Change splash screen into main one
        setTheme(R.style.Theme_FourthwallLPicsum)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, """'OnCreate' invoked""")

        //Visual components
        picsList = findViewById(R.id.pics_list)
        progressIndicator = findViewById(R.id.progressIndicator)

        //Update adapter every time when new pictures arrive
        lifecycleScope.launch {
            Log.d(TAG, "Coroutine launched")
            model.pics.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }

        //Show the PI when data are loading
        adapter.addLoadStateListener {
            progressIndicator.isVisible = it.refresh is LoadState.Loading
        }

        //Installation the adapter
        picsList.adapter = adapter
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //Change column number when screed is rotated
        val picsGrid = picsList.layoutManager as GridLayoutManager
        picsGrid.spanCount = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }
    }

    /**
     * Invoked when an image in [adapter] has been clicked.
     */
    private fun adapterOnClick(pic: Picture) {
        openPictureCallback.launch(pic)
    }

    /**
     * A contract for transition to the [PictureActivity] screen of image details.
     */
    private class OpenPictureActivityContract : ActivityResultContract<Picture, Boolean>() {
        override fun createIntent(context: Context, input: Picture?): Intent {
            return Intent(context, PictureActivity::class.java).apply {
                putExtra(PictureActivity.EXTRA_NAME, input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }

    companion object {
        const val TAG: String = "MAIN_ACTIVITY"
    }
}