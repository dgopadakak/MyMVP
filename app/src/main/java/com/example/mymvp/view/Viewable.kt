package com.example.mymvp.view

interface Viewable
{
    fun showTime(time: String)
    fun showLedStatus(status: Boolean)
    fun changeConnectionStatus(status: Boolean)
    fun changeChipEnabled(status: Boolean)
}