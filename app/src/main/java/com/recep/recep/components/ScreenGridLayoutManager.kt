package com.recep.recep.components

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.GridLayoutManager

class ScreenGridLayoutManager: GridLayoutManager {
    constructor(context: Context, resources: Resources) :
            super(context, maxOf(1, resources.configuration.screenWidthDp / IMAGE_WIDTH_DP))

    companion object {
        const val IMAGE_WIDTH_DP = 368
    }
}