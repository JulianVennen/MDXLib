package com.asx.mdx.common.reflect;

import com.asx.mdx.internal.MDX;

public class AccessTransformer extends net.minecraftforge.fml.common.asm.transformers.AccessTransformer
{
    public AccessTransformer() throws Exception
    {
        super("mdxlib_at.cfg");
        MDX.log().info("Loading access transformer: %s", this.getClass().getName());
    }
}
