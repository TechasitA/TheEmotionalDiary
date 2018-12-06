package com.example.a9onhud.theemotionaldiary;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertEvent(Event event);

    @Query("UPDATE event SET story = :story, feeling = :emo WHERE id = :id")
    void updateEvent(int id, String story, String emo);

    @Query("SELECT * FROM event")
    List<Event> getEvents();

    @Query("SELECT * FROM event WHERE date = :date")
    List<Event> getEventsByDate(String date);



    @Query("DELETE FROM event WHERE id = :id")
    void deleteEvent(int id);
}