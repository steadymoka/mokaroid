package moka.land.component.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import moka.land.base.logA

open class LoggingFragment : Fragment() {

    private var TAG = javaClass.simpleName

    override fun onAttach(context: Context) {
        logA("lifeCycle", "$TAG onAttach called ####")
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logA("lifeCycle", "$TAG onCreateView called ####")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logA("lifeCycle", "$TAG onViewCreated called ####")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        logA("lifeCycle", "$TAG onResume called ####")
        super.onResume()
    }

    // -

    override fun onPause() {
        super.onPause()
        logA("lifeCycle", "$TAG onPause called ####")
    }

    override fun onStop() {
        super.onStop()
        logA("lifeCycle", "$TAG onStop called ####")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logA("lifeCycle", "$TAG onDestroyView called ####")
    }

}
