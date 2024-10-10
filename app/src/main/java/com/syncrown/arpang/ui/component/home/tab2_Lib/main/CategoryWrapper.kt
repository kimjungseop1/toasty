package com.syncrown.arpang.ui.component.home.tab2_Lib.main

data class CategoryWrapper(
    val category: List<Category>
)

data class Category(
    val parentName: String,
    val child: List<Child>
)

data class Child(
    val childName: String
)