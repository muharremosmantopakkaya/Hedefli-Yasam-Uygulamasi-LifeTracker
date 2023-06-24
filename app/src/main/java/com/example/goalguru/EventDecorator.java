package com.example.goalguru;

import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class EventDecorator implements DayViewDecorator {

    private final CalendarDay eventDate;
    private Drawable backgroundDrawable;

    public EventDecorator(CalendarDay eventDate, Drawable backgroundDrawable) {
        this.eventDate = eventDate;
        this.backgroundDrawable = backgroundDrawable;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return eventDate != null && day.equals(eventDate);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(backgroundDrawable);
    }
}
