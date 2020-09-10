package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.utils.Array;
import com.tankzor.game.game_resources.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.support_item.SupportItem;

public class AllyTankRepairButton extends UpgradeButton {
    int currentPrice = -1;
    Array<MovableWarMachine> alliesWarMachines;

    AllyTankRepairButton(WorkshopScreen workshopScreen, float width, float height, TextButtonStyle style) {
        super(workshopScreen, SupportItem.REPAIR_ALLY_TANK, width, height, style);
    }

    void setAlliesWarMachines(Array<MovableWarMachine> alliesWarMachines) {
        this.alliesWarMachines = alliesWarMachines;
    }

    void calculateRepairCost() {
        if (alliesWarMachines == null) {
            currentPrice = 0;
            return;
        }
        int totalHitPointLost = 0;
        for (int i = 0; i < alliesWarMachines.size; i++) {
            WarMachine warMachine = alliesWarMachines.get(i);
            totalHitPointLost += warMachine.getMaxHitPoint() - warMachine.getHitPoint();
        }
        if (totalHitPointLost == 0) {
            currentPrice = 0;
            return;
        }
        currentPrice = totalHitPointLost * PlayerProfile.getInstance().getWeaponModel(id).value;
    }

    @Override
    void update(int value) {
        if (currentPrice == -1) {
            calculateRepairCost();
        }
        if (currentPrice > 0) {
            setDisabled(false);
            valueLabel.setText(currentPrice + "");
            if (currentPrice <= value) {
                addButton.setVisible(true);
            } else {
                addButton.setVisible(false);
            }
        } else {
            setDisabled(true);
            valueLabel.setText("");
            addButton.setVisible(false);
        }
    }

    @Override
    void onAddButtonPress() {
        GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
        for (int i = 0; i < alliesWarMachines.size; i++) {
            WarMachine warMachine = alliesWarMachines.get(i);
            warMachine.setHitPoint(warMachine.getMaxHitPoint());
        }
        PlayerProfile.getInstance().addMoney(-currentPrice);

        workshopScreen.updateAllWeaponItem();
    }
}
