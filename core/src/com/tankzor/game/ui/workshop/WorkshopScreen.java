package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.game_resources.GameImages;
import com.tankzor.game.game_resources.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.research_model.ResearchModel;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.ui.BaseScreen;

/**
 * Created by Admin on 1/21/2017.
 */

public class WorkshopScreen extends BaseScreen {
    private static final String BACK_BUTTON_LABEL = "Exit Workshop";

    // ItemButtons
    private static final String ITEM_BUTTON_BUTTON_UPGRADE = "UpgradeButton";
    private static final String ITEM_BUTTON_BUTTON_RESEARCH = "ResearchButton";
    private static final String ITEM_BUTTON_BUTTON_WEAPON_ITEM = "WeaponItemButton";

    private ScrollPane scrollPane;
    private Stage screenStage;
    private ScreenTitle screenTitle;
    private Image background;
    private VerticalGroup mainContainer;
    private TextButton backButton;
    private Image lineImage;
    private Array<WorkshopMenu> listWorkshopMenu;
    private WorkshopMenu researchMenu;
    private BaseScreen previousScreen;
    private AllyTankRepairButton allyTankRepairButton;

    private WarMachineManager warMachineManager;
    private WeaponManager weaponManager;

    // ItemButton parameters
    private Integer buttonWidth;
    private Integer buttonHeight;
    private TextButton.TextButtonStyle buttonStyle;

