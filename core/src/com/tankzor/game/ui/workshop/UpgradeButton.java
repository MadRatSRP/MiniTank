package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.game_resources.GameImages;
import com.tankzor.game.game_resources.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.ui.WeaponDetailScreen;

public class UpgradeButton extends ItemButton {
    Label valueLabel, capacityLabel;
    WorkshopScreen workshopScreen;

    UpgradeButton(WorkshopScreen workshopScreen, int id, float width, float height,
                  TextButtonStyle style) {
        super(id, width, height, style);

        this.workshopScreen = workshopScreen;

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

        capacityLabel = new Label("", labelStyle);
        capacityLabel.setAlignment(Align.center);
        capacityLabel.setFontScale(Dimension.normalFontScale);
        add(capacityLabel).align(Align.right).fillY().width(100);

        valueLabel = new Label("", labelStyle);
        valueLabel.setAlignment(Align.center);
        valueLabel.setFontScale(Dimension.normalFontScale);
        add(valueLabel).align(Align.right).width(120);

        add(addButton).padRight(Dimension.buttonSpace / 2);

        setText(PlayerProfile.getInstance().getWeaponModel(id).name);
    }

    @Override
    void update(int value) {
        WeaponModel weaponModel = PlayerProfile.getInstance().getWeaponModel(id);
        if (!weaponModel.unlocked || weaponModel.capacity == weaponModel.maxCapacity) {
            setDisabled(true);
            if (weaponModel.capacity != 0) {
                capacityLabel.setText(weaponModel.capacity + "");
            } else {
                capacityLabel.setText("");
            }
            valueLabel.setText("");
            addButton.setVisible(false);
            return;
        }
        setDisabled(false);
        valueLabel.setText(weaponModel.value + "");
        if (weaponModel.capacity != 0) {
            capacityLabel.setText(weaponModel.capacity + "");
        } else {
            capacityLabel.setText("");
        }
        addButton.setVisible(value > weaponModel.value);
    }

    @Override
    void onPress() {
        Tankzor parent = workshopScreen.getParent();
        WeaponDetailScreen weaponDetailScreen = parent.getWeaponDetailScreen();
        weaponDetailScreen.setWeaponModel(PlayerProfile.getInstance().getWeaponModel(id));
        parent.setScreen(weaponDetailScreen);
    }

    @Override
    void onAddButtonPress() {
        GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
        PlayerProfile playerProfile = PlayerProfile.getInstance();
        WeaponModel weaponModel = playerProfile.getWeaponModel(id);
        playerProfile.updateWeapon(id, 1);
        playerProfile.addMoney(-weaponModel.value);

        workshopScreen.updateAllWeaponItem();
    }
}
