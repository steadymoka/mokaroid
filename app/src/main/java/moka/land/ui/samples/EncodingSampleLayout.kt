package moka.land.ui.samples

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apollographql.apollo.ApolloClient
import moka.land.base.log
import moka.land.databinding.LayoutEncodingSampleBinding
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.viewer.ImageViewer
import moka.land.transcoder.MediaTranscoder
import moka.land.transcoder.format.MediaFormatStrategyPresets
import moka.land.transcoder.utils.Thumbnail
import moka.land.util.load
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.Future


class EncodingSampleLayout : Fragment() {

    private val _view by lazy { LayoutEncodingSampleBinding.inflate(layoutInflater) }
    private val apolloClient by inject<ApolloClient>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initView()
        bindEvent()
        return _view.root
    }

    private fun initView() {
    }

    /*
    트랜스코더 https://pyxispub.uzuki.live/?p=174
     */
    private fun bindEvent() {
        _view.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }

        _view.textViewTestButton.setOnClickListener {
            ImagePicker
                .with(this)
                .setConfig {
                    mediaType = MediaType.IMAGE_ONLY
                    implicit = true
                }
                .showSingle { uri ->
                    try {
                        val bitmap = Thumbnail.imageUriToBitmap(uri)
                        val base64 = Thumbnail.bitmapToBase64(bitmap)
                        log("base64: ${base64}")
                        _view.imageViewTarget.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    val bit = Thumbnail.videoUriToBitmap(uri)
//                    _view.imageViewTarget.setImageBitmap(bit)
//                    transcode(uri)
                }
        }

        _view.imageViewTarget.isEnabled = false
        _view.imageViewTarget.setOnClickListener {
            ImageViewer
                .with(activity!!)
                .show(arrayListOf(Uri.fromFile(file!!)))
        }
    }

    private var transcoderFuture: Future<Void>? = null
    private var file: File? = null

    private fun transcode(uri: Uri) {
        val parcelFileDescriptor = try {
            requireActivity().contentResolver.openFileDescriptor(uri, "r")!!
        } catch (e: FileNotFoundException) {
            Toast.makeText(this@EncodingSampleLayout.requireContext(), "File not found.", Toast.LENGTH_LONG).show()
            return
        }
        val fileDescriptor = parcelFileDescriptor.fileDescriptor

        val outputDir = File(requireActivity().getExternalFilesDir(null), "outputs")
        outputDir.mkdir()
        file = File.createTempFile("transcode_test", ".mp4", outputDir)

        transcoderFuture = MediaTranscoder
            .getInstance()
            .transcodeVideo(
                fileDescriptor,
                file!!.absolutePath,
                MediaFormatStrategyPresets.createAndroid720pStrategy(),
                object : MediaTranscoder.Listener {
                    override fun onTranscodeProgress(progress: Double) {
                        log("progress: ${progress}")
                    }

                    override fun onTranscodeCompleted() {
                        log("onTranscodeCompleted is called")
                        _view.imageViewTarget.load(requireActivity(), file)
                        _view.imageViewTarget.isEnabled = true
                    }

                    override fun onTranscodeCanceled() {
                    }

                    override fun onTranscodeFailed(exception: Exception?) {
                        log("onTranscodeFailed is called / ${exception?.message}")
                    }
                }
            )
    }

}
