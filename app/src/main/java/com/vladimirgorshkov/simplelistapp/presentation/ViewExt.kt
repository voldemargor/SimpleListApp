package com.vladimirgorshkov.simplelistapp.presentation

import android.view.View
import android.view.animation.Animation

fun View.startAnimation(animation: Animation, onEnd: () -> Unit) {

    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd.invoke()
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }
    })

    this.startAnimation(animation)

}