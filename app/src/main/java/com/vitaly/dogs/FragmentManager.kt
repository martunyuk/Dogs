package com.vitaly.dogs

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object FragmentManager {
    @JvmOverloads
    fun replaceFragment(
        replacement_id: Int,
        fragment: Fragment?,
        activity: Activity,
        addToBackStack: Boolean = false
    ) {
        val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        if (addToBackStack) fragmentTransition.addToBackStack(null)
        fragmentTransition.replace(replacement_id, fragment!!)
        fragmentTransition.commit()
    }
}