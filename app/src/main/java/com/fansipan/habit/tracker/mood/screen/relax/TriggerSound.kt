package com.fansipan.habit.tracker.mood.screen.relax

import com.fansipan.habit.tracker.mood.R

enum class TriggerSound(val soundNameId: Int, val imageSource: Int, val url: String) {
    CatPurr(R.string.cat_purr, R.drawable.trigger_sound_1, "cat_purr.mp3"),
    WoodFishRs(R.string.wood_fish_rs, R.drawable.trigger_sound_2, "woodfish_rs1.mp3"),
    WoodFishTk(R.string.wood_fish_tk, R.drawable.trigger_sound_3, "sleepy-cat.mp3"),
    SingingBowl(R.string.singing_bowl, R.drawable.trigger_sound_4, "bird.mp3"),
    Mute(R.string.mute, R.drawable.mute, "")
}