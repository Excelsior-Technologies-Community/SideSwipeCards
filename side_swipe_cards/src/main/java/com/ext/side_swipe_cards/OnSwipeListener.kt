package com.ext.side_swipe_cards


interface OnSwipeListener {
    /**
     * Called when a card has been successfully swiped off screen
     * @param direction The direction the card was swiped
     */
    fun onSwiped(direction: SwipeDirection)

    /**
     * Called during a swipe gesture to report progress
     * @param direction Current swipe direction
     * @param progress Progress from 0.0 to 1.0
     */
    fun onSwipeProgress(direction: SwipeDirection, progress: Float) {}

    /**
     * Called when a swipe gesture is cancelled (didn't meet threshold)
     */
    fun onSwipeCancelled() {}
}