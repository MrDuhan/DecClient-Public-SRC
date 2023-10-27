package me.decclient.api.managers.impl;

import me.decclient.api.events.impl.Render2DEvent;
import me.decclient.api.events.impl.Render3DEvent;
import me.decclient.api.managers.Managers;
import me.decclient.mod.Mod;
import me.decclient.mod.gui.screen.DecClickGui;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.impl.client.*;
import me.decclient.mod.modules.impl.combat.*;
import me.decclient.mod.modules.impl.exploit.*;
import me.decclient.mod.modules.impl.misc.*;
import me.decclient.mod.modules.impl.movement.*;
import me.decclient.mod.modules.impl.player.*;
import me.decclient.mod.modules.impl.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager extends Mod {

    public static Minecraft mc = Minecraft.getMinecraft();

    public ArrayList<Module> modules = new ArrayList<>();

    public List<Module> sortedLength = new ArrayList<>();
    public List<String> sortedAbc = new ArrayList<>();

    public void init() {
        registerModules();
    }

    //Module sorting

    public enum Ordering {
        ABC,
        LENGTH
    }

    public void sortModules(Ordering ordering) {
        if (ordering == Ordering.LENGTH) {
            sortedLength = getEnabledModules()
                    .stream()
                    .filter(Module::isDrawn)
                    .sorted(Comparator.comparing(module ->
                            Managers.TEXT.getStringWidth(
                                    HUD.getInstance().lowerCase.getValue() ? module.getArrayListInfo().toLowerCase() : module.getArrayListInfo()
                            ) * (-1))).collect(Collectors.toList());

        } else {
            sortedAbc = new ArrayList<>(getEnabledModulesString());
            sortedAbc.sort(String.CASE_INSENSITIVE_ORDER);
        }
    }

    //Getters

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> modules = new ArrayList<>();

        for (Module module : this.modules) {
            if (!module.isOn()) continue;
            modules.add(module);
        }

        return modules;
    }

    public ArrayList<String> getEnabledModulesString() {
        ArrayList<String> modules = new ArrayList<>();

        for (Module module : this.modules) {
            if (!module.isOn() || !module.isDrawn()) continue;
            modules.add(module.getArrayListInfo());
        }

        return modules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;

            return module;
        }

        return null;
    }

    public ArrayList<Module> getModulesByCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<>();

        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modules.add(module);
            }
        });

        return modules;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public List<Category> getCategories() {
        return Arrays.asList(Category.values());
    }

    //Listeners

    public void onUnloadPre() {
        modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyInput(int key) {
        if (key == 0 || !Keyboard.getEventKeyState() || mc.currentScreen instanceof DecClickGui) return;

        modules.forEach(module -> {
            if (module.getBind().getKey() == key) {
                module.toggle();
            }
        });
    }

    //Registering override-able stuff from Module.java

    public void onLoad() {
        modules.stream().filter(Module::isListening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Module::isOn).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Module::isOn).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Module::isOn).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Module::isOn).forEach(module -> module.onRender3D(event));
    }

    public void onTotemPop(EntityPlayer player) {
        modules.stream().filter(Module::isOn).forEach(module -> module.onTotemPop(player));
    }

    public void onDeath(EntityPlayer player) {
        modules.stream().filter(Module::isOn).forEach(module -> module.onDeath(player));
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    /**
     * Plug method to increase readability level
     * You need to load the categories in this ordering to prevent your game getting fucked up and I don't know why this happens
     * - asphyxia
     */

    private void registerModules() {

        //Client

        modules.add(new ClickGui());
        modules.add(new FontMod());
        modules.add(new HUD());
        modules.add(new FovMod());
        modules.add(new DiscordRPC());
        modules.add(new Desktop());

        //Render

        modules.add(new Highlight());
        modules.add(new HoleESP());
        modules.add(new Model());
        modules.add(new Tracers());
        modules.add(new CrystalChams());
        modules.add(new Chams());
        modules.add(new NameTags());
        modules.add(new Ambience());
        modules.add(new ESP());
        modules.add(new BreadCrumbs());
        modules.add(new NoLag());  //Delete
        modules.add(new LogOutSpots());
        modules.add(new BreakingESP());
        modules.add(new InventoryPreview());
        modules.add(new Shader());
        modules.add(new FullBright());
        modules.add(new FreeLook());
        modules.add(new NoRender());
        modules.add(new TunnelESP());

        //Combat

        modules.add(new WebTrap());
        modules.add(new Aura());
        modules.add(new AntiTrap());
        modules.add(new AntiRegear());
        modules.add(new AutoArmor());
        modules.add(new KeyPearl());
        modules.add(new Criticals());
        modules.add(new SelfTrap());
        modules.add(new SelfWeb());
        modules.add(new AutoTrap());
        modules.add(new PacketExp());
        modules.add(new HoleFiller());
        modules.add(new Blocker());
        modules.add(new AutoFeetPlace());
        modules.add(new Reach());
        modules.add(new HitboxDesync());

        //Player

        modules.add(new FastExp());
        modules.add(new Replenish());
        modules.add(new ArmorWarner());
        modules.add(new FakePlayer());
        modules.add(new Announcer());
        modules.add(new FlagDetect());
        modules.add(new Scaffold());
        modules.add(new NameProtect()); //Delete

        //Misc
        
        modules.add(new BetterChat());
        modules.add(new KillEffects());
        modules.add(new PopNotify());
        modules.add(new GhastFarmer());
        modules.add(new Coords());
        modules.add(new PearlNotify());
        modules.add(new Peek());
        modules.add(new GhastNotifier());
        modules.add(new ToolTips());
        modules.add(new UnfocusedCPU()); //Delete
        modules.add(new MCF());
        modules.add(new DonkeyFinder());
        modules.add(new EGapFinder());
        modules.add(new VisualRange());
        
        //Movement
        
        modules.add(new FastWeb());
        modules.add(new FastFall());
        modules.add(new Sprint());
        modules.add(new AntiVoid());
        modules.add(new Velocity());
        
        //Exploit
        
        modules.add(new TPCoordLog());
        modules.add(new MultiTask());
        modules.add(new LiquidInteract());
        modules.add(new NoHitBox());
        modules.add(new Stresser());
        modules.add(new XCarry());
        modules.add(new PearlSpoof());
        modules.add(new Clip());
        modules.add(new NoInteract());
    }
}

