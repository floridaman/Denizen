package net.aufdemrand.denizen.objects.properties.item;

import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemFlags implements Property {

    public static boolean describes(dObject item) {
        // All items can have flags
        return item instanceof dItem;
    }

    public static ItemFlags getFrom(dObject _item) {
        if (!describes(_item)) return null;
        else return new ItemFlags((dItem)_item);
    }

    private ItemFlags(dItem _item) {
        item = _item;
    }

    public String flags() {
        dList output = new dList();
        for (ItemFlag flag: item.getItemStack().getItemMeta().getItemFlags()) {
            output.add(flag.name());
        }
        return output.identify();
    }

    dItem item;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute == null) return "null";

        // <--[tag]
        // @attribute <i@item.flags>
        // @returns dList
        // @mechanism dItem.flags
        // @group properties
        // @description
        // Returns a list of flags set on this item.
        // Valid flags include: HIDE_ATTRUBTES, HIDE_DESTROYS, HIDE_ENCHANTS, HIDE_PLACED_ON, HIDE_POTION_EFFECTS, and HIDE_UNBREAKABLE
        // -->
        if (attribute.startsWith("flags")) {
            return new Element(flags())
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }


    @Override
    public String getPropertyString() {
        return flags();
    }

    @Override
    public String getPropertyId() {
        return "flags";
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object dItem
        // @name flag
        // @input dList
        // @description
        // Sets the item's meta flag set.
        // @tags
        // <i@item.flags>
        // -->

        if (mechanism.matches("flags")) {
            ItemMeta meta = item.getItemStack().getItemMeta();
            meta.removeItemFlags(ItemFlag.values());
            dList new_flags = mechanism.getValue().asType(dList.class);
            for (String str: new_flags) {
                meta.addItemFlags(ItemFlag.valueOf(str));
            }
            item.getItemStack().setItemMeta(meta);
        }

    }
}
