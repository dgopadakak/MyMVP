package com.example.mymvp.view

interface Viewable
{
    fun showTime(time: String)
    fun showLedStatus(status: Boolean)
    fun changeConnectionStatus(statusNum: Int)
    fun changeSwitchEnabled(status: Boolean)
    fun makeToast(text: String)
}