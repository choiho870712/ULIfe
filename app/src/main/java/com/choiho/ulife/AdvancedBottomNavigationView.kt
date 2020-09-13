package com.choiho.ulife

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdvancedBottomNavigationView(context: Context, attrs: AttributeSet) : BottomNavigationView(context, attrs) {

    private companion object {
        const val BADGE_MIN_WIDTH_HEIGHT = 20
        const val BADGE_MARGIN_TOP = 5
        const val BADGE_MARGIN_LEFT = 15
    }

    private val menuView = children.first() as BottomNavigationMenuView

    init {
        initBadges(R.layout.layout_badge)
    }

    fun setBadgeValue(@IdRes resId: Int, count: Int) {
        val menuItem = menuView.findViewById(resId) as BottomNavigationItemView

        val badge =
            menuItem.findViewById<ConstraintLayout>(R.id.bottom_bar_badge).parent as ViewGroup
        val badgeText = menuItem.findViewById(R.id.bottom_bar_badge_text) as TextView

        if (count > 0) {
            badgeText.text = count.toString()
            badge.visibility = View.VISIBLE
        } else {
            badge.visibility = View.GONE
        }
    }

    fun setBadgeText(@IdRes resId: Int, text: String) {
        val menuItem = menuView.findViewById(resId) as BottomNavigationItemView

        val badge =
            menuItem.findViewById<ConstraintLayout>(R.id.bottom_bar_badge).parent as ViewGroup
        val badgeText = menuItem.findViewById(R.id.bottom_bar_badge_text) as TextView

        badgeText.text = text
        badge.visibility = View.VISIBLE
    }

    fun closeBadge(@IdRes resId: Int) {
        val menuItem = menuView.findViewById(resId) as BottomNavigationItemView

        val badge =
            menuItem.findViewById<ConstraintLayout>(R.id.bottom_bar_badge).parent as ViewGroup
        badge.visibility = View.GONE
    }

    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun initBadges(@LayoutRes badgeLayoutId: Int) {
        val width = Resources.getSystem().displayMetrics.widthPixels
        val totalItems = menuView.childCount
        val oneItemAreaWidth = width / totalItems
        val marginTop = BADGE_MARGIN_TOP.toPx()
        val marginLeft = BADGE_MARGIN_LEFT.toPx()

        for (i in 0 until totalItems) {
            val menuItem = menuView.getChildAt(i) as BottomNavigationItemView

            val container = ConstraintLayout(context)
            container.visibility = View.GONE

            val badge = View.inflate(container.context, badgeLayoutId, null)
            container.addView(badge)

            val constraintSet = ConstraintSet()
            constraintSet.constrainWidth(badge.id, ConstraintSet.WRAP_CONTENT)
            constraintSet.constrainMinWidth(badge.id, BADGE_MIN_WIDTH_HEIGHT.toPx())
            constraintSet.constrainHeight(badge.id, BADGE_MIN_WIDTH_HEIGHT.toPx())
            constraintSet.connect(
                badge.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                oneItemAreaWidth/2 + marginLeft - 50
            )
            constraintSet.connect(
                badge.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                marginTop
            )
            constraintSet.applyTo(container)
            menuItem.addView(container)
        }
    }
}