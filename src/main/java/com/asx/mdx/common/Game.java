package com.asx.mdx.common;

import java.util.ArrayList;
import java.util.List;

import com.asx.mdx.common.minecraft.entity.player.inventory.Inventories;

import com.asx.mdx.internal.MDX;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A class designed to provide easy access to a variety of Minecraft core methods, some
 * of which may naturally have restricted access. The names of these methods will never change.
 */
public class Game
{
    public static final Game instance = new Game();

    protected Game() {}
    
    /**
     * Finds all IRecipe instances registered to a specific Item or Block instance.
     * 
     * @param obj - Item or Block instance to scan for recipes.
     * @return All found instances of IRecipes registered to the specified Item or Block.
     */
    public List<IRecipe> getRecipes(Object obj)
    {
        ItemStack stack = Inventories.newStack(obj);
        List<IRecipe> foundRecipes = new ArrayList<IRecipe>();

        if (stack != null)
        {
            for (ResourceLocation res : CraftingManager.REGISTRY.getKeys())
            {
                IRecipe recipe = CraftingManager.REGISTRY.getObject(res);
                
                if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == stack.getItem())
                {
                    foundRecipes.add(recipe);
                }
            }
        }

        return foundRecipes;
    }

    /**
     * Finds the first IRecipe instance registered to a specific Item or Block instance.
     * 
     * @param obj - Item or Block instance to scan for recipes.
     * @return First found instance of an IRecipe registered to the specified Item or Block.
     */
    public IRecipe getRecipe(Object obj)
    {
        List<IRecipe> recipes = getRecipes(obj);
        
        if (recipes != null && recipes.size() > 0)
        {
            return recipes.get(0);
        }
        
        return null;
    }

    /**
     * Wrapper method for the registerKeyBinding method found in ClientRegistry. Allows for
     * more efficient registration of a KeyBinding. 
     * 
     * @param keyName - Name of the KeyBinding to be registered.
     * @param key - Integer assigned to each individual keyboard key in the Keyboard class.
     * @param keyGroup - Group of KeyBindings to assign this KeyBinding to.
     * @return The KeyBinding Instance created from the provided parameters.
     */
    @SideOnly(Side.CLIENT)
    public KeyBinding registerKeybinding(String keyName, int key, String keyGroup)
    {
        KeyBinding keybind = new KeyBinding(String.format("key.%s", keyName), key, keyGroup);
        ClientRegistry.registerKeyBinding(keybind);
        return keybind;
    }

    /** 
     * Retrieve the ModContainer instance for a mod with the specified ID.
     * 
     * @param id - ID of the mod retrieving an instance from.
     * @return An instance of ModContainer that is assigned to this ID.
     */
    public ModContainer getModContainer(String id)
    {
        for (ModContainer container : Loader.instance().getModList())
        {
            if (container.getModId().equalsIgnoreCase(id))
            {
                return container;
            }
        }

        return null;
    }

    /** Retrieve the mod id of a mod class from the @Mod annotation. **/
    public final String getAnnotatedModId(Class<?> clazz)
    {
        if (clazz.isAnnotationPresent(Mod.class))
        {
            Mod mod = clazz.getAnnotation(Mod.class);

            return mod.modid();
        }

        return null;
    }
}
