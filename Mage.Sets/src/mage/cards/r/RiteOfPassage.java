/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.r;

import java.util.UUID;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.counter.AddCountersTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author Plopman
 */
public class RiteOfPassage extends CardImpl {

    public RiteOfPassage(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{2}{G}");

        // Whenever a creature you control is dealt damage, put a +1/+1 counter on it.
        Effect effect = new AddCountersTargetEffect(CounterType.P1P1.createInstance());
        effect.setText("put a +1/+1 counter on it");
        this.addAbility(new RiteOfPassageTriggeredAbility(effect));

    }

    public RiteOfPassage(final RiteOfPassage card) {
        super(card);
    }

    @Override
    public RiteOfPassage copy() {
        return new RiteOfPassage(this);
    }
}

class RiteOfPassageTriggeredAbility extends TriggeredAbilityImpl {

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent();

    public RiteOfPassageTriggeredAbility(Effect effect) {
        super(Zone.BATTLEFIELD, effect);
    }

    public RiteOfPassageTriggeredAbility(final RiteOfPassageTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public RiteOfPassageTriggeredAbility copy() {
        return new RiteOfPassageTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_CREATURE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        UUID targetId = event.getTargetId();
        Permanent permanent = game.getPermanent(targetId);
        if (permanent != null && filter.match(permanent, getSourceId(), getControllerId(), game)) {
            for (Effect effect : this.getEffects()) {
                effect.setTargetPointer(new FixedTarget(event.getTargetId()));
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever a creature you control is dealt damage, " + super.getRule();
    }
}
