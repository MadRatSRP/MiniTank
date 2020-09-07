package com.tankzor.game.ui.workshop;

import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.support_item.SupportItem;

public class TankRepairButton extends UpgradeButton {
    int currentPrice = -1;
    WarMachineManager warMachineManager;

    TankRepairButton(WorkshopScreen workshopScreen, WarMachineManager warMachineManager,
                     float width, float height, TextButtonStyle style) {
        super(workshopScreen, SupportItem.REPAIR_TANK, width, height, style);
        this.warMachineManager = warMachineManager;
    }

    void calculateRepairCost() {
        if (warMachineManager == null) {
            currentPrice = 0;
            return;
        }
        PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
        int hitPoint = playerWarMachine.getHitPoint();
        int maxHitPoint = playerWarMachine.getMaxHitPoint();
        if (hitPoint == maxHitPoint) {
            currentPrice = 0;
            return;
        }
        currentPrice = (maxHitPoint - hitPoint) * PlayerProfile.getInstance().getWeaponModel(id).value;
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
        PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
        playerWarMachine.setHitPoint(playerWarMachine.getMaxHitPoint());
        PlayerProfile.getInstance().addMoney(-currentPrice);

        workshopScreen.updateAllWeaponItem();
    }
}
