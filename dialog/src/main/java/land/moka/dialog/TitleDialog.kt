package land.moka.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog

class TitleDialog(context: Context) : AlertDialog(context) {

    override fun setCustomTitle(customTitleView: View?) {
        super.setCustomTitle(customTitleView)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }

}
