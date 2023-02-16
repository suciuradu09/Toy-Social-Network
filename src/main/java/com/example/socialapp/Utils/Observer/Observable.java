package com.example.socialapp.Utils.Observer;

import com.example.socialapp.Utils.Events.Event;

public interface Observable <E extends Event> {
    void addObserver(Observer<E> e);

    void removeObserver(Observer<E> e);

    void notifyObservers(E t);
}