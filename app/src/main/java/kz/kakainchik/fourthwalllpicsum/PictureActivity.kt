package kz.kakainchik.fourthwalllpicsum

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import kz.kakainchik.fourthwalllpicsum.entities.Picture
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * The screen with larger view of an image and its details.
 */
class PictureActivity : AppCompatActivity() {
    private lateinit var imageContainer: ImageView
    private lateinit var shareButton: MaterialButton
    private lateinit var authorLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        Log.d(TAG, """'OnCreate' invoked""")

        //Visual components
        imageContainer = findViewById(R.id.picture_image_container)
        shareButton = findViewById(R.id.picture_share_button)
        authorLabel = findViewById(R.id.picture_author_label)

        //Hide label and shareButton if landscape orientation
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            changeDetailsVisibility(View.GONE)

        //Get picture data from previous activity and attach to UI
        val bundle: Picture? = intent.getParcelableExtra(EXTRA_NAME)
        bundle?.let { pic ->
            authorLabel.text = getString(R.string.picture_author_label, pic.author)
            Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.ic_sharp_image_24)
                .load(pic.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { bitmap ->
                            //Set event handler on clicking shareButton
                            shareButton.setOnClickListener {
                                //Create URI from bitmap from Glide container
                                //and put it in the intent
                                val uri = getBitmapUri(bitmap)
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "image/jpeg"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                startActivity(Intent.createChooser(shareIntent, "Share image"))
                            }
                        }
                        return false
                    }
                })
                .into(imageContainer)
            //NOTE: Glide automatically clears after OnDestroy method.
        }
    }

    private fun getBitmapUri(bitmap: Bitmap): Uri {
        val file = File(externalCacheDir, "pictures/${UUID.randomUUID()}.jpeg")
        file.parentFile?.mkdirs()

        //Usually here is an exception handler but not now
        try {
            val stream = FileOutputStream(file)
            stream.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            }
        } catch(ex: IOException) {
            ex.printStackTrace()
        }

        return FileProvider.getUriForFile(
            this,
            getString(R.string.authority_app),
            file
        )
    }

    @SuppressLint("SwitchIntDef")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //Hide label and shareButton when landscape orientation
        when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> changeDetailsVisibility(View.VISIBLE)
            Configuration.ORIENTATION_LANDSCAPE -> changeDetailsVisibility(View.GONE)
        }
    }

    /**
     * Sets visibility property for details visual elements.
     */
    private fun changeDetailsVisibility(mode: Int) {
        shareButton.visibility = mode
        authorLabel.visibility = mode
    }

    companion object {
        const val TAG = "PICTURE_ACTIVITY"
        const val EXTRA_NAME = "PICTURE"
    }
}