package com.asx.mdx.common.minecraft.tile;

import net.minecraft.util.EnumFacing;

public interface IRotatableZAxis
{
    public EnumFacing getRotationZAxis();

    public void setRotationZAxis(EnumFacing facing);
}