# **🃏 SideSwipeCards**
---

**SideSwipeCards** is a lightweight Android library that provides a **smooth, customizable left/right swipeable card view,** similar to Tinder-style interactions.
It supports gesture-based swiping, rotation, scaling, and fully customizable animations via XML attributes.

---

## ✨ **Features**

- 🔄 Smooth left & right swipe gestures
- 🎯 Swipe progress callbacks
- 🎨 Fully customizable via XML attributes
- ⚡ Smooth animations (rotation, scale, translation)

  ---

# **Preview**
---
<p align="center">
  <img src="https://github.com/user-attachments/assets/29556e45-6a89-4b7b-bc79-c2947130702e"
       alt="Demo GIF"
       width="200">


</p>

## ⚡ **Installation**

**Step 1:** Add JitPack repository to your root build.gradle:

```gradle
maven { url = uri("https://jitpack.io") }
```

**Step 2:** Add the dependency in your app `build.gradle` (example if hosted on JitPack):  

```gradle
dependencies {
    	        	        implementation 'com.github.Excelsior-Technologies-Community:SideSwipeCards:1.0.0'


}
```

## ⚡ **Usage**

```

<com.ext.side_swipe_cards.SideSwipeCardView
    android:id="@+id/swipeCard"
    android:layout_width="300dp"
    android:layout_height="420dp"
    app:swipeThreshold="300dp"
    app:rotationFactor="20"
    app:verticalDragFactor="0.2"
    app:scaleDownFactor="0.05"
    app:animationDuration="300"
    app:resetDuration="200"
    app:cardCornerRadius="24dp"
    app:cardElevation="12dp" />


```
## **Mainactivity code**
```
// 1️⃣ Initialize the card view
swipeCard = findViewById(R.id.swipeCard)

// 2️⃣ Set swipe listener
swipeCard.setOnSwipeListener(object : OnSwipeListener {
    override fun onSwiped(direction: SwipeDirection) {
        // Called when card is swiped LEFT or RIGHT
        // Example: load next card
        currentCardIndex = (currentCardIndex + 1) % cardData.size
        loadCard(currentCardIndex)
    }

    override fun onSwipeProgress(direction: SwipeDirection, progress: Float) {
        // Optional: called during swipe
    }

    override fun onSwipeCancelled() {
        // Optional: called when swipe is released below threshold
    }
})

// 3️⃣ Load a card (initial or next)
loadCard(currentCardIndex)

// 4️⃣ Set card image
swipeCard.setCardImage(cardData[currentCardIndex].imageRes)


```

## **attrs file:**

```
<resources>
    <declare-styleable name="SideSwipeCardView">
        <attr name="swipeThreshold" format="dimension" />
        <attr name="rotationFactor" format="float" />
        <attr name="verticalDragFactor" format="float" />
        <attr name="scaleDownFactor" format="float" />
        <attr name="animationDuration" format="integer" />
        <attr name="resetDuration" format="integer" />
        <attr name="cardCornerRadius" format="dimension" />
        <attr name="cardElevation" format="dimension" />
        <!-- Add any others like swipeThreshold if needed -->
    </declare-styleable>
</resources>
```



## **📄 License**

**MIT License**  
```
Copyright (c) 2025 Excelsior Technologies

Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all  
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED **"AS IS"**, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```
