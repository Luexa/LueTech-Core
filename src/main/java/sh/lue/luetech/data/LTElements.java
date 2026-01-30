package sh.lue.luetech.data;

import com.gregtechceu.gtceu.api.material.Element;

import static com.gregtechceu.gtceu.data.material.GTElements.createAndRegister;

public class LTElements {
    public static final Element A = createAndRegister(83, 130, -1, null, "Axolotium", "A", false);
    public static final Element Ad = createAndRegister(22, 24, -1, null, "Adamantium", "Ad", false);
    public static final Element E = createAndRegister(130, 221, -1, null, "Elementium", "E", false);
    public static final Element Ie = createAndRegister(47, 62, -1, null, "Iesnium", "Ie", false);
    public static final Element Qt = createAndRegister(384, 768, -1, null, "Quantonium", "Qt", false);

    public static void init() {}
}
