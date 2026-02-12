package org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.idea;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;

public interface IdeaKeyable {
    String getKey();
    boolean isValidKey(CastingEnvironment env);
}
