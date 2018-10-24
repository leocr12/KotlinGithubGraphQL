package com.example.leonardoallen.strafe.detail

import com.example.leonardoallen.strafe.EntryDetailQuery

interface EntryDetailContract {

    interface View {
        fun setEntryData(data: EntryDetailQuery.Data)
    }

    interface Presenter {
        fun onDestroy()
    }
}