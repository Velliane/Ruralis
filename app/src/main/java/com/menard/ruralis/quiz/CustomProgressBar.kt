package com.menard.ruralis.quiz

import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import com.menard.ruralis.R
import kotlinx.android.synthetic.main.custom_progress_bar.view.*

class CustomProgressBar {

    lateinit var dialog: Dialog

    fun show(context: Context): Dialog{
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_progress_bar, null)
        if (title != null){
            view.custom_progress_text.text = title
        }
        view.custom_progress_container.setBackgroundResource(R.color.colorPrimaryDark)
        view.custom_progress_cardview.setBackgroundResource(R.color.places_text_black_alpha_26)
        setColorFilter(view.custom_progress.indeterminateDrawable, ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null))
        view.custom_progress_text.setTextColor(context.resources.getColor(R.color.colorPrimary))

        view.custom_progress_icon.apply {
            setBackgroundResource(R.drawable.goat_animation)
            val goatAnimation: AnimationDrawable = background as AnimationDrawable
            goatAnimation.start()
        }
        dialog = Dialog(context, R.style.CustomProgressBarTheme)
        dialog.setContentView(view)
        dialog.show()

        return dialog
    }

    private fun setColorFilter(@NonNull drawable: Drawable, color: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        }else{
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

}