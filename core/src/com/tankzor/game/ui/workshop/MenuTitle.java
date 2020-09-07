package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;

public class MenuTitle extends Group {
    MenuTitle(int iconId, String title, float width, float height) {
        setSize(width, height);
        Skin skin = GameImages.getInstance().getUiSkin();
        Image background = new Image(skin.getDrawable(GameImages.KEY_TITLE_MENU_ITEM_BACKGROUND));
        background.setBounds(0, 0, width, height);
        addActor(background);

        Image icon = new Image(GameImages.getInstance().getIcon(iconId));
        Drawable drawable = icon.getDrawable();
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        Label label = new Label(title, GameImages.getInstance().getLabelStyle());
        label.setFontScale(Dimension.quiteLargeFontScale);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.setBounds(0, 0, width, height);
        horizontalGroup.padLeft(Dimension.buttonSpace / 2);
        horizontalGroup.space(Dimension.buttonSpace / 2);
        horizontalGroup.align(Align.left);

        horizontalGroup.addActor(icon);
        horizontalGroup.addActor(label);
        addActor(horizontalGroup);
    }
}
