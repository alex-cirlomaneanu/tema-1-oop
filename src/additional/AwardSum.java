package additional;

import java.util.Collection;



public class AwardSum {
    /**
     * Additional class with a method that calculates the total number of awarads an actor has.
     */
    public int awardSum(final fileio.ActorInputData actor) {
        Collection<Integer> allAwards = actor.getAwards().values();
        int totalAwards = 0;
        for (Integer num : allAwards) {
            totalAwards += num;
        }
        return totalAwards;
    }
}
