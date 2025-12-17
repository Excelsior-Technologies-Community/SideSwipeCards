package com.ext.sideswipecardslibrary

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ext.side_swipe_cards.OnSwipeListener
import com.ext.side_swipe_cards.SideSwipeCardView
import com.ext.side_swipe_cards.SwipeDirection

class MainActivity : AppCompatActivity() {

    private lateinit var swipeCard: SideSwipeCardView
    private var currentCardIndex = 0

    // Sample data for demonstration
    private val cardData = listOf(
        CardItem("Beautiful Sunset", "Maldives Beach", R.drawable.sample_image_1),
        CardItem("Mountain Peak", "Swiss Alps", R.drawable.sample_image_2),
        CardItem("City Lights", "Tokyo Night", R.drawable.sample_image_3),
        CardItem("Forest Trail", "Amazon Rainforest", R.drawable.sample_image_4),
        CardItem("Desert Dunes", "Sahara Desert", R.drawable.sample_image_5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeCard = findViewById(R.id.swipeCard)

        window.statusBarColor = android.graphics.Color.parseColor("#000000") // black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0, // no light icons
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        // Set up swipe listener
        swipeCard.setOnSwipeListener(object : OnSwipeListener {
            override fun onSwiped(direction: SwipeDirection) {
                val action = if (direction == SwipeDirection.RIGHT) "Liked" else "Passed"


                // Load next card
                currentCardIndex = (currentCardIndex + 1) % cardData.size
                loadCard(currentCardIndex)
            }

            override fun onSwipeProgress(direction: SwipeDirection, progress: Float) {
                // Update UI based on swipe progress
                Log.d("SwipeCard", "Direction: $direction, Progress: $progress")
            }

            override fun onSwipeCancelled() {

            }
        })

        // Load initial card
        loadCard(currentCardIndex)
    }

    private fun loadCard(index: Int) {
        val card = cardData[index]
        swipeCard.setCardImage(card.imageRes)
        // Title and subtitle are not shown anymore
    }

    data class CardItem(
        val title: String,
        val subtitle: String,
        val imageRes: Int
    )
}