    public WorkshopScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    public void setWeaponManager(WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    public void setWarMachineManager(WarMachineManager warMachineManager) {
        this.warMachineManager = warMachineManager;
        allyTankRepairButton.setAlliesWarMachines(warMachineManager.getAlliesMovableMachines());
    }

    public void updateAllWorkshop() {
        screenTitle.update();
        int money = PlayerProfile.getInstance().getMoney();
        for (int i = 0; i < listWorkshopMenu.size; i++) {
            listWorkshopMenu.get(i).updateAllButton(money);
        }
        researchMenu.updateAllButton(PlayerProfile.getInstance().getStar());
    }

    public void updateAllWeaponItem() {
        screenTitle.update();
        int money = PlayerProfile.getInstance().getMoney();
        for (int i = 0; i < listWorkshopMenu.size; i++) {
            listWorkshopMenu.get(i).updateAllButton(money);
        }
    }

    public void setPreviousScreen(BaseScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    // ItemButton parameters initialization
    private void initializeButtonWidth() {
        buttonWidth = Gdx.graphics.getWidth();

    }
    private void initializeButtonHeight() {
        buttonHeight = Math.round(Dimension.buttonHeight);
    }
    private void initializeButtonStyle() {
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = GameImages.getInstance().getUiSkin().getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.disabledFontColor = Color.LIGHT_GRAY;
        buttonStyle.overFontColor = Color.DARK_GRAY;
    }

    @Override
    protected void initViews() {
        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();

        Skin skin = GameImages.getInstance().getUiSkin();

        background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthScreen, heightScreen);

        screenTitle = new ScreenTitle(this, previousScreen, 0,
                heightScreen - Dimension.screenTitleHeight * 1.5f, widthScreen,
                Dimension.screenTitleHeight * 1.5f);

        // Initialize ItemButton parameters
        initializeButtonWidth();
        initializeButtonHeight();
        initializeButtonStyle();

        createMenu();

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        lineImage = new Image(lineDrawable);

        backButton = new TextButton(BACK_BUTTON_LABEL, buttonStyle);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerProfile.getInstance().savePlayerData();
                getParent().setScreen(previousScreen);
            }
        });
    }

    private void createMenu() {
        mainContainer = new VerticalGroup();
        mainContainer.fill();
        mainContainer.space(Dimension.buttonSpace / 2);
        listWorkshopMenu = new Array<>();

        WorkshopMenu weaponMenu = new WorkshopMenu(1, "Basic Ammunition");
        for (int i = Bullet.NORMAL_BULLET; i <= Bullet.ARMOR_PIERCING_BULLET; i++) {
            weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, i));
        }
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(7, "Additional Ammunition");
        for (int i = Bullet.MISSILE_BULLET; i <= AreaWeapon.AIR_STRIKE; i++) {
            weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, i));
        }
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(16, "Allies");
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.ALLY_TANK));
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.ALLY_KAMIKAZE_TANK));
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.ALLY_ARTILLERY_TANK));
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(20, "Defence");
        weaponMenu.addItemButton(new UpgradeButton(this, SupportItem.TEMPORARY_ARMOR, buttonWidth,
                buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                if (playerWarMachine != null) {
                    playerWarMachine.updateHitPoint(1);
                }
            }
        });
        weaponMenu.addItemButton(new UpgradeButton(this, SupportItem.PERMANENT_ARMOR, buttonWidth,
                buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                if (playerWarMachine != null) {
                    playerWarMachine.updateHitPoint(1);
                }
            }
        });
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.FORCE_FIELD));
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(22, "Repair");
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.REPAIR_KIT));
        TankRepairButton tankRepairButton = new TankRepairButton(this, warMachineManager,
                buttonWidth, buttonHeight, buttonStyle);
        weaponMenu.addItemButton(tankRepairButton);
        allyTankRepairButton = new AllyTankRepairButton(this,
                buttonWidth, buttonHeight, buttonStyle);
        weaponMenu.addItemButton(allyTankRepairButton);
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(25, "Other");
        weaponMenu.addItemButton(new UpgradeButton(this, SupportItem.RADAR, buttonWidth,
                buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                PlayerProfile playerProfileInstance = PlayerProfile.getInstance();
                if (playerProfileInstance.getResearchModel(ResearchModel.AIR_STRIKE_RESEARCH_ID).currentLevel >= 1) {
                    playerProfileInstance.getWeaponModel(AreaWeapon.AIR_STRIKE).unlocked = true;
                }
                super.onAddButtonPress();
            }
        });
        weaponMenu.addItemButton(new UpgradeButton(this, SupportItem.THERMOVISION, buttonWidth,
                buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                if(warMachineManager != null) {
                    warMachineManager.getPlayerWarMachine().updateThermovisionBound();
                }
            }
        });
        weaponMenu.addItemButton(new UpgradeButton(this, SupportItem.UPGRADE_TANK, buttonWidth,
                buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                if (warMachineManager != null) {
                    PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                    playerWarMachine.updateTankForm(WarMachine.HEAVY_TANK_TYPE);
                    playerWarMachine.updateHitPoint(3);
                }
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                playerProfile.addHP(3);
                playerProfile.getWeaponModel(Bullet.DOUBLE_NORMAL_BULLET).maxCapacity = 99;
                playerProfile.getWeaponModel(Bullet.DOUBLE_PLASMA_BULLET).maxCapacity = 99;
                playerProfile.getWeaponModel(Bullet.HIGH_EXPLOSIVE_BULLET).maxCapacity = 99;
                playerProfile.getWeaponModel(Bullet.ARMOR_PIERCING_BULLET).maxCapacity = 99;
                super.onAddButtonPress();
            }
        });
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.BOOST_SPEED));
        weaponMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_WEAPON_ITEM, SupportItem.TIME_FREEZE));
        weaponMenu.addItemButton(new UpgradeButton(this, SupportItem.LIFE_ITEM,
                buttonWidth, buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
                PlayerProfile.getInstance().addMoney(-PlayerProfile.getInstance().getWeaponModel(SupportItem.LIFE_ITEM).value);
                PlayerProfile.getInstance().addLife(1);
                updateAllWeaponItem();
            }
        });
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        researchMenu = new WorkshopMenu(19, "Research");
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.ADDITIONAL_INTEREST_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.ROUNDS_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.ARTILLERY_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.MISSILES_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.MINES_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.DYNAMITE_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.ARMOR_RESEARCH_ID));
        researchMenu.addItemButton(new ResearchButton(this, ResearchModel.FORCE_FIELD_RESEARCH_ID,
                buttonWidth, buttonHeight, buttonStyle) {
            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                if (playerWarMachine != null) {
                    playerWarMachine.getForceField()
                            .setBaseRecoverTime(PlayerProfile.getInstance().getForceFieldRecoverTime());
                }
            }
        });
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.AIR_STRIKE_RESEARCH_ID));
        researchMenu.addItemButton(formItemButton(ITEM_BUTTON_BUTTON_RESEARCH,
                ResearchModel.ALLY_TANK_RESEARCH_ID));
        mainContainer.addActor(researchMenu);
    }

    private ItemButton formItemButton(String itemButtonId, Integer additionalButtonId) {
        ItemButton button = null;

        switch (itemButtonId) {
            case ITEM_BUTTON_BUTTON_UPGRADE: {
                button = new UpgradeButton(this, additionalButtonId,
                        buttonWidth, buttonHeight, buttonStyle);
                break;
            }
            case ITEM_BUTTON_BUTTON_WEAPON_ITEM: {
                button = new WeaponItemButton(this, weaponManager, additionalButtonId,
                        buttonWidth, buttonHeight, buttonStyle);
                break;
            }
            case ITEM_BUTTON_BUTTON_RESEARCH: {
                button = new ResearchButton(this, additionalButtonId, buttonWidth, buttonHeight, buttonStyle);
                break;
            }
        }

        return button;
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(background);
        screenStage.addActor(screenTitle);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.padTop(Dimension.buttonSpace / 3);
        buttonGroup.space(Dimension.buttonSpace / 2);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(backButton);
        mainContainer.addActor(buttonGroup);

        scrollPane = new ScrollPane(mainContainer);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - screenTitle.getHeight());
        scrollPane.setTouchable(Touchable.enabled);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setSmoothScrolling(true);
        screenStage.addActor(scrollPane);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void show() {
//        scrollPane.scrollTo(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameMultiplexer.addProcessor(screenStage);
        updateAllWorkshop();
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
        warMachineManager = null;
        weaponManager = null;
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }
}
