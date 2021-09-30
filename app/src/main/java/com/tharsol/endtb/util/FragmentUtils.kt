package com.tharsol.endtb.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

object FragmentUtils {

    fun initFragment(factory: FragmentFactory, tag: String, bundle: Bundle? = null): Fragment {
        val fragment = factory.instantiate(ClassLoader.getSystemClassLoader(), tag)
        bundle?.let { fragment.arguments = it }
        return fragment
    }

}