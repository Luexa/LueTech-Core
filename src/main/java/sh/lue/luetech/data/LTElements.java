package sh.lue.luetech.data;

import com.gregtechceu.gtceu.api.material.Element;

import static com.gregtechceu.gtceu.data.material.GTElements.createAndRegister;

public class LTElements {
    public static final Element E = createAndRegister(130, 221, -1, null, "Elementium", "E", false);
    public static final Element Qt = createAndRegister(384, 768, -1, null, "Quantonium", "Qt", false);

    public static void init() {}
}
