package com.example.socialapp.Utils.Observer;

import com.example.socialapp.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}