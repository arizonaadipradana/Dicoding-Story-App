package com.uberalles.dicodingstorysubmission.ui.main

import com.uberalles.dicodingstorysubmission.repos.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "description $i",
                (i * 1.0),
                "id $i",
                (i * 1.0),
            )
            items.add(story)
        }
        return items
    }
}