package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;

public abstract class ItemButton extends TextButton {
    private static final int DRAG_PIXEL_IGNORE = 20;

    ImageButton addButton;
    float xTouchDown, yTouchDown;
    boolean isDragged;
    boolean isTouchChild = false;
    int id;

    ItemButton(int id, float width, float height, TextButtonStyle style) {
        super("", style);
        this.id = id;
        setSize(width, height);

        padLeft(Dimension.buttonSpace);
        padRight(Dimension.buttonSpace);
        Label buttonLabel = getLabel();
        buttonLabel.setAlignment(Align.left);
        buttonLabel.setFontScale(Dimension.normalFontScale);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isTouchChild) {
                    return false;
                }
                xTouchDown = x;
                yTouchDown = y;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragged) {
                    isDragged = false;
                    return;
                }
                onPress();
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(xTouchDown - x) > DRAG_PIXEL_IGNORE || Math.abs(yTouchDown - y) > DRAG_PIXEL_IGNORE) {
                    isDragged = true;
                }
            }
        });

        Skin skin = GameImages.getInstance().getUiSkin();
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable drawable = skin.getDrawable(GameImages.KEY_ADD_BUTTON_PRESS_BACKGROUND);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.imageDown = drawable;
        imageButtonStyle.imageOver = drawable;
        imageButtonStyle.imageDisabled = drawable;
        drawable = skin.getDrawable(GameImages.KEY_ADD_BUTTON_NORMAL_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumIconSize);
        drawable.setMinHeight(Dimension.mediumIconSize);
        imageButtonStyle.imageUp = drawable;
        addButton = new ImageButton(imageButtonStyle);
        addButton.setVisible(false);
        addButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                xTouchDown = x;
                yTouchDown = y;
                isTouchChild = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isTouchChild = false;
                if (isDragged) {
                    isDragged = false;
                    return;
                }
                onAddButtonPress();
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(xTouchDown - x) > DRAG_PIXEL_IGNORE || Math.abs(yTouchDown - y) > DRAG_PIXEL_IGNORE) {
                    isDragged = true;
                }
            }
        });

        setTouchable(Touchable.childrenOnly);
    }

    abstract void update(int value);

    abstract void onPress();

    abstract void onAddButtonPress();
}
