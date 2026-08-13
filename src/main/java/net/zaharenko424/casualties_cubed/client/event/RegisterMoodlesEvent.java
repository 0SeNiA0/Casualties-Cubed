package net.zaharenko424.casualties_cubed.client.event;

import net.minecraftforge.eventbus.api.Event;
import net.zaharenko424.casualties_cubed.client.moodles.AbstractMoodle;

import java.util.List;

public class RegisterMoodlesEvent extends Event {

    private final List<AbstractMoodle> moodles;

    public RegisterMoodlesEvent(List<AbstractMoodle> moodles) {
        this.moodles = moodles;
    }

    public void registerMoodle(AbstractMoodle moodle) {
        moodles.add(moodle);
    }
}
