package com.tankzor.game.game_resources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tankzor.game.common_value.Dimension;

public class GameStyles {
    private static GameStyles instance = new GameStyles();

    private TextButton.TextButtonStyle menuScreenTextButtonStyle;

    public static GameStyles getInstance() {
        return instance;
    }

    private GameStyles() {
        initializeMenuScreenTextButtonStyle();
    }

    private void initializeMenuScreenTextButtonStyle() {
        final Skin skin = GameImages.getInstance().getUiSkin();

        menuScreenTextButtonStyle = new TextButton.TextButtonStyle();
        menuScreenTextButtonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        menuScreenTextButtonStyle.down.setMinWidth(Dimension.buttonWidth);
        menuScreenTextButtonStyle.down.setMinHeight(Dimension.buttonHeight);
        menuScreenTextButtonStyle.font = GameImages.getInstance().getGameFont();
        menuScreenTextButtonStyle.fontColor = Color.WHITE;
        menuScreenTextButtonStyle.disabledFontColor = Color.DARK_GRAY;
        menuScreenTextButtonStyle.overFontColor = Color.LIGHT_GRAY;
    }

    public TextButton.TextButtonStyle getMenuScreenTextButtonStyle() {
        return menuScreenTextButtonStyle;
    }
}
