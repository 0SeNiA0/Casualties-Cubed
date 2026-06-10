package net.zaharenko424.casualties_cubed.client.event;

import net.minecraftforge.eventbus.api.Event;
import net.zaharenko424.casualties_cubed.client.moodles.AbstractMoodleVisual;

import java.util.List;

public class RegisterMoodlesEvent extends Event {

    private final List<AbstractMoodleVisual> moodles;

    public RegisterMoodlesEvent(List<AbstractMoodleVisual> moodles) {
        this.moodles = moodles;
    }

    public void registerMoodle(AbstractMoodleVisual moodle) {
        moodles.add(moodle);
    }
}
