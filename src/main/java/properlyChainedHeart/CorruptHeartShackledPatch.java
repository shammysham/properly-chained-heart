package properlyChainedHeart;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import javassist.CtBehavior;

public class CorruptHeartShackledPatch {

  @SpirePatch(
      clz = CorruptHeart.class,
      method = "takeTurn"
  )
  public static class CorruptHeartTakeTurnPatch {

    @SpireInsertPatch(
        locator = Locator.class,
        localvars = {"additionalAmount"}
    )
    public static void onTakeTurn(CorruptHeart __instance, int additionalAmount) {
      GainStrengthPower shackledPower = (GainStrengthPower) __instance.getPower(GainStrengthPower.POWER_ID);
      if(shackledPower != null) {
        int stacksToRemove = -1 * additionalAmount;
        if(stacksToRemove > shackledPower.amount) {
          stacksToRemove = shackledPower.amount;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new GainStrengthPower(__instance, stacksToRemove)));
      }
    }
  }

  public static class Locator extends SpireInsertLocator {

    @Override
    public int[] Locate(CtBehavior ctBehavior) throws Exception {
      return new int[]{LineFinder.findInOrder(
          ctBehavior,
          new Matcher.FieldAccessMatcher(
              CorruptHeart.class,
              "buffCount"
          )
      )[0]};
    }
  }
}
