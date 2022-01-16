package com.example.deportesdalda

import com.google.firebase.database.Exclude

class Products {
    private var name: String? = null
    private var description: String? = null
    private var price: String? = null
    private var id: String? = null

    @Exclude
    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getName(): String? {
        return name
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun setPrice(price: String?) {
        this.price = price
    }

    fun getPrice(): String? {
        return price
    }
}