package com.example.yanagladdeveloperslife.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

abstract class ButtonSupportedFragment : Fragment(),
    Clickable {
    protected lateinit var btnNex: ExtendedFloatingActionButton
    protected lateinit var btnPrev: ExtendedFloatingActionButton

    var onNextClickListener: View.OnClickListener? = null
    get() = field
    var onPrevClickListener: View.OnClickListener? = null
    get() = field
}

