package me.decclient.mod.gui.screen;

import me.decclient.api.managers.Managers;
import me.decclient.mod.Mod;
import me.decclient.mod.gui.click.Component;
import me.decclient.mod.gui.click.items.Item;
import me.decclient.mod.gui.click.items.buttons.ModuleButton;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class DecAppearance extends GuiScreen {

    private static DecAppearance HUDEditorClickGui;
    private static DecAppearance INSTANCE;

    static {
        INSTANCE = new DecAppearance();
    }

    private final ArrayList<Component> components = new ArrayList();

    public DecAppearance() {
        setInstance();
        load();
    }

    public static DecAppearance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DecAppearance();
        }
        return INSTANCE;
    }

    public static DecAppearance getClickGui() {
        return getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -84;
        for (Category category : Managers.MODULES.getCategories()) {
            if (category == Category.HUD) {
                components.add(new Component(category.getName(), x += 90, 4, true) {

                    @Override
                    public void setupItems() {
                        counter1 = new int[]{1};
                        Managers.MODULES.getModulesByCategory(category).forEach(module -> addButton(new ModuleButton(module)));
                    }
                });
            }
        }
        components.forEach(components -> components.getItems().sort(Comparator.comparing(Mod::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton) item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        checkMouseWheel();
        components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    @Override
    public void onGuiClosed() {
        try {
            super.onGuiClosed();
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

