package com.alphadevelopmentsolutions.frcscout.animations

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.alphadevelopmentsolutions.frcscout.callbacks.OnAnimationCompleteCallback

class LoadingAnimation(context: Context, attributeSet: AttributeSet) : LottieAnimationView(context, attributeSet) {
    companion object {
        private const val LOADING_MIN_FRAME = 0
        private const val LOADING_MAX_FRAME = 118

        private const val ZERO_FRAME_MIN = 116
        private const val ZERO_FRAME = 118

        private const val SUCCESS_MIN_FRAME = 239
        private const val SUCCESS_MAX_FRAME = 395

        private const val FAILURE_MIN_FRAME = 657
        private const val FAILURE_MAX_FRAME = 820

        private const val SPEED = 1.5f

        private const val FILE_NAME = "loading.json"
    }

    init
    {
        setAnimation(FILE_NAME)
        repeatCount = LottieDrawable.INFINITE
        setMinAndMaxFrame(LOADING_MIN_FRAME, LOADING_MAX_FRAME)
        speed = SPEED
        playAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        repeatCount = LottieDrawable.INFINITE
    }

    /**
     * Success callback
     * @param callback [OnAnimationCompleteCallback] calls [OnAnimationCompleteCallback.onSuccess] if [callback] is not null
     */
    fun success(callback: OnAnimationCompleteCallback?) {
        addAnimatorUpdateListener {

            when (frame) {
                in ZERO_FRAME_MIN..ZERO_FRAME -> setMinAndMaxFrame(SUCCESS_MIN_FRAME, SUCCESS_MAX_FRAME)
                SUCCESS_MAX_FRAME -> {
                    repeatCount = 0
                    callback?.onSuccess()
                    removeAllUpdateListeners()
                    removeAllAnimatorListeners()
                }
            }
        }
    }

    /**
     * Fail callback
     * @param callback [OnAnimationCompleteCallback] calls [OnAnimationCompleteCallback.onFail] if [callback] is not null
     */
    fun failure(callback: OnAnimationCompleteCallback?) {
        addAnimatorUpdateListener {

            when (frame) {
                in ZERO_FRAME_MIN..ZERO_FRAME -> setMinAndMaxFrame(FAILURE_MIN_FRAME, FAILURE_MAX_FRAME)
                FAILURE_MAX_FRAME -> {
                    repeatCount = 0
                    callback?.onFail()
                    removeAllUpdateListeners()
                    removeAllAnimatorListeners()
                }
            }
        }
    }
}