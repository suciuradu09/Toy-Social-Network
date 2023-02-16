package com.example.socialapp.Utils.Events;

import com.example.socialapp.domain.User;

public class UtilizatorChangeData implements Event {
    private ChangeEventType type;
    private User data;
    private User oldData;

    public UtilizatorChangeData(ChangeEventType type, User data){
        this.type = type;
        this.data = data;
    }

    public UtilizatorChangeData(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}