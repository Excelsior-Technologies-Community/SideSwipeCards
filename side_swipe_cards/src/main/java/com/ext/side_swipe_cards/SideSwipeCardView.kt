package com.ext.side_swipe_cards

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kotlin.math.abs

class SideSwipeCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /* ---------------- State ---------------- */

    private var downX = 0f
    private var downY = 0f
    private var isDragging = false
    private var isAnimating = false

    /* ---------------- Config ---------------- */

    private var swipeThreshold = 300f
    private var rotationFactor = 20f
    private var verticalDragFactor = 0.2f
    private var scaleDownFactor = 0.05f
    private var animationDuration = 300L
    private var resetDuration = 200L
    private var cardCornerRadius = 24f
    private var cardElevation = 12f

    /* ---------------- Views ---------------- */

    private lateinit var cardView: CardView
    private lateinit var imageView: ImageView

    /* ---------------- Listeners ---------------- */

    private var swipeListener: OnSwipeListener? = null
    private var swipeStartListener: OnSwipeStartListener? = null

    init {
        attrs?.let { readAttributes(context, it) }
        setupView(context)
    }

    /* ---------------- Setup ---------------- */

    private fun setupView(context: Context) {
        cardView = CardView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            radius = cardCornerRadius
            cardElevation = this@SideSwipeCardView.cardElevation
            setCardBackgroundColor(
                ContextCompat.getColor(context, android.R.color.white)
            )
        }

        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        cardView.addView(imageView)
        addView(cardView)

        isClickable = true
        isFocusable = true
    }

    private fun readAttributes(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SideSwipeCardView)
        ta.apply {
            swipeThreshold = getDimension(R.styleable.SideSwipeCardView_swipeThreshold, 300f) // 300dp equivalent in px
            rotationFactor = getFloat(R.styleable.SideSwipeCardView_rotationFactor, 20f)
            verticalDragFactor = getFloat(R.styleable.SideSwipeCardView_verticalDragFactor, 0.2f)
            scaleDownFactor = getFloat(R.styleable.SideSwipeCardView_scaleDownFactor, 0.05f)
            animationDuration = getInt(R.styleable.SideSwipeCardView_animationDuration, 300).toLong()
            resetDuration = getInt(R.styleable.SideSwipeCardView_resetDuration, 200).toLong()
            cardCornerRadius = getDimension(R.styleable.SideSwipeCardView_cardCornerRadius, 24f) // 24dp in px
            cardElevation = getDimension(R.styleable.SideSwipeCardView_cardElevation, 12f) // 12dp in px
        }
        ta.recycle()
    }
    /* ---------------- Public API ---------------- */

    fun setCardImage(@DrawableRes resId: Int) {
        imageView.setImageResource(resId)
    }

    fun setOnSwipeListener(listener: OnSwipeListener) {
        swipeListener = listener
    }

    fun setOnSwipeStartListener(listener: OnSwipeStartListener) {
        swipeStartListener = listener
    }

    fun swipe(direction: SwipeDirection) {
        if (!isAnimating) {
            animateSwipeOut(direction)
        }
    }

    fun reset() {
        if (!isAnimating) {
            resetPosition()
        }
    }

    /* ---------------- Touch ---------------- */

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isAnimating) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                downY = event.rawY
                isDragging = false
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - downX
                val dy = event.rawY - downY

                if (!isDragging && abs(dx) > 10) {
                    isDragging = true
                    swipeStartListener?.onSwipeStart()
                }

                val progress = (abs(dx) / swipeThreshold).coerceIn(0f, 1f)
                val direction = if (dx > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT

                cardView.translationX = dx
                cardView.translationY = dy * verticalDragFactor
                cardView.rotation = (dx / rotationFactor).coerceIn(-45f, 45f)
                cardView.scaleX = 1f - (progress * scaleDownFactor)
                cardView.scaleY = 1f - (progress * scaleDownFactor)

                swipeListener?.onSwipeProgress(direction, progress)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (abs(cardView.translationX) > swipeThreshold) {
                    animateSwipeOut(
                        if (cardView.translationX > 0)
                            SwipeDirection.RIGHT else SwipeDirection.LEFT
                    )
                } else {
                    resetPosition()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /* ---------------- Animations ---------------- */

    private fun animateSwipeOut(direction: SwipeDirection) {
        isAnimating = true

        val targetX =
            if (direction == SwipeDirection.RIGHT) width * 2f else -width * 2f

        cardView.animate()
            .translationX(targetX)
            .rotation(if (direction == SwipeDirection.RIGHT) 45f else -45f)
            .setDuration(animationDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    swipeListener?.onSwiped(direction)
                    resetImmediately()
                    isAnimating = false
                }
            })
            .start()
    }

    private fun resetPosition() {
        isAnimating = true
        cardView.animate()
            .translationX(0f)
            .translationY(0f)
            .rotation(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(resetDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    swipeListener?.onSwipeCancelled()
                    isAnimating = false
                }
            })
            .start()
    }

    private fun resetImmediately() {
        cardView.translationX = 0f
        cardView.translationY = 0f
        cardView.rotation = 0f
        cardView.scaleX = 1f
        cardView.scaleY = 1f
    }
}
