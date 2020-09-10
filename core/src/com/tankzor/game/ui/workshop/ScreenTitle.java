package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.game_resources.GameImages;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.ui.BaseScreen;

public class ScreenTitle extends Group {
    Label moneyLabel, starLabel, lifeLabel;
    Image background;
    PlayerProfile playerProfile = PlayerProfile.getInstance();

    ScreenTitle(WorkshopScreen workshopScreen, BaseScreen previousScreen, float x,
                float y, float width, float height) {
        setBounds(x, y, width, height);
        Skin skin = GameImages.getInstance().getUiSkin();
        background = new Image(skin.getDrawable(GameImages.KEY_LABEL_BACKGROUND));
        background.setBounds(0, 0, width, height);
        addActor(background);

        Table iconGroup = new Table();
        iconGroup.padLeft(Dimension.buttonSpace * 2);
        iconGroup.setBounds(0, 0, width, height);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;
        TextButton backButton = new TextButton("<< BACK", buttonStyle);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.setBounds(10, 0, Dimension.buttonWidth / 3, height);
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerProfile.getInstance().savePlayerData();
                workshopScreen.getParent().setScreen(previousScreen);
            }
        });

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();
        Label money = new Label("Coins: ", labelStyle);
        money.setFontScale(Dimension.normalFontScale);
        moneyLabel = new Label(playerProfile.getMoney() + "", labelStyle);
        moneyLabel.setAlignment(Align.center);
        moneyLabel.setFontScale(Dimension.normalFontScale);


        Label star = new Label("Stars: ", labelStyle);
        star.setFontScale(Dimension.normalFontScale);
        starLabel = new Label(playerProfile.getStar() + "", labelStyle);
        starLabel.setAlignment(Align.center);
        starLabel.setFontScale(Dimension.normalFontScale);

        Label life = new Label("Lives: ", labelStyle);
        life.setFontScale(Dimension.normalFontScale);
        lifeLabel = new Label(playerProfile.getLife() + "", labelStyle);
        lifeLabel.setAlignment(Align.center);
        lifeLabel.setFontScale(Dimension.normalFontScale);

        iconGroup.add(money);
        iconGroup.add(moneyLabel).padRight(Dimension.screenTitleHeight);
        iconGroup.add(star);
        iconGroup.add(starLabel).padRight(Dimension.screenTitleHeight);
        iconGroup.add(life);
        iconGroup.add(lifeLabel);
        iconGroup.align(Align.center);

        addActor(iconGroup);
        addActor(backButton);
    }

    public void update() {
        moneyLabel.setText(playerProfile.getMoney() + "");
        starLabel.setText(playerProfile.getStar() + "");
        lifeLabel.setText(playerProfile.getLife() + "");
    }
}
